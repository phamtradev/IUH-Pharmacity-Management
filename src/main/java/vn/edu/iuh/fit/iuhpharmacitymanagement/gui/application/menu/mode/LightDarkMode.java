package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.menu.mode;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Raven
 */
public class LightDarkMode extends JPanel {

    public void setMenuFull(boolean menuFull) {
        this.menuFull = menuFull;
        if (menuFull) {
            buttonLight.setVisible(true);
            buttonDark.setVisible(true);
            buttonLighDark.setVisible(false);
        } else {
            buttonLight.setVisible(false);
            buttonDark.setVisible(false);
            buttonLighDark.setVisible(true);
            // Cập nhật icon cho buttonLighDark khi menu được đóng
            checkStyle();
        }
        revalidate();
        repaint();
    }

    private boolean menuFull = true;

    public LightDarkMode() {
        init();
    }

    private void init() {
        setBorder(new EmptyBorder(2, 2, 2, 2));
        setLayout(new LightDarkModeLayout());
        setBackground(java.awt.Color.decode("#002A42"));
        putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:999;"
                + "background:#002A42");
        FlatSVGIcon lightIcon = new FlatSVGIcon("vn/edu/iuh/fit/iuhpharmacitymanagement/gui/application/icon/mode/light.svg");
        FlatSVGIcon darkIcon = new FlatSVGIcon("vn/edu/iuh/fit/iuhpharmacitymanagement/gui/application/icon/mode/dark.svg");

        FlatSVGIcon.ColorFilter colorFilter = new FlatSVGIcon.ColorFilter();
        // Icon luôn màu trắng cho menu xanh đậm
        colorFilter.add(java.awt.Color.decode("#969696"),
                       java.awt.Color.WHITE, // Luôn màu trắng
                       java.awt.Color.WHITE); // Luôn màu trắng
        lightIcon.setColorFilter(colorFilter);
        darkIcon.setColorFilter(colorFilter);

        buttonLight = new JButton("Light", lightIcon);
        buttonDark = new JButton("Dark", darkIcon);

