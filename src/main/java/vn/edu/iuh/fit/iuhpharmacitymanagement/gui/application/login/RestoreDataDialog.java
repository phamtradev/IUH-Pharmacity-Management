package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.login;

import raven.toast.Notifications;
import vn.edu.iuh.fit.iuhpharmacitymanagement.service.backup.BackupManifestService;
import vn.edu.iuh.fit.iuhpharmacitymanagement.service.backup.BackupRecord;
import vn.edu.iuh.fit.iuhpharmacitymanagement.service.backup.DataBackupService;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Dialog để khôi phục dữ liệu từ backup bằng cách nhập key (tên file)
 */
public class RestoreDataDialog extends JDialog {
    // Hash SHA-256 của key "20092004"
    private static final String ADMIN_KEY_HASH = "8124684675a378155feb7b4b06cacf2ba09e49def0536b757c50c12d63c7224a";
    
    private JPasswordField txtKey;
    private JButton btnTimKiem;
    private JButton btnKhoiPhuc;
    private JButton btnHuy;
    private JTextArea txtThongTin;
    private JLabel lblTrangThai;
    private Path selectedBackupFile;
    private DataBackupService backupService;
    private BackupManifestService manifestService;

    public RestoreDataDialog(JFrame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        backupService = new DataBackupService();
        Path backupDir = backupService.getDefaultBackupDirectory();
        manifestService = new BackupManifestService(backupDir);
    }

    private void initComponents() {
        setTitle("Khôi phục dữ liệu");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout(10, 10));

        // Panel chính
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Panel nhập key
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Label và text field cho key
        JLabel lblKey = new JLabel("Nhập key:");
        lblKey.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        inputPanel.add(lblKey, gbc);

        txtKey = new JPasswordField(30);
        txtKey.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtKey.setEchoChar('•'); // Ẩn ký tự khi nhập giống mật khẩu
        txtKey.addActionListener(e -> btnTimKiem.doClick());
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        inputPanel.add(txtKey, gbc);

        btnTimKiem = new JButton("Khôi phục");
        btnTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnTimKiem.addActionListener(e -> timKiemVaKhoiPhuc());
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        inputPanel.add(btnTimKiem, gbc);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // Panel thông tin
        JPanel infoPanel = new JPanel(new BorderLayout(5, 5));
        JLabel lblThongTin = new JLabel("Thông tin backup:");
        lblThongTin.setFont(new Font("Segoe UI", Font.BOLD, 12));
        infoPanel.add(lblThongTin, BorderLayout.NORTH);

        txtThongTin = new JTextArea(6, 30);
        txtThongTin.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtThongTin.setEditable(false);
        txtThongTin.setBackground(getBackground());
        txtThongTin.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        JScrollPane scrollPane = new JScrollPane(txtThongTin);
        scrollPane.setBorder(null);
        infoPanel.add(scrollPane, BorderLayout.CENTER);

        lblTrangThai = new JLabel(" ");
        lblTrangThai.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblTrangThai.setForeground(Color.GRAY);
        infoPanel.add(lblTrangThai, BorderLayout.SOUTH);

        mainPanel.add(infoPanel, BorderLayout.CENTER);

        // Panel nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnKhoiPhuc = new JButton("Khôi phục");
        btnKhoiPhuc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnKhoiPhuc.setEnabled(false);
        btnKhoiPhuc.addActionListener(e -> thucHienKhoiPhuc());
        buttonPanel.add(btnKhoiPhuc);

        btnHuy = new JButton("Hủy");
        btnHuy.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnHuy.addActionListener(e -> dispose());
        buttonPanel.add(btnHuy);
        
