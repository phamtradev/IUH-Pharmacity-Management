/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.loading;

import java.awt.Image;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import vn.edu.iuh.fit.iuhpharmacitymanagement.connectDB.ConnectDB;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.form.WelcomeFormNhanVien;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.form.WelcomeFormQuanLy;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.login.LoginFrame;

/**
 *
 * @author User
 */
public class SplashScreen extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(SplashScreen.class.getName());

    /**
     * Creates new form SplashScreen
     */
    public SplashScreen() {
        initComponents();

        BackgroundPanel.setComponentZOrder(progressBar, 0); // ProgressBar ở lớp trên cùng
        BackgroundPanel.setComponentZOrder(lblLoading, 1);
        
        BackgroundPanel.setComponentZOrder(IUHLogo, 3);
        BackgroundPanel.setComponentZOrder(BackgroundImg, 3);

        setLocationRelativeTo(null);
        setResizable(false);

        // Thread thực tế để load các bước theo tiến trình thật
        new Thread(() -> {
            try {
                // Bước 1: Khởi tạo hệ thống
                updateProgress("Đang khởi tạo hệ thống...", 0);
                
                // Bước 2: Load hình ảnh (thực tế)
                updateProgress("Đang tải hình ảnh...", 5);
                setImageToLabel(BackgroundImg, "/img/LoadingImage.png");
                updateProgress("Đang tải hình ảnh...", 10);
                setImageToLabel(IUHLogo, "/img/IUH.png");
                updateProgress("Đã tải hình ảnh", 15);
                
                // Bước 3: Kết nối database (thực tế)
                updateProgress("Đang kết nối cơ sở dữ liệu...", 20);
                boolean dbConnected = connectDatabase();
                if (dbConnected) {
                    updateProgress("Đã kết nối cơ sở dữ liệu", 40);
                } else {
                    updateProgress("Không thể kết nối cơ sở dữ liệu", 40);
                    // Không thoát ứng dụng, vẫn cho phép vào màn hình đăng nhập
                    // Người dùng có thể khôi phục dữ liệu từ màn hình đăng nhập
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this,
                            "Không thể kết nối đến cơ sở dữ liệu!\n" +
                            "Bạn có thể khôi phục dữ liệu từ màn hình đăng nhập.",
                            "Cảnh báo",
                            JOptionPane.WARNING_MESSAGE);
                    });
                    // Tiếp tục chạy để mở màn hình đăng nhập
                }
                
                // Bước 4: Chuẩn bị giao diện (preload LoginFrame)
                updateProgress("Chuẩn bị giao diện...", 50);
                LoginFrame loginFrame = new LoginFrame();
                updateProgress("Đã chuẩn bị giao diện", 70);
                
                // Bước 5: Tải tài nguyên (thực tế - có thể thêm các tác vụ khác ở đây)
                updateProgress("Đang tải tài nguyên...", 75);
                // Có thể thêm các tác vụ load resources khác ở đây
                updateProgress("Đã tải tài nguyên", 85);
                
                // Bước 6: Hoàn tất
                updateProgress("Hoàn tất! Đang vào hệ thống...", 95);
                Thread.sleep(300); // Delay nhỏ để người dùng thấy thông báo
                updateProgress("Hoàn tất! Đang vào hệ thống...", 100);
                
                // Mở LoginFrame
                SwingUtilities.invokeLater(() -> {
                    dispose();
                    loginFrame.setVisible(true);
                });

            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                        "Đã xảy ra lỗi khi khởi động ứng dụng: " + e.getMessage(),
                        "Lỗi Khởi Động",
                        JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                });
            }
        }).start();
    }

    private void updateProgress(String message, int progress) {
        SwingUtilities.invokeLater(() -> {
            lblLoading.setText(message);
            progressBar.setValue(progress);
        });
    }

    public void setProgress(int value) {
        //quyền truy cập private ProgressBar để hiện số tiến trình
        this.progressBar.setValue(value);
    }

    private void setImageToLabel(javax.swing.JLabel label, String imagePath) {
        try {
            java.net.URL imgURL = getClass().getResource(imagePath);
            System.out.println("Đường dẫn ảnh: " + imgURL);

            if (imgURL != null) {
                ImageIcon originalIcon = new ImageIcon(imgURL);
                Image scaledImage = originalIcon.getImage().getScaledInstance(
                        label.getWidth(),
                        label.getHeight(),
                        Image.SCALE_SMOOTH
                );
                label.setIcon(new ImageIcon(scaledImage));
            } else {
                logger.warning("Không tìm thấy ảnh ở đường dẫn: " + imagePath);
            }
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Lỗi khi load hình ảnh", e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BackgroundPanel = new javax.swing.JPanel();
        BackgroundImg = new javax.swing.JLabel();
        IUHLogo = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        lblLoading = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        setUndecorated(true);

        BackgroundPanel.setBackground(new java.awt.Color(255, 255, 255));
        BackgroundPanel.setMinimumSize(new java.awt.Dimension(1200, 500));
        BackgroundPanel.setPreferredSize(new java.awt.Dimension(1000, 600));
        BackgroundPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        BackgroundImg.setText("BackgroundImage");
        BackgroundImg.setPreferredSize(new java.awt.Dimension(1200, 600));
        BackgroundPanel.add(BackgroundImg, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, -2, 1200, 600));

        IUHLogo.setText("IUHLogo");
        IUHLogo.setMaximumSize(new java.awt.Dimension(424, 300));
        IUHLogo.setMinimumSize(new java.awt.Dimension(424, 300));
        IUHLogo.setPreferredSize(new java.awt.Dimension(424, 300));
        BackgroundPanel.add(IUHLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 250, 140));

        progressBar.setStringPainted(true);
        BackgroundPanel.add(progressBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 576, 1190, -1));

        lblLoading.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblLoading.setText("Loading...");
        BackgroundPanel.add(lblLoading, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 550, -1, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BackgroundPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1200, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(BackgroundPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>   
        new SplashScreen().setVisible(true);

    }

    //các task thực tế
    private boolean connectDatabase() {
        try {
            // Test kết nối thực sự
            Connection conn = ConnectDB.getConnection();
            if (conn != null && !conn.isClosed()) {
                conn.close(); // Đóng connection test
                return true;
            }
            return false;
        } catch (SQLException e) {
            logger.log(java.util.logging.Level.SEVERE, "Lỗi kết nối database", e);
            return false;
        }
    }

    private void openLogin() {
        new LoginFrame().setVisible(true);
    }

    private void openFrameForMana() {
        new WelcomeFormQuanLy().setVisible(true);
    }

    private void openFrameForEmp() {
        new WelcomeFormNhanVien().setVisible(true);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BackgroundImg;
    private javax.swing.JPanel BackgroundPanel;
    private javax.swing.JLabel IUHLogo;
    private javax.swing.JLabel lblLoading;
    private javax.swing.JProgressBar progressBar;
    // End of variables declaration//GEN-END:variables
}
