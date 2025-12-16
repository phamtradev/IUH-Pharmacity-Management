package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.form;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.JDialog;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.net.URL;

/**
 * Dialog hiển thị trang hướng dẫn sử dụng ngay trong ứng dụng
 * bằng JavaFX WebView nhúng vào Swing.
 */
public class HelpDialog extends JDialog {

    private final JFXPanel jfxPanel = new JFXPanel();

    public HelpDialog(JFrame owner) {
        super(owner, "Hướng dẫn sử dụng", true);
        initUI();
    }

    public HelpDialog() {
        super((JFrame) null, "Hướng dẫn sử dụng", true);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        add(jfxPanel, BorderLayout.CENTER);

        // Tự co giãn gần full màn hình (90% kích thước màn hình)
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.9);
        int height = (int) (screenSize.height * 0.9);
        setSize(width, height);
        setLocationRelativeTo(getOwner());

        // Khởi tạo JavaFX WebView trên JavaFX Application Thread
        Platform.runLater(() -> {
            WebView webView = new WebView();
            WebEngine engine = webView.getEngine();

            // Bản offline (file tĩnh đóng gói trong JAR: /help/index.html)
            URL localUrl = getClass().getResource("/help/index.html");
            if (localUrl != null) {
                engine.load(localUrl.toExternalForm());
            }
            jfxPanel.setScene(new Scene(webView));
        });
    }
}


