/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.menu;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import vn.edu.iuh.fit.iuhpharmacitymanagement.util.ResizeImage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Path2D;
import java.util.List;
import javax.swing.*;

/**
 * @author PhamTra
 */
public class MenuItem extends JPanel {
    public boolean isMenuShow() {
        return menuShow;
    }

    public void setMenuShow(boolean menuShow) {
        this.menuShow = menuShow;
    }

    public float getAnimate() {
        return animate;
    }

    public void setAnimate(float animate) {
        this.animate = animate;
    }

    public String[] getMenus() {
        return menus;
    }

    public int getMenuIndex() {
        return menuIndex;
    }

    private final List<MenuEvent> events;
    private final Menu menu;
    private final String menus[];
    private final int menuIndex;
    private final int menuItemHeight = 50; // Tăng từ 45 lên 50
    private final int subMenuItemHeight = 40; // Tăng từ 35 lên 40
    private final int subMenuLeftGap = 34;
    private final int firstGap = 10; // Tăng từ 5 lên 10
    private final int bottomGap = 10; // Tăng từ 5 lên 10
    private boolean menuShow;
    private float animate;

    private PopupSubmenu popup;

    public MenuItem(Menu menu, String menus[], int menuIndex, List<MenuEvent> events) {
        this.menu = menu;
        this.menus = menus;
        this.menuIndex = menuIndex;
        this.events = events;
        init();
    }

    private Icon getIcon() {
        Color lightColor = FlatUIUtils.getUIColor("Menu.icon.lightColor", Color.red);
        Color darkColor = FlatUIUtils.getUIColor("Menu.icon.darkColor", Color.red);

        FlatSVGIcon icon;
        try {
            // Kiểm tra và ánh xạ chính xác icon dựa trên menuIndex
            String iconPath = "/img/" + menuIndex + ".svg";
            icon = new FlatSVGIcon(getClass().getResource(iconPath));

            // Nếu không tìm thấy icon, ném ngoại lệ để sử dụng fallback
            if (icon == null || icon.getIconWidth() <= 0) {
                throw new Exception("Icon not found");
            }
        } catch (Exception e) {
            // Fallback nếu không tìm thấy file tương ứng
            System.out.println("Cannot find icon for menuIndex: " + menuIndex + ", using fallback");
            icon = new FlatSVGIcon(getClass().getResource("/img/0.svg"));
        }

        FlatSVGIcon.ColorFilter f = new FlatSVGIcon.ColorFilter();
        f.add(Color.decode("#969696"), lightColor, darkColor);
        icon.setColorFilter(f);
        return ResizeImage.resizeImage(icon, 34, 34);
    }

    private void init() {
        setLayout(new MenuLayout());
        putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.background;"
                + "foreground:$Menu.lineColor; "
                + "font:bold 15 Roboto"); // Tăng kích thước font lên 15

