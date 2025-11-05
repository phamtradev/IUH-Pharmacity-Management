package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.dashboard;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.swing.*;
import java.awt.*;
import vn.edu.iuh.fit.iuhpharmacitymanagement.util.ResizeImage;

/**
 * Component card để hiển thị thông tin thống kê trên dashboard
 */
public class DashboardCard extends JPanel {
    private JLabel lblTitle;
    private JLabel lblValue;
    private JLabel lblDescription;
    private JLabel lblIcon;

    public DashboardCard(String title, String value, String description, Color backgroundColor, Color textColor, String iconName) {
        setBackground(backgroundColor);
        setPreferredSize(new Dimension(280, 140));
        setMaximumSize(new Dimension(280, 140));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        setLayout(new BorderLayout(15, 10));

        // Panel chứa tiêu đề và giá trị
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Tiêu đề
        lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTitle.setForeground(textColor);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Giá trị
        lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblValue.setForeground(textColor);
        lblValue.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Mô tả
        lblDescription = new JLabel(description);
        lblDescription.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        Color descColor = new Color(
            Math.max(0, Math.min(255, textColor.getRed() - 20)),
            Math.max(0, Math.min(255, textColor.getGreen() - 20)),
            Math.max(0, Math.min(255, textColor.getBlue() - 20)),
            200
        );
        lblDescription.setForeground(descColor);
        lblDescription.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(lblTitle);
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(lblValue);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(lblDescription);

        add(contentPanel, BorderLayout.CENTER);
        
        // Icon
        if (iconName != null && !iconName.isEmpty()) {
            try {
                FlatSVGIcon svgIcon = new FlatSVGIcon(getClass().getResource("/img/" + iconName));
                Icon resizedIcon = ResizeImage.resizeImage(svgIcon, 50, 50);
                lblIcon = new JLabel(resizedIcon);
                lblIcon.setOpaque(false);
                
                // Tạo panel cho icon để căn giữa theo chiều dọc
                JPanel iconPanel = new JPanel(new GridBagLayout());
                iconPanel.setOpaque(false);
                iconPanel.add(lblIcon);
                
                add(iconPanel, BorderLayout.EAST);
            } catch (Exception e) {
                System.out.println("Không thể tải icon: " + iconName);
            }
        }
    }

    public void updateValue(String value) {
        lblValue.setText(value);
    }

    public void updateDescription(String description) {
        lblDescription.setText(description);
    }
}

