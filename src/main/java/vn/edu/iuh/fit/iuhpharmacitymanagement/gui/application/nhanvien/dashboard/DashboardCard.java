package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.dashboard;

import javax.swing.*;
import java.awt.*;

/**
 * Component card để hiển thị thông tin thống kê trên dashboard
 */
public class DashboardCard extends JPanel {
    private JLabel lblTitle;
    private JLabel lblValue;
    private JLabel lblDescription;

    public DashboardCard(String title, String value, String description, Color backgroundColor, Color textColor) {
        setBackground(backgroundColor);
        setPreferredSize(new Dimension(280, 140));
        setMaximumSize(new Dimension(280, 140));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        setLayout(new BorderLayout(10, 10));

        // Panel chứa tiêu đề và giá trị
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Tiêu đề
        lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTitle.setForeground(textColor);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Giá trị
        lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValue.setForeground(textColor);
        lblValue.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Mô tả
        lblDescription = new JLabel(description);
        lblDescription.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDescription.setForeground(new Color(textColor.getRed(), textColor.getGreen(), textColor.getBlue(), 180));
        lblDescription.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(lblTitle);
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(lblValue);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(lblDescription);

        add(contentPanel, BorderLayout.CENTER);
    }

    public void updateValue(String value) {
        lblValue.setText(value);
    }

    public void updateDescription(String description) {
        lblDescription.setText(description);
    }
}

