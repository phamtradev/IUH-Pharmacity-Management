package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.login;

import raven.toast.Notifications;
import vn.edu.iuh.fit.iuhpharmacitymanagement.service.backup.BackupManifestService;
import vn.edu.iuh.fit.iuhpharmacitymanagement.service.backup.BackupRecord;
import vn.edu.iuh.fit.iuhpharmacitymanagement.service.backup.DataBackupService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Dialog ẩn cho admin nhập key để khôi phục dữ liệu nhanh từ bản backup mới nhất
public class RestoreDataDialog extends JDialog {
    // Hash SHA-256 của key "20092004"
    private static final String ADMIN_KEY_HASH = "8124684675a378155feb7b4b06cacf2ba09e49def0536b757c50c12d63c7224a";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    private JPasswordField txtKey;
    private JButton btnTimKiem;
    private JButton btnKhoiPhuc;
    private JButton btnHuy;
    private JTable tblBackup;
    private DefaultTableModel tableModel;
    private JLabel lblTrangThai;
    private Path selectedBackupFile;
    private DataBackupService backupService;
    private BackupManifestService manifestService;
    private List<BackupRecord> backupRecords;

    public RestoreDataDialog(JFrame parent, boolean modal) {
        super(parent, modal);
        backupRecords = new ArrayList<>();
        initComponents();
        backupService = new DataBackupService();
        Path backupDir = backupService.getDefaultBackupDirectory();
        manifestService = new BackupManifestService(backupDir);
    }

    private void initComponents() {
        setTitle("Khôi phục dữ liệu");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(true);
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

        btnTimKiem = new JButton("Tìm kiếm");
        btnTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnTimKiem.addActionListener(e -> timKiemBackup());
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        inputPanel.add(btnTimKiem, gbc);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // Panel danh sách backup
        JPanel listPanel = new JPanel(new BorderLayout(5, 5));
        JLabel lblDanhSach = new JLabel("Danh sách backup:");
        lblDanhSach.setFont(new Font("Segoe UI", Font.BOLD, 12));
        listPanel.add(lblDanhSach, BorderLayout.NORTH);

        // Tạo table model
        String[] columnNames = {"Tên file", "Ngày tạo", "Kích thước", "Đường dẫn"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblBackup = new JTable(tableModel);
        tblBackup.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tblBackup.setRowHeight(25);
        tblBackup.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblBackup.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        tblBackup.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = tblBackup.getSelectedRow();
            if (selectedRow >= 0 && selectedRow < backupRecords.size()) {
                BackupRecord record = backupRecords.get(selectedRow);
                Path filePath = Path.of(record.absolutePath());
                if (Files.exists(filePath)) {
                    selectedBackupFile = filePath;
                    btnKhoiPhuc.setEnabled(true);
                } else {
                    selectedBackupFile = null;
                    btnKhoiPhuc.setEnabled(false);
                }
            } else {
                selectedBackupFile = null;
                btnKhoiPhuc.setEnabled(false);
            }
        });
        
        // Điều chỉnh độ rộng cột
        tblBackup.getColumnModel().getColumn(0).setPreferredWidth(200);
        tblBackup.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblBackup.getColumnModel().getColumn(2).setPreferredWidth(100);
        tblBackup.getColumnModel().getColumn(3).setPreferredWidth(300);
        
        JScrollPane scrollPane = new JScrollPane(tblBackup);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        scrollPane.setPreferredSize(new Dimension(750, 300));
        listPanel.add(scrollPane, BorderLayout.CENTER);

        lblTrangThai = new JLabel(" ");
        lblTrangThai.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblTrangThai.setForeground(Color.GRAY);
        listPanel.add(lblTrangThai, BorderLayout.SOUTH);

        mainPanel.add(listPanel, BorderLayout.CENTER);

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

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(getParent());
    }

    private void timKiemBackup() {
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

        lblTrangThai.setText("Đang tải danh sách backup...");
        btnTimKiem.setEnabled(false);
        selectedBackupFile = null;
        tableModel.setRowCount(0);
        backupRecords = new ArrayList<>();

        // Tìm tất cả các bản backup
        SwingWorker<List<BackupRecord>, Void> worker = new SwingWorker<List<BackupRecord>, Void>() {
            @Override
            protected List<BackupRecord> doInBackground() throws Exception {
                List<BackupRecord> allRecords = new ArrayList<>();
                
                // Tìm trong manifest trước
                List<BackupRecord> manifestRecords = manifestService.readAll();
                for (BackupRecord record : manifestRecords) {
                    Path filePath = Path.of(record.absolutePath());
                    if (Files.exists(filePath)) {
                        allRecords.add(record);
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
                        
                        for (Path filePath : backupFiles) {
                            // Kiểm tra xem đã có trong manifest chưa
                            boolean existsInManifest = allRecords.stream()
                                .anyMatch(r -> r.absolutePath().equals(filePath.toAbsolutePath().toString()));
                            
                            if (!existsInManifest) {
                                // Tạo record mới từ file
                                try {
                                    FileTime lastModified = Files.getLastModifiedTime(filePath);
                                    long size = Files.size(filePath);
                                    String createdAtIso = Instant.ofEpochMilli(lastModified.toMillis()).toString();
                                    
                                    BackupRecord record = new BackupRecord(
                                        filePath.getFileName().toString(),
                                        filePath.toAbsolutePath().toString(),
                                        size,
                                        createdAtIso,
                                        "IUH_Pharmacity_Management"
                                    );
                                    allRecords.add(record);
                                } catch (IOException e) {
                                    // Bỏ qua file lỗi
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                
                // Sắp xếp theo thời gian (mới nhất trước)
                allRecords.sort((r1, r2) -> {
                    try {
                        Instant time1 = Instant.parse(r1.createdAtIso());
                        Instant time2 = Instant.parse(r2.createdAtIso());
                        return time2.compareTo(time1); // Mới nhất trước
                    } catch (Exception e) {
                        // Nếu không parse được, so sánh theo tên file
                        return r2.fileName().compareTo(r1.fileName());
                    }
                });
                
                return allRecords;
            }

            @Override
            protected void done() {
                btnTimKiem.setEnabled(true);
                try {
                    List<BackupRecord> records = get();
                    if (records != null && !records.isEmpty()) {
                        backupRecords = records;
                        hienThiDanhSachBackup(records);
                        lblTrangThai.setText("Đã tìm thấy " + records.size() + " bản backup");
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
                    e.printStackTrace();
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

    private void hienThiDanhSachBackup(List<BackupRecord> records) {
        tableModel.setRowCount(0);
        for (BackupRecord record : records) {
            try {
                // Format ngày tạo
                String ngayTao = "";
                try {
                    Instant instant = Instant.parse(record.createdAtIso());
                    LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                    ngayTao = dateTime.format(DATE_FORMATTER);
                } catch (Exception e) {
                    ngayTao = record.createdAtIso();
                }
                
                // Format kích thước
                String kichThuoc = formatSize(record.sizeInBytes());
                
                // Thêm vào table
                tableModel.addRow(new Object[]{
                    record.fileName(),
                    ngayTao,
                    kichThuoc,
                    record.absolutePath()
                });
            } catch (Exception e) {
                // Bỏ qua record lỗi
                e.printStackTrace();
            }
        }
    }
    
    private String formatSize(long sizeBytes) {
        if (sizeBytes < 1024) {
            return sizeBytes + " bytes";
        } else if (sizeBytes < 1024 * 1024) {
            return String.format("%.2f KB", sizeBytes / 1024.0);
        } else {
            return String.format("%.2f MB", sizeBytes / (1024.0 * 1024.0));
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

