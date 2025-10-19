/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.menu;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.util.UIScale;
import java.awt.*;
import java.awt.geom.Path2D;
import javax.swing.*;

/**
 * @author PhamTra
 */
public class PopupSubmenu extends JPanel {
    private final Menu menu;
    private final int menuIndex;
    private final int subMenuLeftGap = 20;
    private final int subMenuItemHeight = 30;
    private final int subMenuItemGap = 8;
    private final String[] menus;
    private JPopupMenu popup;

    public PopupSubmenu(ComponentOrientation orientation, Menu menu, int menuIndex, String[] menus) {
        this.menu = menu;
        this.menuIndex = menuIndex;
        this.menus = menus;
        applyComponentOrientation(orientation);
        init();
    }

    private void init() {
        setLayout(new MenuLayout());
        popup = new JPopupMenu();
        popup.putClientProperty(FlatClientProperties.STYLE,
                "background:$Menu.background;" +
                "borderColor:$Menu.background;");
        putClientProperty(FlatClientProperties.STYLE,
                "border:0,3,0,3;" +
                "background:$Menu.background;" +
                "foreground:$Menu.lineColor");

        for (int i = 1; i < menus.length; i++) {
            JButton button = createButtonItem(menus[i]);
            final int subIndex = i;
            button.addActionListener(e -> {
                menu.runEvent(menuIndex, subIndex);
                popup.setVisible(false);
            });
            add(button);
        }
        popup.add(this);
    }

    private JButton createButtonItem(String text) {
        JButton button = new JButton(text);
        button.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.background;"
                + "foreground:$Menu.foreground;"
                + "selectedBackground:rgb(23, 162, 184);"  // Màu xanh cyan giống title "Danh sách thông tin khách hàng"
                + "selectedForeground:#FFFFFF;"  // Text màu trắng khi được chọn
                + "borderWidth:0;"
                + "arc:10;"
                + "focusWidth:0;"
                + "iconTextGap:10;"
                + "font:bold 15 Roboto;" // Tăng kích thước font lên 15
                + "margin:5,11,5,11");
        return button;
    }

    public void show(Component com, int x, int y) {
        if (menu.getComponentOrientation().isLeftToRight()) {
            popup.show(com, x, y);
        } else {
            popup.show(com, -getPreferredSize().width - UIScale.scale(5), y);
        }
        applyAlignment();
        SwingUtilities.updateComponentTreeUI(popup);
    }

    private void applyAlignment() {
        setComponentOrientation(menu.getComponentOrientation());
        boolean isLeftToRight = menu.getComponentOrientation().isLeftToRight();
        for (Component c : getComponents()) {
            if (c instanceof JButton button) {
                button.setHorizontalAlignment(isLeftToRight ? JButton.LEFT : JButton.RIGHT);
            }
        }
    }

    protected void setSelectedIndex(int index) {
        for (int i = 0; i < getComponentCount(); i++) {
            Component com = getComponent(i);
            if (com instanceof JButton button) {
                button.setSelected(i == index - 1);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        int ssubMenuItemHeight = UIScale.scale(subMenuItemHeight);
        int ssubMenuLeftGap = UIScale.scale(subMenuLeftGap);
        Path2D.Double p = new Path2D.Double();
        int last = getComponent(getComponentCount() - 1).getY() + (ssubMenuItemHeight / 2);
        boolean ltr = getComponentOrientation().isLeftToRight();
        int round = UIScale.scale(10);
        int x = ltr ? (ssubMenuLeftGap - round) : (getWidth() - (ssubMenuLeftGap - round));

        p.moveTo(x, 0);
        p.lineTo(x, last - round);
        for (int i = 0; i < getComponentCount(); i++) {
            int com = getComponent(i).getY() + (ssubMenuItemHeight / 2);
            p.append(createCurve(round, x, com, ltr), false);
        }

        g2.setColor(getForeground());
        g2.setStroke(new BasicStroke(UIScale.scale(1f)));
        g2.draw(p);
        g2.dispose();
    }

    private Shape createCurve(int round, int x, int y, boolean ltr) {
        Path2D p2 = new Path2D.Double();
        p2.moveTo(x, y - round);
        p2.curveTo(x, y - round, x, y, x + (ltr ? round : -round), y);
        return p2;
    }

    private static class MenuLayout implements LayoutManager {
        private static final int MAX_WIDTH = 150;

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                Insets insets = parent.getInsets();
                int maxWidth = UIScale.scale(MAX_WIDTH);
                int ssubMenuLeftGap = UIScale.scale(((PopupSubmenu)parent).subMenuLeftGap);
                int width = getMaxWidth(parent) + ssubMenuLeftGap;
                int height = insets.top + insets.bottom;

                for (int i = 0; i < parent.getComponentCount(); i++) {
                    Component com = parent.getComponent(i);
                    if (com.isVisible()) {
                        height += UIScale.scale(((PopupSubmenu)parent).subMenuItemHeight);
                        width = Math.max(width, com.getPreferredSize().width);
                    }
                }

                width += insets.left + insets.right;
                return new Dimension(Math.max(width, maxWidth), height);
            }
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return new Dimension(0, 0);
        }

        @Override
        public void layoutContainer(Container parent) {
            synchronized (parent.getTreeLock()) {
                PopupSubmenu popupSubmenu = (PopupSubmenu)parent;
                boolean ltr = parent.getComponentOrientation().isLeftToRight();
                Insets insets = parent.getInsets();
                int ssubMenuLeftGap = UIScale.scale(popupSubmenu.subMenuLeftGap);
                int ssubMenuItemHeight = UIScale.scale(popupSubmenu.subMenuItemHeight);
                int x = insets.left + (ltr ? ssubMenuLeftGap : 0);
                int y = insets.top;
                int width = getMaxWidth(parent);
                int size = parent.getComponentCount();

                for (int i = 0; i < size; i++) {
                    Component com = parent.getComponent(i);
                    if (com.isVisible()) {
                        com.setBounds(x, y, width, ssubMenuItemHeight);
                        y += ssubMenuItemHeight + UIScale.scale(popupSubmenu.subMenuItemGap);
                    }
                }
            }
        }

        private int getMaxWidth(Container parent) {
            int width = 0;
            for (int i = 0; i < parent.getComponentCount(); i++) {
                Component com = parent.getComponent(i);
                if (com.isVisible()) {
                    width = Math.max(width, com.getPreferredSize().width);
                }
            }
            return width;
        }
    }
}
