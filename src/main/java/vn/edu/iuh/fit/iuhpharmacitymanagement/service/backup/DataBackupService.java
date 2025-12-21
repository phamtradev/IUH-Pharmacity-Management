package vn.edu.iuh.fit.iuhpharmacitymanagement.service.backup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import vn.edu.iuh.fit.iuhpharmacitymanagement.connectDB.ConnectDB;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

// Thực hiện sao lưu/khôi phục toàn bộ dữ liệu SQL Server sang file zip
public class DataBackupService {

    private static final DateTimeFormatter FILE_TIME_FORMATTER
            = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").withLocale(Locale.US);
    private static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .create();
    private static final String METADATA_ENTRY = "metadata.json";

    // Thư mục mặc định trên máy người dùng để lưu file backup (*.zip)
    public Path getDefaultBackupDirectory() {
        return Path.of(System.getProperty("user.home"), "IUH-Pharmacity-Backups");
    }

    // Tạo 1 file backup mới (.zip), quét toàn bộ bảng và ghi dữ liệu sang JSON
    // progressCallback dùng để cập nhật trạng thái lên UI (label/loading)
    public BackupResult backup(Path targetDirectory, Consumer<String> progressCallback) throws IOException, SQLException {
        Objects.requireNonNull(targetDirectory, "targetDirectory");
        Files.createDirectories(targetDirectory);
        String timestamp = FILE_TIME_FORMATTER.format(LocalDateTime.now());
        Path backupFile = targetDirectory.resolve("backup_" + timestamp + ".zip");

        try (Connection connection = ConnectDB.getConnection()) {
            connection.setAutoCommit(false);
            String databaseName = connection.getCatalog();
            List<TableInfo> tables = loadTables(connection);
            long totalRows = 0;

            if (progressCallback != null) {
                progressCallback.accept("Đang thu thập " + tables.size() + " bảng dữ liệu...");
            }

            try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(backupFile))) {
                for (TableInfo table : tables) {
                    if (progressCallback != null) {
                        progressCallback.accept("Sao lưu bảng " + table.displayName());
                    }
                    JsonObject payload = exportTable(connection, table);
                    long rows = payload.getAsJsonArray("rows").size();
                    totalRows += rows;
                    ZipEntry entry = new ZipEntry("tables/" + table.fileName());
                    zos.putNextEntry(entry);
                    byte[] data = GSON.toJson(payload).getBytes(StandardCharsets.UTF_8);
                    zos.write(data);
                    zos.closeEntry();
                }

                JsonObject metadata = new JsonObject();
                metadata.addProperty("databaseName", databaseName);
                metadata.addProperty("createdAt", Instant.now().toString());
                metadata.addProperty("tableCount", tables.size());
                metadata.addProperty("rowCount", totalRows);
                metadata.addProperty("formatVersion", 1);
                zos.putNextEntry(new ZipEntry(METADATA_ENTRY));
                zos.write(GSON.toJson(metadata).getBytes(StandardCharsets.UTF_8));
                zos.closeEntry();
            }
            return new BackupResult(backupFile, databaseName, tables.size(), totalRows);
        }
    }

    // Khôi phục dữ liệu từ file backup (.zip) đã chọn
    // Hỗ trợ tự tạo database, tự sắp xếp lại thứ tự insert theo ràng buộc khóa ngoại
    public void restore(Path backupFile, Consumer<String> progressCallback) throws IOException, SQLException {
        Objects.requireNonNull(backupFile, "backupFile");
        if (!Files.exists(backupFile)) {
            throw new IOException("Không tìm thấy file sao lưu: " + backupFile);
        }

        if (progressCallback != null) {
            progressCallback.accept("Đang đọc file backup...");
        }
        List<TableDump> tablePayloads = loadBackupPayloads(backupFile, progressCallback);

        if (progressCallback != null) {
            progressCallback.accept("Đang kiểm tra database...");
        }
        // Tạo database nếu chưa tồn tại
        if (!ConnectDB.createDatabaseIfNotExists()) {
            throw new SQLException("Không thể tạo database. Vui lòng kiểm tra quyền truy cập SQL Server.");
        }

        if (progressCallback != null) {
            progressCallback.accept("Đang kết nối database...");
        }
        try (Connection connection = ConnectDB.getConnection()) {
            boolean initialAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            List<TableDump> orderedTables = null;
            try {
                if (progressCallback != null) {
                    progressCallback.accept("Đang sắp xếp thứ tự bảng...");
                }
                orderedTables = sortTablesForInsert(connection, tablePayloads);
                List<TableDump> deleteOrder = new ArrayList<>(orderedTables);
                Collections.reverse(deleteOrder);

                if (progressCallback != null) {
                    progressCallback.accept("Đang tắt ràng buộc...");
                }
                disableConstraints(connection, orderedTables);
                // Tắt trigger toàn cục để tránh trigger business logic (ví dụ validate expiry) chặn quá trình insert
                disableTriggers(connection, orderedTables);
                deleteExistingData(connection, deleteOrder, progressCallback);
                insertRows(connection, orderedTables, progressCallback);
                if (progressCallback != null) {
                    progressCallback.accept("Đang bật lại ràng buộc...");
                }
                enableConstraints(connection, orderedTables);
                // Bật lại triggers sau khi constraints đã được bật và dữ liệu đã sẵn sàng
                enableTriggers(connection, orderedTables);
                if (progressCallback != null) {
                    progressCallback.accept("Đang lưu thay đổi...");
                }
                connection.commit();
                if (progressCallback != null) {
                    progressCallback.accept("Hoàn tất!");
                }
            } catch (Exception ex) {
                connection.rollback();
                throw ex;
            } finally {
                // Đảm bảo luôn bật lại constraints, kể cả khi có lỗi
                if (orderedTables != null) {
                    try {
                        enableConstraints(connection, orderedTables);
                        enableTriggers(connection, orderedTables);
                    } catch (SQLException e) {
                        // Log lỗi nhưng không throw để không che giấu lỗi gốc
                        System.err.println("Lỗi khi bật lại constraints: " + e.getMessage());
                    }
                }
                connection.setAutoCommit(initialAutoCommit);
            }
        }
    }

    /**
     * Xóa toàn bộ dữ liệu hiện có trong các bảng trước khi insert dữ liệu mới
     * từ file backup. Được gọi trong quá trình {@link #restore(Path, Consumer)}
     * sau khi đã tắt ràng buộc khóa ngoại.
     */
    private void deleteExistingData(Connection connection,
            List<TableDump> tables,
            Consumer<String> progressCallback) throws SQLException {
        // Dùng DELETE FROM thay vì TRUNCATE vì TRUNCATE không hoạt động 
        // khi có foreign key references, ngay cả khi đã disable constraints
        for (TableDump dump : tables) {
            // Kiểm tra xem bảng có tồn tại không
            if (!tableExists(connection, dump)) {
                // Bảng chưa tồn tại, bỏ qua
                continue;
            }
            if (progressCallback != null) {
                progressCallback.accept("Đang xóa dữ liệu bảng " + dump.displayName());
            }
            try (Statement statement = connection.createStatement()) {
                // Dùng DELETE FROM vì đã disable constraints nên sẽ không có lỗi foreign key
                statement.execute("DELETE FROM " + dump.qualifiedName());
            }
        }
    }

    /**
     * Insert lại toàn bộ các dòng dữ liệu đã đọc từ file backup vào database
     * hiện tại. Nếu bảng chưa tồn tại, hàm sẽ tự tạo bảng dựa trên metadata
     * (tên cột, kiểu cột).
     */
    private void insertRows(Connection connection,
            List<TableDump> tables,
            Consumer<String> progressCallback) throws SQLException {
        for (TableDump dump : tables) {
            JsonArray rows = dump.rows();
            if (progressCallback != null) {
                progressCallback.accept("Đang khôi phục bảng " + dump.displayName() + " (" + rows.size() + " dòng)");
            }
            if (rows.isEmpty()) {
                continue;
            }

            // Tạo bảng nếu chưa tồn tại
            if (!tableExists(connection, dump)) {
                if (progressCallback != null) {
                    progressCallback.accept("Đang tạo bảng " + dump.displayName() + "...");
                }
                createTableIfNotExists(connection, dump);
            }

            boolean hasIdentity = hasIdentityColumn(connection, dump);
            String insertSql = buildInsertSql(dump);
            try (Statement identityStmt = hasIdentity ? connection.createStatement() : null; PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
                if (hasIdentity) {
                    identityStmt.execute("SET IDENTITY_INSERT " + dump.qualifiedName() + " ON");
                }
                int batch = 0;
                final int BATCH_SIZE = 1000; // Tăng batch size lên 1000 để nhanh hơn
                for (JsonElement element : rows) {
                    JsonArray row = element.getAsJsonArray();
                    for (int i = 0; i < dump.columns().size(); i++) {
                        JsonElement cell = row.get(i);
                        int sqlType = dump.columnTypes().get(i);
                        setStatementValue(preparedStatement, i + 1, cell, sqlType);
                    }
                    preparedStatement.addBatch();
                    batch++;
                    if (batch % BATCH_SIZE == 0) {
                        preparedStatement.executeBatch();
                        preparedStatement.clearBatch(); // Giải phóng memory
                    }
                }
                if (batch % BATCH_SIZE != 0) {
                    preparedStatement.executeBatch();
                }
                if (hasIdentity) {
                    identityStmt.execute("SET IDENTITY_INSERT " + dump.qualifiedName() + " OFF");
                }
            }
        }
    }

    /**
     * Tạm thời tắt toàn bộ foreign key constraints trong database để tránh lỗi
     * khi xóa/ghi dữ liệu theo thứ tự mới.
     */
    private void disableConstraints(Connection connection, List<TableDump> tables) throws SQLException {
        // Disable tất cả foreign key constraints trong database
        // để tránh lỗi khi xóa/insert dữ liệu
        // Sử dụng sp_msforeachtable để disable tất cả constraints của tất cả tables
        // Chỉ thực hiện nếu có ít nhất một bảng tồn tại
        try (Statement statement = connection.createStatement()) {
            // Kiểm tra xem có bảng nào trong database không
            try (ResultSet rs = statement.executeQuery(
                    "SELECT COUNT(*) as cnt FROM sys.tables WHERE is_ms_shipped = 0")) {
                if (rs.next() && rs.getInt("cnt") > 0) {
                    // Tắt tất cả foreign key constraints
                    statement.execute("EXEC sp_msforeachtable 'ALTER TABLE ? NOCHECK CONSTRAINT ALL'");
                }
            }
        }
    }

    /**
     * Bật lại toàn bộ foreign key constraints sau khi đã khôi phục dữ liệu
     * xong.
     */
    private void enableConstraints(Connection connection, List<TableDump> tables) throws SQLException {
        // Enable lại tất cả foreign key constraints
        // Sử dụng sp_msforeachtable để enable tất cả constraints của tất cả tables
        // Chỉ thực hiện nếu có ít nhất một bảng tồn tại
        try (Statement statement = connection.createStatement()) {
            // Kiểm tra xem có bảng nào trong database không
            try (ResultSet rs = statement.executeQuery(
                    "SELECT COUNT(*) as cnt FROM sys.tables WHERE is_ms_shipped = 0")) {
                if (rs.next() && rs.getInt("cnt") > 0) {
                    // Bật lại tất cả foreign key constraints
                    statement.execute("EXEC sp_msforeachtable 'ALTER TABLE ? CHECK CONSTRAINT ALL'");
                }
            }
        }
    }

    /**
     * Vô hiệu toàn bộ table-level triggers trước khi insert hàng loạt.
     * Sử dụng sp_msforeachtable để disable trigger trên tất cả bảng người dùng.
     */
    private void disableTriggers(Connection connection, List<TableDump> tables) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            try (ResultSet rs = statement.executeQuery(
                    "SELECT COUNT(*) as cnt FROM sys.tables WHERE is_ms_shipped = 0")) {
                if (rs.next() && rs.getInt("cnt") > 0) {
                    statement.execute("EXEC sp_msforeachtable 'DISABLE TRIGGER ALL ON ?'");
                }
            }
        }
    }

    /**
     * Bật lại toàn bộ table-level triggers sau khi insert xong.
     */
    private void enableTriggers(Connection connection, List<TableDump> tables) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            try (ResultSet rs = statement.executeQuery(
                    "SELECT COUNT(*) as cnt FROM sys.tables WHERE is_ms_shipped = 0")) {
                if (rs.next() && rs.getInt("cnt") > 0) {
                    statement.execute("EXEC sp_msforeachtable 'ENABLE TRIGGER ALL ON ?'");
                }
            }
        }
    }

    /**
     * Kiểm tra bảng (schema + tên bảng) có tồn tại trong database hiện tại hay
     * không.
     */
    private boolean tableExists(Connection connection, TableDump dump) throws SQLException {
        final String sql = """
                SELECT 1
                FROM sys.tables t
                JOIN sys.schemas s ON t.schema_id = s.schema_id
                WHERE s.name = ? AND t.name = ?
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, dump.schema());
            ps.setString(2, dump.name());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Tạo schema/bảng tương ứng với dữ liệu trong file backup nếu nó chưa tồn
     * tại trong database. Bảng được tạo dựa trên danh sách cột và kiểu dữ liệu
     * đã lưu trong JSON.
     */
    private void createTableIfNotExists(Connection connection, TableDump dump) throws SQLException {
        // Tạo schema nếu chưa tồn tại
        try (Statement stmt = connection.createStatement()) {
            String createSchemaSql = "IF NOT EXISTS (SELECT * FROM sys.schemas WHERE name = '"
                    + escapeIdentifier(dump.schema()) + "') EXEC('CREATE SCHEMA [" + escapeIdentifier(dump.schema()) + "]')";
            stmt.execute(createSchemaSql);
        }

        // Tạo bảng với các cột từ metadata
        StringBuilder createTableSql = new StringBuilder();
        createTableSql.append("CREATE TABLE ").append(dump.qualifiedName()).append(" (");

        for (int i = 0; i < dump.columns().size(); i++) {
            if (i > 0) {
                createTableSql.append(", ");
            }
            String columnName = dump.columns().get(i);
            int sqlType = dump.columnTypes().get(i);
            createTableSql.append('[').append(escapeIdentifier(columnName)).append("] ");
            createTableSql.append(getSqlTypeName(sqlType));
        }

        createTableSql.append(")");

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSql.toString());
        }
    }

    /**
     * Chuyển mã kiểu dữ liệu JDBC (java.sql.Types) sang tên kiểu dữ liệu tương
     * ứng trong SQL Server.
     */
    private String getSqlTypeName(int sqlType) {
        // Chuyển đổi SQL type code thành tên type trong SQL Server
        return switch (sqlType) {
            case Types.BIT ->
                "BIT";
            case Types.TINYINT ->
                "TINYINT";
            case Types.SMALLINT ->
                "SMALLINT";
            case Types.INTEGER ->
                "INT";
            case Types.BIGINT ->
                "BIGINT";
            case Types.REAL ->
                "REAL";
            case Types.FLOAT, Types.DOUBLE ->
                "FLOAT";
            case Types.DECIMAL, Types.NUMERIC ->
                "DECIMAL(18,2)";
            case Types.CHAR ->
                "CHAR(255)";
            case Types.VARCHAR ->
                "VARCHAR(MAX)";
            case Types.NCHAR ->
                "NCHAR(255)";
            case Types.NVARCHAR ->
                "NVARCHAR(MAX)";
            case Types.DATE ->
                "DATE";
            case Types.TIME ->
                "TIME";
            case Types.TIMESTAMP ->
                "DATETIME2";
            case Types.BINARY ->
                "BINARY(MAX)";
            case Types.VARBINARY ->
                "VARBINARY(MAX)";
            case Types.BLOB ->
                "VARBINARY(MAX)";
            case Types.CLOB ->
                "NVARCHAR(MAX)";
            case Types.NCLOB ->
                "NVARCHAR(MAX)";
            default ->
                "NVARCHAR(MAX)"; // Default fallback
        };
    }

    /**
     * Kiểm tra xem bảng có cột IDENTITY hay không để bật/tắt IDENTITY_INSERT
     * khi insert dữ liệu.
     */
    private boolean hasIdentityColumn(Connection connection, TableDump dump) throws SQLException {
        // Kiểm tra xem bảng có tồn tại không trước
        if (!tableExists(connection, dump)) {
            return false;
        }
        final String sql = """
                SELECT 1
                FROM sys.identity_columns ic
                JOIN sys.objects o ON ic.object_id = o.object_id
                JOIN sys.schemas s ON o.schema_id = s.schema_id
                WHERE s.name = ? AND o.name = ?
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, dump.schema());
            ps.setString(2, dump.name());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Sinh câu lệnh INSERT theo danh sách cột của bảng cần khôi phục. Ví dụ:
     * INSERT INTO [dbo].[NhanVien] ([id], [ten], ...) VALUES (?, ?, ...)
     */
    private String buildInsertSql(TableDump dump) {
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ")
                .append(dump.qualifiedName())
                .append(" (");
        for (int i = 0; i < dump.columns().size(); i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append('[').append(escapeIdentifier(dump.columns().get(i))).append(']');
        }
        builder.append(") VALUES (");
        for (int i = 0; i < dump.columns().size(); i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append('?');
        }
        builder.append(')');
        return builder.toString();
    }

    /**
     * Đọc metadata từ SQL Server để lấy danh sách tất cả các bảng người dùng
     * (bỏ qua schema hệ thống như sys, INFORMATION_SCHEMA) phục vụ cho việc
     * backup.
     */
    private List<TableInfo> loadTables(Connection connection) throws SQLException {
        List<TableInfo> tables = new ArrayList<>();
        DatabaseMetaData metaData = connection.getMetaData();
        try (ResultSet rs = metaData.getTables(connection.getCatalog(), null, "%", new String[]{"TABLE"})) {
            while (rs.next()) {
                String schema = rs.getString("TABLE_SCHEM");
                String name = rs.getString("TABLE_NAME");
                if (schema == null
                        || schema.equalsIgnoreCase("sys")
                        || schema.equalsIgnoreCase("INFORMATION_SCHEMA")
                        || name.startsWith("sys")) {
                    continue;
                }
                tables.add(new TableInfo(schema, name));
            }
        }
        tables.sort(Comparator.comparing(TableInfo::displayName));
        return tables;
    }

    /**
     * Đọc toàn bộ dữ liệu của một bảng và chuyển thành JSON (metadata + danh
     * sách dòng) để ghi vào file backup (.json trong file .zip).
     */
    private JsonObject exportTable(Connection connection, TableInfo tableInfo) throws SQLException {
        JsonObject payload = new JsonObject();
        payload.addProperty("schema", tableInfo.schema());
        payload.addProperty("name", tableInfo.name());
        payload.addProperty("qualifiedName", tableInfo.displayName());
        JsonArray columns = new JsonArray();
        JsonArray columnTypes = new JsonArray();
        JsonArray rows = new JsonArray();
        try (Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
            statement.setFetchSize(500);
            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableInfo.qualifiedName())) {
                ResultSetMetaData meta = resultSet.getMetaData();
                int columnCount = meta.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    columns.add(meta.getColumnName(i));
                    columnTypes.add(meta.getColumnType(i));
                }
                while (resultSet.next()) {
                    JsonArray row = new JsonArray();
                    for (int i = 1; i <= columnCount; i++) {
                        Object value = resultSet.getObject(i);
                        row.add(serializeValue(value));
                    }
                    rows.add(row);
                }
            }
        }
        payload.add("columns", columns);
        payload.add("columnTypes", columnTypes);
        payload.add("rows", rows);
        return payload;
    }

    /**
     * Chuẩn hóa giá trị đọc từ ResultSet sang JSON: - Kiểu thời gian → chuỗi
     * ISO - Nhị phân → base64 kèm _type = "binary" - Clob/Blob → đọc toàn bộ
     * nội dung.
     */
    private JsonElement serializeValue(Object value) throws SQLException {
        if (value == null) {
            return JsonNull.INSTANCE;
        }
        if (value instanceof byte[] bytes) {
            JsonObject binaryObject = new JsonObject();
            binaryObject.addProperty("_type", "binary");
            binaryObject.addProperty("data", Base64.getEncoder().encodeToString(bytes));
            return binaryObject;
        }
        if (value instanceof Timestamp ts) {
            return new JsonPrimitive(ts.toInstant().toString());
        }
        if (value instanceof java.sql.Date date) {
            return new JsonPrimitive(date.toString());
        }
        if (value instanceof java.sql.Time time) {
            return new JsonPrimitive(time.toString());
        }
        if (value instanceof Clob clob) {
            String data = clob.getSubString(1, (int) clob.length());
            clob.free();
            return new JsonPrimitive(data);
        }
        if (value instanceof Blob blob) {
            try (InputStream is = blob.getBinaryStream()) {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                is.transferTo(buffer);
                JsonObject binaryObject = new JsonObject();
                binaryObject.addProperty("_type", "binary");
                binaryObject.addProperty("data", Base64.getEncoder().encodeToString(buffer.toByteArray()));
                blob.free();
                return binaryObject;
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }
        return GSON.toJsonTree(value);
    }

    /**
     * Đọc file backup (.zip), duyệt từng entry JSON của bảng (bỏ qua
     * metadata.json) và build thành danh sách {@link TableDump} để phục vụ việc
     * khôi phục.
     */
    private List<TableDump> loadBackupPayloads(Path backupFile, Consumer<String> progressCallback) throws IOException {
        List<TableDump> dumps = new ArrayList<>();
        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(backupFile))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }
                if (entry.getName().equalsIgnoreCase(METADATA_ENTRY)) {
                    continue;
                }
                if (!entry.getName().endsWith(".json")) {
                    continue;
                }
                if (progressCallback != null) {
                    progressCallback.accept("Đang đọc " + entry.getName());
                }
                String content = readAll(zis);
                JsonObject object = GSON.fromJson(content, JsonObject.class);
                dumps.add(new TableDump(object));
            }
        }
        dumps.sort(Comparator.comparing(TableDump::displayName));
        return dumps;
    }

    /**
     * Đọc toàn bộ dữ liệu từ InputStream thành chuỗi UTF-8.
     */
    private static String readAll(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        inputStream.transferTo(buffer);
        return buffer.toString(StandardCharsets.UTF_8);
    }

    /**
     * Gán giá trị JSON tương ứng vào PreparedStatement theo đúng kiểu JDBC. Hàm
     * này là "cầu nối" giữa dữ liệu JSON trong file backup và câu lệnh INSERT
     * thực tế.
     */
    private void setStatementValue(PreparedStatement ps, int index, JsonElement value, int sqlType) throws SQLException {
        if (value == null || value.isJsonNull()) {
            ps.setNull(index, sqlType);
            return;
        }
        if (value.isJsonObject()) {
            JsonObject obj = value.getAsJsonObject();
            if (obj.has("_type") && "binary".equals(obj.get("_type").getAsString())) {
                byte[] data = Base64.getDecoder().decode(obj.get("data").getAsString());
                ps.setBytes(index, data);
                return;
            }
        }
        switch (sqlType) {
            case Types.INTEGER, Types.SMALLINT, Types.TINYINT ->
                ps.setInt(index, value.getAsInt());
            case Types.BIGINT ->
                ps.setLong(index, value.getAsLong());
            case Types.FLOAT, Types.REAL ->
                ps.setFloat(index, value.getAsFloat());
            case Types.DOUBLE ->
                ps.setDouble(index, value.getAsDouble());
            case Types.NUMERIC, Types.DECIMAL ->
                ps.setBigDecimal(index, value.getAsBigDecimal());
            case Types.BIT, Types.BOOLEAN ->
                ps.setBoolean(index, value.getAsBoolean());
            case Types.CHAR, Types.NCHAR, Types.VARCHAR, Types.NVARCHAR, Types.LONGVARCHAR, Types.LONGNVARCHAR ->
                ps.setString(index, value.getAsString());
            case Types.DATE ->
                ps.setDate(index, Date.valueOf(value.getAsString()));
            case Types.TIME ->
                ps.setTime(index, Time.valueOf(value.getAsString()));
            case Types.TIMESTAMP, Types.TIMESTAMP_WITH_TIMEZONE -> {
                Timestamp timestamp = Timestamp.from(Instant.parse(value.getAsString()));
                ps.setTimestamp(index, timestamp);
            }
            case Types.BINARY, Types.VARBINARY, Types.LONGVARBINARY -> {
                byte[] data = Base64.getDecoder().decode(value.getAsString());
                ps.setBytes(index, data);
            }
            default ->
                ps.setObject(index, value.getAsString());
        }
    }

    /**
     * Escape tên schema/bảng/cột để an toàn khi đưa vào câu lệnh SQL Server.
     */
    private static String escapeIdentifier(String identifier) {
        return identifier.replace("]", "]]");
    }

    /**
     * Sắp xếp lại danh sách bảng theo thứ tự phụ thuộc khóa ngoại (topological
     * sort) để đảm bảo insert bảng cha trước rồi mới insert bảng con.
     */
    private List<TableDump> sortTablesForInsert(Connection connection, List<TableDump> dumps) throws SQLException {
        Map<String, TableDump> tableMap = new LinkedHashMap<>();
        for (TableDump dump : dumps) {
            tableMap.put(tableKey(dump.schema(), dump.name()), dump);
        }
        Map<String, Set<String>> parentDependencies = new HashMap<>();
        Map<String, Set<String>> childDependencies = new HashMap<>();
        DatabaseMetaData metaData = connection.getMetaData();
        for (TableDump dump : dumps) {
            String childKey = tableKey(dump.schema(), dump.name());
            try (ResultSet rs = metaData.getImportedKeys(connection.getCatalog(), dump.schema(), dump.name())) {
                while (rs.next()) {
                    String pkSchema = rs.getString("PKTABLE_SCHEM");
                    String pkTable = rs.getString("PKTABLE_NAME");
                    if (pkSchema == null || pkTable == null) {
                        continue;
                    }
                    String parentKey = tableKey(pkSchema, pkTable);
                    if (!tableMap.containsKey(parentKey)) {
                        continue;
                    }
                    parentDependencies.computeIfAbsent(childKey, k -> new LinkedHashSet<>()).add(parentKey);
                    childDependencies.computeIfAbsent(parentKey, k -> new LinkedHashSet<>()).add(childKey);
                }
            }
        }

        Map<String, Integer> indegree = new HashMap<>();
        for (String key : tableMap.keySet()) {
            indegree.put(key, parentDependencies.getOrDefault(key, Collections.emptySet()).size());
        }

        Deque<String> ready = new ArrayDeque<>();
        for (Map.Entry<String, Integer> entry : indegree.entrySet()) {
            if (entry.getValue() == 0) {
                ready.add(entry.getKey());
            }
        }

        List<TableDump> ordered = new ArrayList<>(dumps.size());
        Set<String> orderedKeys = new LinkedHashSet<>();
        while (!ready.isEmpty()) {
            String key = ready.removeFirst();
            TableDump dump = tableMap.get(key);
            if (dump == null) {
                continue;
            }
            ordered.add(dump);
            orderedKeys.add(key);
            for (String childKey : childDependencies.getOrDefault(key, Collections.emptySet())) {
                int newDegree = indegree.computeIfPresent(childKey, (k, v) -> v - 1);
                if (newDegree == 0) {
                    ready.addLast(childKey);
                }
            }
        }

        if (ordered.size() != dumps.size()) {
            for (Map.Entry<String, TableDump> entry : tableMap.entrySet()) {
                if (orderedKeys.add(entry.getKey())) {
                    ordered.add(entry.getValue());
                }
            }
        }
        return ordered;
    }

    /**
     * Tạo key duy nhất cho một bảng theo dạng "schema.name" (viết thường) để
     * lưu map phụ thuộc.
     */
    private static String tableKey(String schema, String name) {
        return (schema + "." + name).toLowerCase(Locale.ROOT);
    }

    public record BackupResult(Path file, String databaseName, int tableCount, long rowCount) {

    }

    /**
     * Thông tin rút gọn về một bảng trong database dùng cho quá trình backup.
     */
    private record TableInfo(String schema, String name) {

        String qualifiedName() {
            return "[" + escapeIdentifier(schema) + "].[" + escapeIdentifier(name) + "]";
        }

        String displayName() {
            return schema + "." + name;
        }

        String fileName() {
            return schema + "." + name + ".json";
        }
    }

    /**
     * Đại diện cho dữ liệu đầy đủ của một bảng được đọc từ/ghi ra file JSON
     * trong gói backup. Bao gồm: schema, tên bảng, danh sách cột, kiểu cột và
     * toàn bộ các dòng dữ liệu.
     */
    private static class TableDump {

        private final String schema;
        private final String name;
        private final List<String> columns;
        private final List<Integer> columnTypes;
        private final JsonArray rows;

        TableDump(JsonObject object) {
            this.schema = object.get("schema").getAsString();
            this.name = object.get("name").getAsString();
            this.columns = toStringList(object.getAsJsonArray("columns"));
            this.columnTypes = toIntList(object.getAsJsonArray("columnTypes"));
            this.rows = object.getAsJsonArray("rows");
        }

        String schema() {
            return schema;
        }

        String name() {
            return name;
        }

        List<String> columns() {
            return columns;
        }

        List<Integer> columnTypes() {
            return columnTypes;
        }

        JsonArray rows() {
            return rows;
        }

        String qualifiedName() {
            return "[" + escapeIdentifier(schema) + "].[" + escapeIdentifier(name) + "]";
        }

        String displayName() {
            return schema + "." + name;
        }

        private static List<String> toStringList(JsonArray array) {
            List<String> list = new ArrayList<>(array.size());
            for (JsonElement element : array) {
                list.add(element.getAsString());
            }
            return list;
        }

        private static List<Integer> toIntList(JsonArray array) {
            List<Integer> list = new ArrayList<>(array.size());
            for (JsonElement element : array) {
                list.add(element.getAsInt());
            }
            return list;
        }
    }
}
