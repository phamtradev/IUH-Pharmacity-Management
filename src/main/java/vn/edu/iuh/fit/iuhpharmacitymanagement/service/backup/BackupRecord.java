package vn.edu.iuh.fit.iuhpharmacitymanagement.service.backup;

// Mô tả metadata của một file backup trong lịch sử
public record BackupRecord(
        String fileName,
        String absolutePath,
        long sizeInBytes,
        String createdAtIso,
        String databaseName) {
}

