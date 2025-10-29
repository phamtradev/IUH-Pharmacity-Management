package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JOptionPane;
import vn.edu.iuh.fit.iuhpharmacitymanagement.connectDB.ConnectDB;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.form.MenuForm;

import java.awt.*;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.login.LoginFrame;

public class MainApplication {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Thiết lập Look and Feel trước khi test kết nối
                System.setProperty("flatlaf.useWindowDecorations", "true");
                FlatLightLaf.setup();
                UIManager.put("TitlePane.background", java.awt.Color.WHITE);
                UIManager.put("TitlePane.foreground", java.awt.Color.BLACK);
                UIManager.put("TitlePane.inactiveBackground", java.awt.Color.WHITE);
                UIManager.put("TitlePane.inactiveForeground", Color.WHITE);
                UIManager.put("Component.focusWidth", 0);
                UIManager.put("Component.innerFocusWidth", 0);
                UIManager.put("Component.borderWidth", 0);
                UIManager.put("PopupMenu.borderWidth", 0);
                UIManager.put("ScrollPane.borderWidth", 0);
                UIManager.put("Button.arc", 10); // bo tròn các JButton
                UIManager.put("Component.arc", 10); // Bo tròn góc cho TẤT CẢ component
                UIManager.put("TextComponent.arc", 10); // Cụ thể hơn cho JTextField, JPasswordField...

                //Thay đổi cho process bar
                UIManager.put("ProgressBar.arc", 10);
                UIManager.put("ProgressBar.height", 12);
                UIManager.put("ProgressBar.foreground", new java.awt.Color(0, 153, 255));
                UIManager.put("ProgressBar.background", new java.awt.Color(220, 220, 220));

                //Thay đổi cho JtextField
                UIManager.put("Component.borderWidth", 1);
                UIManager.put("Component.borderColor", new java.awt.Color(150, 180, 255));

                // Khi người dùng click vào JTextField, đường viền màu xanh sẽ xuất hiện, báo hiệu rằng ô này đang hoạt động.
                UIManager.put("Component.focusColor", UIManager.getColor("Component.accentColor"));
                UIManager.put("Component.focusWidth", 2);

                // Thiết lập Look and Feel
                FlatLightLaf.setup();
                // Test kết nối database
                if (!ConnectDB.testConnection()) {
                    JOptionPane.showMessageDialog(null,
                            "Không thể kết nối đến cơ sở dữ liệu.",
                            "Lỗi Kết Nối",
                            JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                } else {
                    System.out.println("Kết nối đến cơ sở dữ liệu thành công!");
                }

                // Khởi tạo ứng dụng - chạy vào giao diện chính luôn
                //new MenuForm().setVisible(true);
                new LoginFrame().setVisible(true);
                //new vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.loading.SplashScreen().setVisible(true);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Đã xảy ra lỗi khi khởi động ứng dụng: " + e.getMessage(),
                        "Lỗi Ứng Dụng",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}
