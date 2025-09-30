package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JOptionPane;
import vn.edu.iuh.fit.iuhpharmacitymanagement.connectDB.ConnectDB;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.form.MenuForm;

import java.awt.*;

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

                // Khởi tạo ứng dụng
                new MenuForm().setVisible(true);
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
