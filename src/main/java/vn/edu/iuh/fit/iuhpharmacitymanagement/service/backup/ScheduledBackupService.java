package vn.edu.iuh.fit.iuhpharmacitymanagement.service.backup;

import vn.edu.iuh.fit.iuhpharmacitymanagement.util.BackupPreferences;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Quản lý lịch backup tự động: chạy backup vào 23h mỗi ngày
 * và tự động xóa các bản backup cũ, chỉ giữ lại 15 bản gần nhất.
 */
public class ScheduledBackupService {
    private static final int MAX_BACKUPS_TO_KEEP = 15;
    private static final LocalTime BACKUP_TIME = LocalTime.of(23, 0); // 23:00
    
    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> scheduledTask;
    private final DataBackupService backupService;
    private final BackupManifestService manifestService;
    
    private static ScheduledBackupService instance;
    
    private ScheduledBackupService() {
        this.backupService = new DataBackupService();
        Path backupDir = backupService.getDefaultBackupDirectory();
        this.manifestService = new BackupManifestService(backupDir);
    }
    
    public static synchronized ScheduledBackupService getInstance() {
        if (instance == null) {
            instance = new ScheduledBackupService();
        }
        return instance;
    }
    
    /**
     * Khởi động scheduler nếu được bật trong preferences
     */
    public void startIfEnabled() {
        if (BackupPreferences.isScheduledBackupEnabled()) {
            start();
        }
    }
    
    /**
     * Khởi động scheduler để chạy backup vào 23h mỗi ngày
     */
    public void start() {
        if (scheduler != null && !scheduler.isShutdown()) {
            stop(); // Dừng scheduler cũ nếu có
        }
        
        scheduler = Executors.newScheduledThreadPool(1);
        
        // Tính thời gian đến lần backup tiếp theo (23h hôm nay hoặc 23h ngày mai)
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextBackup = now.toLocalDate().atTime(BACKUP_TIME);
        if (nextBackup.isBefore(now) || nextBackup.isEqual(now)) {
            nextBackup = nextBackup.plusDays(1);
        }
        
        long initialDelay = java.time.Duration.between(now, nextBackup).toMinutes();
        long periodInMinutes = 24 * 60; // 24 giờ = 1440 phút
        
        scheduledTask = scheduler.scheduleAtFixedRate(
            this::performScheduledBackup,
            initialDelay,
            periodInMinutes,
            TimeUnit.MINUTES
        );
        
        System.out.println("Scheduled backup đã được khởi động. Lần backup tiếp theo: " + nextBackup);
    }
    
    /**
     * Dừng scheduler
     */
    public void stop() {
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
            scheduledTask = null;
        }
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
    
    /**
     * Thực hiện backup theo lịch và tự động xóa backup cũ
     */
    private void performScheduledBackup() {
        try {
            System.out.println("Bắt đầu backup theo lịch...");
            
            Path backupDir = backupService.getDefaultBackupDirectory();
            if (!Files.exists(backupDir)) {
                Files.createDirectories(backupDir);
            }
            
            // Thực hiện backup
            DataBackupService.BackupResult result = backupService.backup(backupDir, null);
            
            // Lưu vào manifest
            BackupRecord record = new BackupRecord(
                result.file().getFileName().toString(),
                result.file().toAbsolutePath().toString(),
                Files.size(result.file()),
                manifestService.nowIso(),
                result.databaseName()
            );
            manifestService.append(record);
            
            System.out.println("Backup theo lịch hoàn thành: " + result.file());
            
            // Tự động xóa backup cũ, chỉ giữ 15 bản gần nhất
            cleanupOldBackups();
            
        } catch (Exception ex) {
            System.err.println("Lỗi khi thực hiện backup theo lịch: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    /**
     * Xóa các bản backup cũ, chỉ giữ lại MAX_BACKUPS_TO_KEEP bản gần nhất
     */
    public void cleanupOldBackups() {
        try {
            List<BackupRecord> records = manifestService.readAll();
            
            if (records.size() <= MAX_BACKUPS_TO_KEEP) {
                return; // Không cần xóa gì
            }
            
            // Sắp xếp theo thời gian tạo (mới nhất trước)
            records.sort((r1, r2) -> {
                try {
                    return java.time.Instant.parse(r2.createdAtIso())
                        .compareTo(java.time.Instant.parse(r1.createdAtIso()));
                } catch (Exception e) {
                    return 0;
                }
            });
            
            // Xóa các bản cũ hơn
            List<BackupRecord> toKeep = records.subList(0, MAX_BACKUPS_TO_KEEP);
            List<BackupRecord> toDelete = records.subList(MAX_BACKUPS_TO_KEEP, records.size());
            
            for (BackupRecord record : toDelete) {
                try {
                    Path filePath = Path.of(record.absolutePath());
                    if (Files.exists(filePath)) {
                        Files.delete(filePath);
                        System.out.println("Đã xóa backup cũ: " + record.fileName());
                    }
                } catch (Exception ex) {
                    System.err.println("Không thể xóa file: " + record.absolutePath() + " - " + ex.getMessage());
                }
            }
            
            // Cập nhật manifest chỉ giữ lại các bản cần giữ
            manifestService.replaceAll(toKeep);
            
            System.out.println("Đã xóa " + toDelete.size() + " bản backup cũ. Giữ lại " + toKeep.size() + " bản gần nhất.");
            
        } catch (Exception ex) {
            System.err.println("Lỗi khi xóa backup cũ: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    /**
     * Kiểm tra xem scheduler có đang chạy không
     */
    public boolean isRunning() {
        return scheduler != null && !scheduler.isShutdown() && scheduledTask != null && !scheduledTask.isCancelled();
    }
}

