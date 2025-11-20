package vn.edu.iuh.fit.iuhpharmacitymanagement.service.backup;

/**
 * Thông tin một bản sao lưu đã được tạo.
 */
public record BackupRecord(
        String fileName,
        String absolutePath,
        long sizeInBytes,
        String createdAtIso,
        String databaseName) {
}

