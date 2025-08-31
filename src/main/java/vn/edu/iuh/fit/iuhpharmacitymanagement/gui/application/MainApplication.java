package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.form.MenuForm;

import java.awt.*;

public class MainApplication {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Cấu hình FlatLaf với thanh tiêu đề màu trắng
                System.setProperty("flatlaf.useWindowDecorations", "true");

                // Thiết lập Look and Feel
                FlatLightLaf.setup();

                // Cấu hình màu thanh tiêu đề toàn cục
                UIManager.put("TitlePane.background", java.awt.Color.WHITE);
                UIManager.put("TitlePane.foreground", java.awt.Color.BLACK);
                UIManager.put("TitlePane.inactiveBackground", java.awt.Color.WHITE);
                UIManager.put("TitlePane.inactiveForeground", Color.WHITE);

                // Loại bỏ tất cả shadow effects
                UIManager.put("Component.focusWidth", 0);
                UIManager.put("Component.innerFocusWidth", 0);
                UIManager.put("Component.borderWidth", 0);
                UIManager.put("PopupMenu.borderWidth", 0);
                UIManager.put("ScrollPane.borderWidth", 0);

                // Khởi tạo ứng dụng
                new MenuForm().setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