        // Đảm bảo chữ luôn màu trắng
        buttonLight.setForeground(java.awt.Color.WHITE);
        buttonDark.setForeground(java.awt.Color.WHITE);
        buttonLight.setBackground(java.awt.Color.decode("#00385C"));
        buttonDark.setBackground(java.awt.Color.decode("#00385C"));
        buttonLighDark = new JButton();
        buttonLighDark.setBackground(java.awt.Color.decode("#1A4A73"));
        buttonLighDark.setForeground(java.awt.Color.WHITE);
        buttonLighDark.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:999;"
                + "background:#1A4A73;"
                + "foreground:#FFFFFF;"
                + "focusWidth:0;"
                + "borderWidth:0;"
                + "innerFocusWidth:0");
        buttonLighDark.addActionListener((ActionEvent e) -> {
            changeMode(!FlatLaf.isLafDark());
        });
        checkStyle();
        buttonDark.addActionListener((ActionEvent e) -> {
            changeMode(true);
        });
        buttonLight.addActionListener((ActionEvent e) -> {
            changeMode(false);
        });

        add(buttonLight);
        add(buttonDark);
        add(buttonLighDark);
    }

    private void changeMode(boolean dark) {
        if (FlatLaf.isLafDark() != dark) {
            if (dark) {
                EventQueue.invokeLater(() -> {
                    FlatAnimatedLafChange.showSnapshot();
                    FlatMacDarkLaf.setup();
                    FlatLaf.updateUI();
                    checkStyle();
                    // Cập nhật lại icon sau khi thay đổi theme
                    updateIcons();
                    FlatAnimatedLafChange.hideSnapshotWithAnimation();
                });
            } else {
                EventQueue.invokeLater(() -> {
                    FlatAnimatedLafChange.showSnapshot();
                    FlatMacLightLaf.setup();
                    FlatLaf.updateUI();
                    checkStyle();
                    // Cập nhật lại icon sau khi thay đổi theme
                    updateIcons();
                    FlatAnimatedLafChange.hideSnapshotWithAnimation();
                });
            }
        }
    }

    private void checkStyle() {
        boolean isDark = FlatLaf.isLafDark();
        addStyle(buttonLight, !isDark);
        addStyle(buttonDark, isDark);

        FlatSVGIcon icon;
        if (isDark) {
            icon = new FlatSVGIcon("vn/edu/iuh/fit/iuhpharmacitymanagement/gui/application/icon/mode/dark.svg");
        } else {
            icon = new FlatSVGIcon("vn/edu/iuh/fit/iuhpharmacitymanagement/gui/application/icon/mode/light.svg");
        }

        // Thêm color filter cho icon - luôn màu trắng
        FlatSVGIcon.ColorFilter colorFilter = new FlatSVGIcon.ColorFilter();
        colorFilter.add(java.awt.Color.decode("#969696"),
                       java.awt.Color.WHITE, // Luôn màu trắng
                       java.awt.Color.WHITE); // Luôn màu trắng
        icon.setColorFilter(colorFilter);

        buttonLighDark.setIcon(icon);
    }

    private void updateIcons() {
        // Cập nhật lại icon cho buttonLight và buttonDark
        FlatSVGIcon lightIcon = new FlatSVGIcon("vn/edu/iuh/fit/iuhpharmacitymanagement/gui/application/icon/mode/light.svg");
        FlatSVGIcon darkIcon = new FlatSVGIcon("vn/edu/iuh/fit/iuhpharmacitymanagement/gui/application/icon/mode/dark.svg");

        FlatSVGIcon.ColorFilter colorFilter = new FlatSVGIcon.ColorFilter();
        colorFilter.add(java.awt.Color.decode("#969696"),
                       java.awt.Color.WHITE, // Luôn màu trắng
                       java.awt.Color.WHITE); // Luôn màu trắng
        lightIcon.setColorFilter(colorFilter);
        darkIcon.setColorFilter(colorFilter);

        buttonLight.setIcon(lightIcon);
        buttonDark.setIcon(darkIcon);
    }

    private void addStyle(JButton button, boolean style) {
        // Đảm bảo chữ luôn màu trắng
        button.setForeground(java.awt.Color.WHITE);

        if (style) {
            button.putClientProperty(FlatClientProperties.STYLE, ""
                    + "arc:999;"
                    + "background:#1A4A73;"
                    + "foreground:#FFFFFF;"
                    + "focusWidth:0;"
                    + "borderWidth:0;"
                    + "innerFocusWidth:0");
        } else {
            button.putClientProperty(FlatClientProperties.STYLE, ""
                    + "arc:999;"
                    + "background:#00385C;"
                    + "foreground:#FFFFFF;"
                    + "focusWidth:0;"
                    + "borderWidth:0;"
                    + "innerFocusWidth:0");
        }
    }

    private JButton buttonLight;
    private JButton buttonDark;
    private JButton buttonLighDark;

    private class LightDarkModeLayout implements LayoutManager {

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(5, buttonDark.getPreferredSize().height + (menuFull ? 0 : 5));
            }
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(0, 0);
            }
        }

        @Override
        public void layoutContainer(Container parent) {
            synchronized (parent.getTreeLock()) {
                Insets insets = parent.getInsets();
                int x = insets.left;
                int y = insets.top;
                int gap = 5;
                int width = parent.getWidth() - (insets.left + insets.right);
                int height = parent.getHeight() - (insets.top + insets.bottom);
                int buttonWidth = (width - gap) / 2;
                if (menuFull) {
                    buttonLight.setBounds(x, y, buttonWidth, height);
                    buttonDark.setBounds(x + buttonWidth + gap, y, buttonWidth, height);
                } else {
                    buttonLighDark.setBounds(x, y, width, height);
                }
            }
        }
    }
}
