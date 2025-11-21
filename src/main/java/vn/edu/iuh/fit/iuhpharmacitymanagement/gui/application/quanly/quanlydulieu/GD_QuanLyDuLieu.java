package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlydulieu;

import com.formdev.flatlaf.FlatClientProperties;
import raven.toast.Notifications;
import vn.edu.iuh.fit.iuhpharmacitymanagement.service.backup.BackupManifestService;
import vn.edu.iuh.fit.iuhpharmacitymanagement.service.backup.BackupRecord;
import vn.edu.iuh.fit.iuhpharmacitymanagement.service.backup.DataBackupService;
import vn.edu.iuh.fit.iuhpharmacitymanagement.util.BackupPreferences;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

// Màn hình cho quản lý thực hiện backup/restore dữ liệu và xem lịch sử
public class GD_QuanLyDuLieu extends JPanel {

    private final DataBackupService backupService = new DataBackupService();
    private final BackupManifestService manifestService;
    private Path currentDirectory;

    private JTextField txtFolder;
    private JTable tblHistory;
    private DefaultTableModel historyModel;
    private JButton btnBackupNow;
    private JButton btnRestoreSelected;
    private JButton btnRestoreFromFile;
    private JButton btnDeleteSelected;
    private JButton btnChooseFolder;
    private JButton btnOpenFolder;
    private JLabel lblStatus;
    private JProgressBar progressBar;
    private JCheckBox chkAutoBackup;

    private static final DateTimeFormatter DISPLAY_TIME_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(ZoneId.systemDefault());

    public GD_QuanLyDuLieu() {
        this.currentDirectory = backupService.getDefaultBackupDirectory();
        this.manifestService = new BackupManifestService(currentDirectory);
        initComponents();
        reloadHistory();
    }

    private void initComponents() {
        setLayout(new BorderLayout(16, 16));
        setBorder(new EmptyBorder(24, 24, 24, 24));
        setBackground(Color.WHITE);
        putClientProperty(FlatClientProperties.STYLE, "background:#FFFFFF");

        add(buildHeader(), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);
    }

