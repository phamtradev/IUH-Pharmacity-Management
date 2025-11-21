package vn.edu.iuh.fit.iuhpharmacitymanagement.util;

import java.util.prefs.Preferences;

// Lưu trạng thái bật/tắt auto backup của ứng dụng
public class BackupPreferences {
    private static final String PREF_NODE = "vn/edu/iuh/fit/iuhpharmacitymanagement/backup";
    private static final String KEY_AUTO_BACKUP_ENABLED = "autoBackupEnabled";
    
    private static final Preferences prefs = Preferences.userRoot().node(PREF_NODE);
    
    /**
     * Kiểm tra xem tự động sao lưu có được bật không
     */
    public static boolean isAutoBackupEnabled() {
        return prefs.getBoolean(KEY_AUTO_BACKUP_ENABLED, false);
    }
    
    /**
     * Bật hoặc tắt tự động sao lưu
     */
    public static void setAutoBackupEnabled(boolean enabled) {
        prefs.putBoolean(KEY_AUTO_BACKUP_ENABLED, enabled);
    }
}

