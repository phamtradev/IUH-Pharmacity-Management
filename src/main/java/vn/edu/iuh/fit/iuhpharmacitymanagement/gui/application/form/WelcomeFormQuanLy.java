package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.form;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;

/**
 * Giao diện chào mừng cho quản lý
 * @author PhamTra
 */
public class WelcomeFormQuanLy extends JPanel {

    public WelcomeFormQuanLy() {
        init();
    }

    private void init() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        putClientProperty(FlatClientProperties.STYLE, ""
                + "background:#FFFFFF;"
                + "border:0,0,0,0");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.anchor = GridBagConstraints.CENTER;

        // Tiêu đề chính
        JLabel lblWelcome = new JLabel("XIN CHÀO QUẢN LÝ");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblWelcome.setForeground(new Color(231, 76, 60)); // Màu đỏ cam (khác với nhân viên)
        add(lblWelcome, gbc);

        // Icon hoặc hình ảnh (emoji)
        gbc.gridy = 1;
        JLabel lblIcon = new JLabel("👨‍💼");
        lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 80));
        add(lblIcon, gbc);

        // Mô tả
        gbc.gridy = 2;
        JLabel lblDescription = new JLabel("Chào mừng đến với hệ thống quản trị!");
        lblDescription.setFont(new Font("Segoe UI", Font.ITALIC, 20));
        lblDescription.setForeground(new Color(127, 140, 141));
        add(lblDescription, gbc);

        // Hướng dẫn
        gbc.gridy = 3;
        gbc.insets = new Insets(40, 20, 20, 20);
        JLabel lblGuide = new JLabel("<html><center>Sử dụng menu bên trái để quản lý hệ thống:<br/>" +
                "Thống kê, Báo cáo, Quản lý nhân viên, và nhiều hơn nữa.</center></html>");
        lblGuide.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblGuide.setForeground(new Color(149, 165, 166));
        lblGuide.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblGuide, gbc);

        // Panel với thông tin nhanh
        gbc.gridy = 4;
        gbc.insets = new Insets(30, 20, 20, 20);
        add(createQuickInfoPanel(), gbc);
    }

    private JPanel createQuickInfoPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(20, 40, 20, 40)
        ));

        // Thông tin nhanh 1
        JPanel info1 = createInfoCard("📊", "Thống kê", "Tổng quan hệ thống");
        panel.add(info1);

        // Thông tin nhanh 2
        JPanel info2 = createInfoCard("📈", "Báo cáo", "Phân tích dữ liệu");
        panel.add(info2);

        // Thông tin nhanh 3
        JPanel info3 = createInfoCard("👥", "Nhân viên", "Quản lý nhân sự");
        panel.add(info3);

        // Thông tin nhanh 4
        JPanel info4 = createInfoCard("🎁", "Khuyến mãi", "Quản lý ưu đãi");
        panel.add(info4);

        return panel;
    }

    private JPanel createInfoCard(String icon, String title, String subtitle) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(15, 25, 15, 25)
        ));
        card.setPreferredSize(new Dimension(180, 120));

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblIcon);

        card.add(Box.createVerticalStrut(10));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblTitle);

        JLabel lblSubtitle = new JLabel(subtitle);
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSubtitle.setForeground(new Color(127, 140, 141));
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblSubtitle);

        return card;
    }
}


