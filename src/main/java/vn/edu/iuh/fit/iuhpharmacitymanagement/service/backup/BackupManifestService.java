package vn.edu.iuh.fit.iuhpharmacitymanagement.service.backup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

// Ghi nhận lịch sử các file backup đã tạo vào file manifest
public class BackupManifestService {

    private static final String MANIFEST_FILE_NAME = "backup_manifest.json";
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    private final Type listType = new TypeToken<List<BackupRecord>>() {
    }.getType();
    private final Path manifestPath;

    public BackupManifestService(Path baseDirectory) {
        this.manifestPath = baseDirectory.resolve(MANIFEST_FILE_NAME);
    }

    public synchronized List<BackupRecord> readAll() {
        if (!Files.exists(manifestPath)) {
            return new ArrayList<>();
        }
        try (Reader reader = Files.newBufferedReader(manifestPath, StandardCharsets.UTF_8)) {
            List<BackupRecord> data = gson.fromJson(reader, listType);
            if (data == null) {
                return new ArrayList<>();
            }
            return new ArrayList<>(data);
        } catch (IOException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    public synchronized void append(BackupRecord record) {
        List<BackupRecord> records = readAll();
        records.removeIf(r -> r.absolutePath().equals(record.absolutePath()));
        records.add(0, record);
        write(records);
    }

    public synchronized void pruneMissingFiles() {
        List<BackupRecord> records = readAll();
        boolean changed = records.removeIf(record -> !Files.exists(Path.of(record.absolutePath())));
        if (changed) {
            write(records);
        }
    }

    public void ensureManifestDirectory() throws IOException {
        Files.createDirectories(manifestPath.getParent());
    }

    public String nowIso() {
        return Instant.now().toString();
    }

    private void write(List<BackupRecord> records) {
        try {
            ensureManifestDirectory();
            try (Writer writer = Files.newBufferedWriter(
                    manifestPath,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE)) {
                gson.toJson(records, listType, writer);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