        // Ẩn nút khôi phục riêng vì đã tích hợp vào btnTimKiem
        btnKhoiPhuc.setVisible(false);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(getParent());
    }

    private void timKiemVaKhoiPhuc() {
        String key = new String(txtKey.getPassword()).trim();
        if (key.isEmpty()) {
            Notifications.getInstance().setJFrame((JFrame) SwingUtilities.getWindowAncestor(this));
            Notifications.getInstance().show(Notifications.Type.ERROR, 
                Notifications.Location.TOP_CENTER,
                "Vui lòng nhập key");
            return;
        }

        // Kiểm tra key bằng cách so sánh hash
        String keyHash = hashKey(key);
        if (!keyHash.equals(ADMIN_KEY_HASH)) {
            Notifications.getInstance().setJFrame((JFrame) SwingUtilities.getWindowAncestor(this));
            Notifications.getInstance().show(Notifications.Type.ERROR, 
                Notifications.Location.TOP_CENTER,
                "Key không hợp lệ");
            txtKey.setText(""); // Xóa key đã nhập
            return;
        }

        lblTrangThai.setText("Đang tìm bản backup mới nhất...");
        btnTimKiem.setEnabled(false);
        selectedBackupFile = null;
        txtThongTin.setText("");

        // Tìm bản backup mới nhất
        SwingWorker<Path, Void> worker = new SwingWorker<Path, Void>() {
            @Override
            protected Path doInBackground() throws Exception {
                // Tìm trong manifest trước (sắp xếp theo thời gian)
                List<BackupRecord> records = manifestService.readAll();
                if (!records.isEmpty()) {
                    // Sắp xếp theo createdAtIso (mới nhất trước)
                    records.sort((r1, r2) -> {
                        try {
                            Instant time1 = Instant.parse(r1.createdAtIso());
                            Instant time2 = Instant.parse(r2.createdAtIso());
                            return time2.compareTo(time1); // Mới nhất trước
                        } catch (Exception e) {
                            return 0;
                        }
                    });
                    
                    // Lấy bản mới nhất còn tồn tại
                    for (BackupRecord record : records) {
                        Path filePath = Path.of(record.absolutePath());
                        if (Files.exists(filePath)) {
                            return filePath;
                        }
                    }
                }
                
                // Nếu không tìm thấy trong manifest, tìm trực tiếp trong thư mục backup
                Path backupDir = backupService.getDefaultBackupDirectory();
                if (Files.exists(backupDir)) {
                    try {
                        List<Path> backupFiles = Files.list(backupDir)
                            .filter(p -> {
                                String fileName = p.getFileName().toString();
                                return fileName.endsWith(".zip") && 
                                       !fileName.equals("backup_manifest.json");
                            })
                            .collect(Collectors.toList());
                        
                        if (!backupFiles.isEmpty()) {
                            // Sắp xếp theo thời gian sửa đổi (mới nhất trước)
                            backupFiles.sort((p1, p2) -> {
                                try {
                                    FileTime time1 = Files.getLastModifiedTime(p1);
                                    FileTime time2 = Files.getLastModifiedTime(p2);
                                    return time2.compareTo(time1);
                                } catch (IOException e) {
                                    return 0;
                                }
                            });
                            
                            return backupFiles.get(0);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                
                return null;
            }

            @Override
            protected void done() {
                btnTimKiem.setEnabled(true);
                try {
                    Path found = get();
                    if (found != null && Files.exists(found)) {
                        selectedBackupFile = found;
                        hienThiThongTin(found);
                        lblTrangThai.setText("Đã tìm thấy bản backup mới nhất");
                        // Tự động thực hiện khôi phục
                        thucHienKhoiPhuc();
                    } else {
                        lblTrangThai.setText("Không tìm thấy bản backup nào");
                        JOptionPane.showMessageDialog(RestoreDataDialog.this,
                            "Không tìm thấy bản backup nào trong hệ thống",
                            "Thông báo",
                            JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception e) {
                    lblTrangThai.setText("Lỗi khi tìm kiếm: " + e.getMessage());
                    JOptionPane.showMessageDialog(RestoreDataDialog.this,
                        "Lỗi khi tìm kiếm: " + e.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    /**
     * Mã hóa key bằng SHA-256
     */
    private String hashKey(String key) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(key.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Fallback: nếu không có SHA-256, trả về key gốc (không nên xảy ra)
            return key;
        }
    }

    private void hienThiThongTin(Path file) {
        try {
            StringBuilder info = new StringBuilder();
            info.append("File: ").append(file.getFileName().toString()).append("\n");
            info.append("Đường dẫn: ").append(file.toAbsolutePath().toString()).append("\n");
            
            if (Files.exists(file)) {
                long sizeBytes = Files.size(file);
                String sizeStr;
                if (sizeBytes < 1024) {
                    sizeStr = sizeBytes + " bytes";
                } else if (sizeBytes < 1024 * 1024) {
                    sizeStr = String.format("%.2f KB", sizeBytes / 1024.0);
                } else {
                    sizeStr = String.format("%.2f MB", sizeBytes / (1024.0 * 1024.0));
                }
                info.append("Kích thước: ").append(sizeStr).append("\n");
            }
            
            txtThongTin.setText(info.toString());
        } catch (IOException e) {
            txtThongTin.setText("Lỗi khi đọc thông tin file: " + e.getMessage());
        }
    }

    private void thucHienKhoiPhuc() {
        if (selectedBackupFile == null || !Files.exists(selectedBackupFile)) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn file backup hợp lệ",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
            "Khôi phục sẽ ghi đè toàn bộ dữ liệu hiện tại.\n" +
            "Bạn có chắc chắn muốn tiếp tục?",
            "Xác nhận khôi phục",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        // Tạo progress dialog với UI tốt hơn
        JDialog progressDialog = new JDialog(this, "Đang khôi phục dữ liệu", true);
        progressDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        progressDialog.setLayout(new BorderLayout(10, 10));
        progressDialog.setResizable(false);
        
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel progressLabel = new JLabel("Đang khôi phục dữ liệu, vui lòng đợi...");
        progressLabel.setHorizontalAlignment(SwingConstants.CENTER);
        progressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        contentPanel.add(progressLabel, BorderLayout.CENTER);
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(false);
        contentPanel.add(progressBar, BorderLayout.SOUTH);
        
        progressDialog.add(contentPanel, BorderLayout.CENTER);
        
        progressDialog.pack();
        progressDialog.setLocationRelativeTo(this);
        
        // Đảm bảo dialog được hiển thị trước khi worker chạy
        SwingUtilities.invokeLater(() -> {
            progressDialog.setVisible(true);
        });

        // Thực hiện restore trong thread riêng
        SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    backupService.restore(selectedBackupFile, message -> {
                        publish(message);
                    });
                } catch (Exception e) {
                    publish("Lỗi: " + e.getMessage());
                    throw e;
                }
                return null;
            }

            @Override
            protected void process(java.util.List<String> chunks) {
                if (!chunks.isEmpty()) {
                    String latestMessage = chunks.get(chunks.size() - 1);
                    progressLabel.setText(latestMessage);
                    // Force repaint để đảm bảo UI được cập nhật
                    progressLabel.repaint();
                }
            }

            @Override
            protected void done() {
                // Đảm bảo dialog được đóng đúng cách
                SwingUtilities.invokeLater(() -> {
                    progressDialog.dispose();
                });
                
                try {
                    get(); // Kiểm tra exception nếu có
                    JOptionPane.showMessageDialog(RestoreDataDialog.this,
                        "Khôi phục hoàn tất!\n" +
                        "Vui lòng khởi động lại ứng dụng để áp dụng dữ liệu.",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } catch (Exception ex) {
                    String errorMsg = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
                    JOptionPane.showMessageDialog(RestoreDataDialog.this,
                        "Khôi phục thất bại: " + errorMsg,
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        };
        worker.execute();
    }
}