    private Component buildHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        JLabel title = new JLabel("Quản lý dữ liệu");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        JLabel subtitle = new JLabel("Sao lưu và khôi phục toàn bộ dữ liệu hệ thống");
        subtitle.setFont(subtitle.getFont().deriveFont(Font.PLAIN, 13f));
        panel.add(title, BorderLayout.NORTH);
        panel.add(subtitle, BorderLayout.SOUTH);
        return panel;
    }

    private Component buildContent() {
        JPanel container = new JPanel();
        container.setOpaque(false);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.add(buildBackupCard());
        container.add(Box.createVerticalStrut(16));
        container.add(buildHistoryCard());
        return container;
    }

    private Component buildBackupCard() {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout(12, 12));

        JLabel title = new JLabel("Sao lưu thủ công");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        card.add(title, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        center.add(new JLabel("Thư mục lưu trữ:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtFolder = new JTextField(currentDirectory.toAbsolutePath().toString());
        txtFolder.setEditable(false);
        center.add(txtFolder, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        btnChooseFolder = new JButton("Chọn...");
        btnChooseFolder.addActionListener(e -> chooseFolder());
        center.add(btnChooseFolder, gbc);

        gbc.gridx = 3;
        btnOpenFolder = new JButton("Mở thư mục");
        btnOpenFolder.addActionListener(e -> openFolder());
        center.add(btnOpenFolder, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        btnBackupNow = new JButton("Sao lưu ngay");
        btnBackupNow.addActionListener(e -> performBackup());
        center.add(btnBackupNow, gbc);

        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        chkAutoBackup = new JCheckBox("Tự động sao lưu khi tắt ứng dụng");
        chkAutoBackup.setSelected(BackupPreferences.isAutoBackupEnabled());
        chkAutoBackup.addActionListener(e -> {
            BackupPreferences.setAutoBackupEnabled(chkAutoBackup.isSelected());
        });
        center.add(chkAutoBackup, gbc);

        card.add(center, BorderLayout.CENTER);
        return card;
    }

    private Component buildHistoryCard() {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout(12, 12));

        JLabel title = new JLabel("Lịch sử sao lưu");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        card.add(title, BorderLayout.NORTH);

        historyModel = new DefaultTableModel(new Object[]{
                "Tên file", "Ngày tạo", "Dung lượng", "Đường dẫn"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblHistory = new JTable(historyModel);
        tblHistory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tblHistory);
        card.add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setOpaque(false);
        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.addActionListener(e -> reloadHistory());
        btnDeleteSelected = new JButton("Xóa bản đã chọn");
        btnDeleteSelected.addActionListener(e -> deleteSelectedBackup());
        btnRestoreFromFile = new JButton("Khôi phục từ file...");
        btnRestoreFromFile.addActionListener(e -> restoreFromFile());
        btnRestoreSelected = new JButton("Khôi phục bản đã chọn");
        btnRestoreSelected.addActionListener(e -> restoreSelected());
        actionPanel.add(btnRefresh);
        actionPanel.add(btnDeleteSelected);
        actionPanel.add(btnRestoreFromFile);
        actionPanel.add(btnRestoreSelected);
        card.add(actionPanel, BorderLayout.SOUTH);

        return card;
    }

    private Component buildStatusBar() {
        JPanel statusPanel = new JPanel(new BorderLayout(12, 0));
        statusPanel.setOpaque(false);
        lblStatus = new JLabel("Sẵn sàng");
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(false);
        progressBar.setVisible(false);
        statusPanel.add(lblStatus, BorderLayout.CENTER);
        statusPanel.add(progressBar, BorderLayout.EAST);
        return statusPanel;
    }

    private JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xE0E0E0)),
                new EmptyBorder(16, 16, 16, 16)
        ));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:20;");
        return panel;
    }

    private void chooseFolder() {
        JFileChooser chooser = new JFileChooser(currentDirectory.toFile());
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("Chọn thư mục sao lưu");
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            currentDirectory = chooser.getSelectedFile().toPath();
            txtFolder.setText(currentDirectory.toAbsolutePath().toString());
        }
    }

    private void openFolder() {
        try {
            if (!Desktop.isDesktopSupported()) {
                throw new UnsupportedOperationException("Hệ điều hành không hỗ trợ mở thư mục tự động.");
            }
            Desktop desktop = Desktop.getDesktop();
            if (!desktop.isSupported(Desktop.Action.OPEN)) {
                throw new UnsupportedOperationException("Không thể mở thư mục trên hệ thống này.");
            }
            desktop.open(currentDirectory.toFile());
        } catch (Exception ex) {
            showError("Không thể mở thư mục: " + ex.getMessage());
        }
    }

    /**
     * Xử lý nút "Sao lưu ngay":
     * - Đảm bảo thư mục tồn tại
     * - Gọi {@link DataBackupService#backup} trên luồng nền
     * - Cập nhật lịch sử (manifest) và status/progress trên UI.
     */
    private void performBackup() {
        Path directory = Path.of(txtFolder.getText());
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (Exception ex) {
                showError("Không thể tạo thư mục: " + ex.getMessage());
                return;
            }
        }
        setBusy(true, "Đang chuẩn bị sao lưu...");
        SwingWorker<DataBackupService.BackupResult, String> worker = new SwingWorker<>() {
            @Override
            protected DataBackupService.BackupResult doInBackground() throws Exception {
                return backupService.backup(directory, this::publish);
            }

            @Override
            protected void process(List<String> chunks) {
                if (!chunks.isEmpty()) {
                    updateStatus(chunks.get(chunks.size() - 1));
                }
            }

            @Override
            protected void done() {
                setBusy(false, "Sẵn sàng");
                try {
                    DataBackupService.BackupResult result = get();
                    BackupRecord record = new BackupRecord(
                            result.file().getFileName().toString(),
                            result.file().toAbsolutePath().toString(),
                            Files.size(result.file()),
                            manifestService.nowIso(),
                            result.databaseName()
                    );
                    manifestService.append(record);
                    reloadHistory();
            showNotification(Notifications.Type.SUCCESS,
                    "Sao lưu thành công vào:\n" + result.file());
                } catch (Exception ex) {
                    showError("Sao lưu thất bại: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }

    /**
     * Khôi phục từ bản backup được chọn trong bảng lịch sử.
     * Lấy đường dẫn từ cột "Đường dẫn" rồi gọi {@link #confirmAndRestore(Path)}.
     */
    private void restoreSelected() {
        int row = tblHistory.getSelectedRow();
        if (row < 0) {
            showNotification(Notifications.Type.WARNING, "Vui lòng chọn một bản sao lưu.");
            return;
        }
        String path = (String) historyModel.getValueAt(row, 3);
        confirmAndRestore(Path.of(path));
    }

    private void deleteSelectedBackup() {
        int row = tblHistory.getSelectedRow();
        if (row < 0) {
            showNotification(Notifications.Type.WARNING, "Vui lòng chọn một bản sao lưu để xóa.");
            return;
        }
        String fileName = (String) historyModel.getValueAt(row, 0);
        String absolutePath = (String) historyModel.getValueAt(row, 3);
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Xóa bản sao lưu \"" + fileName + "\"?\nFile sẽ bị xóa khỏi máy tính.",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            Files.deleteIfExists(Path.of(absolutePath));
        } catch (Exception ex) {
            showError("Không thể xóa file: " + ex.getMessage());
            return;
        }
        manifestService.removeByAbsolutePath(absolutePath);
        reloadHistory();
        showNotification(Notifications.Type.SUCCESS, "Đã xóa bản sao lưu \"" + fileName + "\".");
    }

    /**
     * Cho phép người dùng chọn một file .zip bất kỳ trên máy
     * (không nhất thiết nằm trong thư mục mặc định) rồi thực hiện khôi phục.
     */
    private void restoreFromFile() {
        JFileChooser chooser = new JFileChooser(currentDirectory.toFile());
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Gói sao lưu (*.zip)", "zip"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            confirmAndRestore(chooser.getSelectedFile().toPath());
        }
    }

    /**
     * Hiện hộp thoại xác nhận, sau đó chạy quá trình khôi phục trên luồng nền:
     * - Gọi {@link DataBackupService#restore(Path, java.util.function.Consumer)}
     * - Cập nhật status trên thanh trạng thái
     * - Hiển thị thông báo thành công/thất bại bằng toast.
     */
    private void confirmAndRestore(Path backupFile) {
        if (!Files.exists(backupFile)) {
            showError("Không tìm thấy file: " + backupFile);
            return;
        }
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Khôi phục sẽ ghi đè toàn bộ dữ liệu hiện tại.\nTiếp tục?",
                "Xác nhận khôi phục",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }
        setBusy(true, "Đang chuẩn bị khôi phục...");
        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                backupService.restore(backupFile, this::publish);
                return null;
            }

            @Override
            protected void process(List<String> chunks) {
                if (!chunks.isEmpty()) {
                    updateStatus(chunks.get(chunks.size() - 1));
                }
            }

            @Override
            protected void done() {
                setBusy(false, "Sẵn sàng");
                try {
                    get();
                    showNotification(Notifications.Type.SUCCESS,
                            "Khôi phục hoàn tất.\nVui lòng khởi động lại ứng dụng để áp dụng dữ liệu.");
                } catch (Exception ex) {
                    showError("Khôi phục thất bại: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }

    /**
     * Đọc file manifest để hiển thị danh sách các bản sao lưu đã tạo:
     * - Xóa các bản ghi trỏ tới file không còn tồn tại
     * - Đưa dữ liệu lên bảng lịch sử (tên file, ngày tạo, dung lượng, đường dẫn).
     */
    private void reloadHistory() {
        manifestService.pruneMissingFiles();
        List<BackupRecord> records = manifestService.readAll();
        historyModel.setRowCount(0);
        for (BackupRecord record : records) {
            historyModel.addRow(new Object[]{
                    record.fileName(),
                    formatTimestamp(record.createdAtIso()),
                    formatSize(record.sizeInBytes()),
                    record.absolutePath()
            });
        }
    }

    /**
     * Khóa/mở các nút thao tác trong lúc đang thực hiện backup/restore
     * và đồng thời hiển thị tiến trình + dòng trạng thái ở cuối màn hình.
     */
    private void setBusy(boolean busy, String message) {
        btnBackupNow.setEnabled(!busy);
        btnRestoreSelected.setEnabled(!busy);
        btnRestoreFromFile.setEnabled(!busy);
        if (btnDeleteSelected != null) {
            btnDeleteSelected.setEnabled(!busy);
        }
        btnChooseFolder.setEnabled(!busy);
        btnOpenFolder.setEnabled(!busy);
        progressBar.setVisible(busy);
        progressBar.setIndeterminate(busy);
        updateStatus(message);
    }

    private void updateStatus(String message) {
        lblStatus.setText(message);
    }

    private void showError(String message) {
        showNotification(Notifications.Type.ERROR, message);
        updateStatus(message);
    }

    private void showNotification(Notifications.Type type, String message) {
        java.awt.Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame frame) {
            Notifications.getInstance().setJFrame(frame);
        }
        Notifications.getInstance().show(type, message);
    }

    private String formatTimestamp(String iso) {
        if (iso == null) {
            return "";
        }
        try {
            Instant instant = Instant.parse(iso);
            return DISPLAY_TIME_FORMAT.format(instant);
        } catch (DateTimeParseException ex) {
            return iso;
        }
    }

    private String formatSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        }
        double size = bytes;
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }
        return String.format(Locale.US, "%.1f %s", size, units[unitIndex]);
    }
}

