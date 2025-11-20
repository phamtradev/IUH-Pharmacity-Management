/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.form;

import com.formdev.flatlaf.FlatLightLaf;
import vn.edu.iuh.fit.iuhpharmacitymanagement.service.backup.BackupManifestService;
import vn.edu.iuh.fit.iuhpharmacitymanagement.service.backup.BackupRecord;
import vn.edu.iuh.fit.iuhpharmacitymanagement.service.backup.DataBackupService;
import vn.edu.iuh.fit.iuhpharmacitymanagement.util.BackupPreferences;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main application window that contains the menu and main content
 * @author PhamTra
 */
public class MenuForm extends javax.swing.JFrame {
    private static final Color WHITE = Color.WHITE;
    private static final Color BLACK = Color.BLACK;
    private static final Color GRAY = Color.GRAY;
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;

    public static MainForm mainForm;

    public MenuForm() {
        initComponents();
        setupWindow();
        setupMainForm();
    }
    
     public MenuForm(int type) {
        initComponents();
        setupWindow();
        setupMainForm(type);
    }
     
    private void setupWindow() {
        // Configure title bar
        getRootPane().putClientProperty("JRootPane.titleBarBackground", WHITE);
        getRootPane().putClientProperty("JRootPane.titleBarForeground", BLACK);

        // Set white background for entire window
        getContentPane().setBackground(WHITE);
        setBackground(WHITE);

        // Remove borders that might cause gray colors
        getRootPane().setBorder(null);

        // Set window title
        setTitle("IUH Pharmacity Management");

        // Thiết lập full màn hình
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        
        // Thêm window listener để tự động sao lưu khi tắt app
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (BackupPreferences.isAutoBackupEnabled()) {
                    // Cho phép đóng app ngay, backup chạy background
                    performAutoBackupInBackground();
                } else {
                    // Không có auto backup, đóng app ngay
                    System.exit(0);
                }
            }
        });
    }
    
    private void performAutoBackupInBackground() {
        // Hiển thị thông báo ngắn gọn rồi đóng app
        javax.swing.JOptionPane.showMessageDialog(
            this,
            "Ứng dụng sẽ đóng ngay. Sao lưu tự động đang chạy trong background.",
            "Thông báo",
            javax.swing.JOptionPane.INFORMATION_MESSAGE
        );
        
        // Đóng cửa sổ ngay, không chờ backup
        dispose();
        
        // Tạo thread backup chạy background
        Thread backupThread = new Thread(() -> {
            try {
                System.out.println("Bắt đầu sao lưu tự động trong background...");
                
                DataBackupService backupService = new DataBackupService();
                Path backupDir = backupService.getDefaultBackupDirectory();
                
                // Đảm bảo thư mục tồn tại
                if (!Files.exists(backupDir)) {
                    Files.createDirectories(backupDir);
                }
                
                // Thực hiện backup (không có progress callback vì app đã đóng)
                DataBackupService.BackupResult result = backupService.backup(backupDir, null);
                
                // Lưu vào manifest
                BackupManifestService manifestService = new BackupManifestService(backupDir);
                BackupRecord record = new BackupRecord(
                    result.file().getFileName().toString(),
                    result.file().toAbsolutePath().toString(),
                    Files.size(result.file()),
                    manifestService.nowIso(),
                    result.databaseName()
                );
                manifestService.append(record);
                
                System.out.println("Sao lưu tự động hoàn thành: " + result.file());
            } catch (Exception ex) {
                System.err.println("Lỗi khi tự động sao lưu: " + ex.getMessage());
                ex.printStackTrace();
            } finally {
                // Đóng app sau khi backup xong (hoặc lỗi)
                System.exit(0);
            }
        });
        
        // Thread không phải daemon để đảm bảo JVM đợi nó hoàn thành trước khi tắt
        backupThread.setDaemon(false);
        backupThread.setName("AutoBackupThread");
        backupThread.start();
    }

    private void setupMainForm() {
        mainForm = new MainForm();
        setContentPane(mainForm);
    }
      private void setupMainForm(int type) {
        mainForm = new MainForm(type);
        setContentPane(mainForm);
    }
    /**
     * Entry point chính của ứng dụng - tích hợp từ MainApplication
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                setupLookAndFeel();
                new MenuForm().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void setupLookAndFeel() {
        // Configure FlatLaf with white title bar
        System.setProperty("flatlaf.useWindowDecorations", "true");
        FlatLightLaf.setup();

        // Configure global title bar colors
        UIManager.put("TitlePane.background", WHITE);
        UIManager.put("TitlePane.foreground", BLACK);
        UIManager.put("TitlePane.inactiveBackground", WHITE);
        UIManager.put("TitlePane.inactiveForeground", GRAY);

        // Remove all shadow effects and gray colors
        UIManager.put("Component.focusWidth", 0);
        UIManager.put("Component.innerFocusWidth", 0);
        UIManager.put("Component.borderWidth", 0);
        UIManager.put("PopupMenu.borderWidth", 0);
        UIManager.put("ScrollPane.borderWidth", 0);
        UIManager.put("Panel.borderWidth", 0);

        // Remove default backgrounds that might cause gray colors
        UIManager.put("Panel.background", WHITE);
        UIManager.put("ScrollPane.background", WHITE);
        UIManager.put("Viewport.background", WHITE);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        // Configure title bar before creating layout
        getRootPane().putClientProperty("JRootPane.titleBarBackground", WHITE);
        getRootPane().putClientProperty("JRootPane.titleBarForeground", BLACK);

        // Set white background for frame and layout
        setBackground(WHITE);
        getContentPane().setBackground(WHITE);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