        for (int i = 0; i < menus.length; i++) {
            JButton menuItem = createButtonItem(menus[i]);
            menuItem.setHorizontalAlignment(menuItem.getComponentOrientation().isLeftToRight() ? JButton.LEADING : JButton.TRAILING);
            if (i == 0) {
                menuItem.setIcon(getIcon());
                menuItem.addActionListener((ActionEvent e) -> {
                    if (menus.length > 1) {
                        if (menu.isMenuFull()) {
                            MenuAnimation.animate(MenuItem.this, !menuShow);
                        } else {
                            popup.show(MenuItem.this, (int) MenuItem.this.getWidth() + UIScale.scale(5), UIScale.scale(menuItemHeight) / 2);
                        }
                    } else {
                        menu.runEvent(menuIndex, 0);
                    }
                });
            } else {
                final int subIndex = i;
                menuItem.addActionListener((ActionEvent e) -> {
                    menu.runEvent(menuIndex, subIndex);
                });
            }
            add(menuItem);
        }
        popup = new PopupSubmenu(getComponentOrientation(), menu, menuIndex, menus);
    }

    protected void setSelectedIndex(int index) {
        int size = getComponentCount();
        boolean selected = false;
        for (int i = 0; i < size; i++) {
            Component com = getComponent(i);
            if (com instanceof JButton) {
                ((JButton) com).setSelected(i == index);
                if (i == index) {
                    selected = true;
                }
            }
        }
        ((JButton) getComponent(0)).setSelected(selected);
        popup.setSelectedIndex(index);
    }

    private JButton createButtonItem(String text) {
        JButton button = new JButton(text);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.background;"
                + "foreground:$Menu.foreground;"
                + "selectedBackground:rgb(23, 162, 184);"  // Màu xanh cyan giống title "Danh sách thông tin khách hàng"
                + "selectedForeground:#FFFFFF;"  // Text màu trắng khi được chọn
                + "borderWidth:0;"
                + "focusWidth:0;"
                + "innerFocusWidth:0;"
                + "arc:10;"
                + "iconTextGap:10;"
                + "font:bold 15 Roboto;"
                + "margin:8,11,8,11"); // Tăng margin từ 3,11,3,11 thành 8,11,8,11
        return button;
    }

    public void hideMenuItem() {
        animate = 0;
        menuShow = false;
    }

    public void setFull(boolean full) {
        if (full) {
            int size = getComponentCount();
            for (int i = 0; i < size; i++) {
                Component com = getComponent(i);
                if (com instanceof JButton) {
                    JButton button = (JButton) com;
                    button.setText(menus[i]);
                    button.setHorizontalAlignment(getComponentOrientation().isLeftToRight() ? JButton.LEFT : JButton.RIGHT);
                }
            }
        } else {
            for (Component com : getComponents()) {
                if (com instanceof JButton) {
                    JButton button = (JButton) com;
                    button.setText("");
                    button.setHorizontalAlignment(JButton.CENTER);
                }
            }
            animate = 0f;
            menuShow = false;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (animate > 0) {
            int ssubMenuItemHeight = UIScale.scale(subMenuItemHeight);
            int ssubMenuLeftGap = UIScale.scale(subMenuLeftGap);
            int smenuItemHeight = UIScale.scale(menuItemHeight);
            int sfirstGap = UIScale.scale(firstGap);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Path2D.Double p = new Path2D.Double();
            int last = getComponent(getComponentCount() - 1).getY() + (ssubMenuItemHeight / 2);
            boolean ltr = getComponentOrientation().isLeftToRight();
            int round = UIScale.scale(10);
            int x = ltr ? (ssubMenuLeftGap - round) : (getWidth() - (ssubMenuLeftGap - round));
            p.moveTo(x, smenuItemHeight + sfirstGap);
            p.lineTo(x, last - round);
            for (int i = 1; i < getComponentCount(); i++) {
                int com = getComponent(i).getY() + (ssubMenuItemHeight / 2);
                p.append(createCurve(round, x, com, ltr), false);
            }
            g2.setColor(getForeground());
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2.setStroke(new BasicStroke(UIScale.scale(1f)));
            g2.draw(p);
            g2.dispose();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (menus.length > 1) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (menu.isMenuFull()) {
                try {
                    // Sử dụng icon SVG từ resources
                    FlatSVGIcon icon;
                    if (menuShow) {
                        icon = new FlatSVGIcon(getClass().getResource("/img/menu_left.svg"));
                    } else {
                        icon = new FlatSVGIcon(getClass().getResource("/img/menu_right.svg"));
                    }

                    // Thiết lập màu cho icon
                    FlatSVGIcon.ColorFilter colorFilter = new FlatSVGIcon.ColorFilter();
                    colorFilter.add(Color.decode("#969696"), FlatUIUtils.getUIColor("Menu.arrowColor", getForeground()));
                    icon.setColorFilter(colorFilter);

                    // Điều chỉnh kích thước icon
                    icon = icon.derive(16, 16);

                    // Tính toán vị trí vẽ icon
                    boolean ltr = getComponentOrientation().isLeftToRight();
                    int smenuItemHeight = UIScale.scale(menuItemHeight);
                    int x = ltr ? getWidth() - icon.getIconWidth() - UIScale.scale(15) : UIScale.scale(15);
                    int y = (smenuItemHeight - icon.getIconHeight()) / 2;

                    // Vẽ icon
                    icon.paintIcon(this, g2, x, y);
                } catch (Exception e) {
                    // Fallback: vẽ mũi tên truyền thống nếu không load được icon
                    drawTraditionalArrow(g2);
                }
            }

            g2.dispose();
        }
    }

    private void drawTraditionalArrow(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.setColor(FlatUIUtils.getUIColor("Menu.arrowColor", getForeground()));
        int smenuItemHeight = UIScale.scale(menuItemHeight);
        boolean ltr = getComponentOrientation().isLeftToRight();
        g2.setStroke(new BasicStroke(UIScale.scale(1.5f)));

        // Vẽ mũi tên xoay khi menu mở rộng
        int arrowWidth = UIScale.scale(8);
        int arrowHeight = UIScale.scale(5);
        int ax = ltr ? (getWidth() - arrowWidth - UIScale.scale(15)) : UIScale.scale(15);
        int ay = (smenuItemHeight - arrowHeight) / 2;
        Path2D p = new Path2D.Double();
        if (!menuShow) {
            // Mũi tên chỉ xuống khi menu đóng
            p.moveTo(ax, ay);
            p.lineTo(ax + arrowWidth/2f, ay + arrowHeight);
            p.lineTo(ax + arrowWidth, ay);
        } else {
            // Mũi tên chỉ lên khi menu mở
            p.moveTo(ax, ay + arrowHeight);
            p.lineTo(ax + arrowWidth/2f, ay);
            p.lineTo(ax + arrowWidth, ay + arrowHeight);
        }
        g2.draw(p);
    }

    private Shape createCurve(int round, int x, int y, boolean ltr) {
        Path2D p2 = new Path2D.Double();
        p2.moveTo(x, y - round);
        p2.curveTo(x, y - round, x, y, x + (ltr ? round : -round), y);
        return p2;

    }

    private class MenuLayout implements LayoutManager {

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                Insets inset = parent.getInsets();
                int width = parent.getWidth();
                int height = inset.top + inset.bottom;
                int size = parent.getComponentCount();
                Component item = parent.getComponent(0);
                height += UIScale.scale(menuItemHeight);
                if (item.isVisible()) {
                    int subMenuHeight = size > 1 ? UIScale.scale(firstGap) + UIScale.scale(bottomGap) : 0;
                    for (int i = 1; i < size; i++) {
                        Component com = parent.getComponent(i);
                        if (com.isVisible()) {
                            subMenuHeight += UIScale.scale(subMenuItemHeight);
                        }
                    }
                    height += (subMenuHeight * animate);
                } else {
                    height = 0;
                }
                return new Dimension(width, height);
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
                boolean ltr = parent.getComponentOrientation().isLeftToRight();
                Insets insets = parent.getInsets();
                int x = insets.left;
                int y = insets.top;
                int width = parent.getWidth() - (insets.left + insets.right);
                int size = parent.getComponentCount();
                for (int i = 0; i < size; i++) {
                    Component com = parent.getComponent(i);
                    if (com.isVisible()) {
                        if (i == 0) {
                            int smenuItemHeight = UIScale.scale(menuItemHeight);
                            int sfirstGap = UIScale.scale(firstGap);
                            com.setBounds(x, y, width, smenuItemHeight);
                            y += smenuItemHeight + sfirstGap;
                        } else {
                            int ssubMenuLeftGap = UIScale.scale(subMenuLeftGap);
                            int subMenuX = ltr ? ssubMenuLeftGap : 0;
                            int ssubMenuItemHeight = UIScale.scale(subMenuItemHeight);
                            com.setBounds(x + subMenuX, y, width - ssubMenuLeftGap, ssubMenuItemHeight);
                            y += ssubMenuItemHeight;
                        }
                    }
                }
            }
        }
    }
}
