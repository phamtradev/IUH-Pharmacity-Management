/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.menu;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.util.UIScale;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 *
 * @author PhamTra
 */
public class Menu extends JPanel {

    private final String menuItems[][] = {
        {"~MAIN~"},
        {"Dashboard"},
        {"~MENU~"},
        {"1", "1.1", "1.2", "1.3"},
        {"2"},
        {"3"},
        {"~QUẢN LÝ~"},
        {"Quản lý khách hàng", "4.1", "4.2", "4.3", "4.4"},
        {"5", "5.1", "5.2", "5.3", "5.4"},
        {"Test", "ok"},
        {"Test", "ok"},
        {"Test", "ok"},
        {"Test", "ok"},
        {"Test", "ok"},
        {"~BÁO CÁO~"},
        {"6", "6.1", "6.2", "6.3", "6.4"},
        {"7", "7.1", "7.2", "7.3"},
        {"8", "8.1", "8.2", "8.3", "8.4", "8.5", "8.6", "8.7"},
        {"Logout"}
    };

    public boolean isMenuFull() {
        return menuFull;
    }

    public void setMenuFull(boolean menuFull) {
        this.menuFull = menuFull;
        if (menuFull) {
            header.setText(headerName);
            header.setHorizontalAlignment(getComponentOrientation().isLeftToRight() ? JLabel.LEFT : JLabel.RIGHT);
        } else {
            header.setText("");
            header.setHorizontalAlignment(JLabel.CENTER);
        }
        // Đảm bảo header luôn màu trắng
        header.setForeground(java.awt.Color.WHITE);

        for (Component com : panelMenu.getComponents()) {
            if (com instanceof MenuItem) {
                ((MenuItem) com).setFull(menuFull);
            }
        }
    }

    private final List<MenuEvent> events = new ArrayList<>();
    private boolean menuFull = true;
    private final String headerName = "IUH PHARMACITY";

    protected final boolean hideMenuTitleOnMinimum = true;
    protected final int menuTitleLeftInset = 5;
    protected final int menuTitleVgap = 5;
    protected final int menuMaxWidth = 300; // Tăng từ 250 lên 300
    protected final int menuMinWidth = 80; // Tăng từ 60 lên 80
    protected final int headerFullHgap = 5;

    public Menu() {
        init();
    }

    private void init() {
        setLayout(new MenuLayout());
        // Đặt màu nền cố định cho menu
        setBackground(java.awt.Color.decode("#00385C"));
        putClientProperty(FlatClientProperties.STYLE, ""
                + "border:0,0,0,0;" // Loại bỏ tất cả border để menu có thể tràn lên trên
                + "background:#00385C;"
                + "arc:10"); // Sửa từ "arc:0,0,10,10" thành "arc:10"
        header = new JLabel(headerName);
        // Ẩn header bằng cách đặt visible = false
        header.setVisible(false);
        header.setForeground(java.awt.Color.WHITE);
        header.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:$Menu.header.font;"
                + "foreground:#FFFFFF");

        //  Menu
        scroll = new JScrollPane();
        panelMenu = new JPanel(new MenuItemLayout(this));
        panelMenu.setBackground(java.awt.Color.decode("#00385C"));
        panelMenu.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:2,2,2,2;" // Giảm padding
                + "background:#00385C");

        scroll.setViewportView(panelMenu);
        scroll.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:null");
        JScrollBar vscroll = scroll.getVerticalScrollBar();
        vscroll.setUnitIncrement(10);
        // Loại bỏ styling để tránh lỗi với Java 24
        // vscroll.putClientProperty(FlatClientProperties.STYLE, "");
        createMenu();
        add(header);
        add(scroll);
    }

    private void createMenu() {
        int index = 0;
        for (int i = 0; i < menuItems.length; i++) {
            String menuName = menuItems[i][0];
            if (menuName.startsWith("~") && menuName.endsWith("~")) {
                panelMenu.add(createTitle(menuName));
            } else {
                MenuItem menuItem = new MenuItem(this, menuItems[i], index++, events);
                panelMenu.add(menuItem);
            }
        }
        // các icon trong menu ghi theo dạng [số]+[tên] cho dễ chia
        // vd: 1-hehe, không ghi 01
        // icon tải về dùng đuôi svg để đỡ sửa lại w và h
    }

    private JLabel createTitle(String title) {
        String menuName = title.substring(1, title.length() - 1);
        JLabel lbTitle = new JLabel(menuName);
        // Đặt màu trắng cố định cho tiêu đề
        lbTitle.setForeground(java.awt.Color.WHITE);
        lbTitle.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:$Menu.label.font;"
                + "foreground:#FFFFFF");
        return lbTitle;
    }

    public void setSelectedMenu(int index, int subIndex) {
        runEvent(index, subIndex);
    }

    protected void setSelected(int index, int subIndex) {
        int size = panelMenu.getComponentCount();
        for (int i = 0; i < size; i++) {
            Component com = panelMenu.getComponent(i);
            if (com instanceof MenuItem) {
                MenuItem item = (MenuItem) com;
                if (item.getMenuIndex() == index) {
                    item.setSelectedIndex(subIndex);
                } else {
                    item.setSelectedIndex(-1);
                }
            }
        }
    }

    protected void runEvent(int index, int subIndex) {
        MenuAction menuAction = new MenuAction();
        for (MenuEvent event : events) {
            event.menuSelected(index, subIndex, menuAction);
        }
        if (!menuAction.isCancel()) {
            setSelected(index, subIndex);
        }
    }

    public void addMenuEvent(MenuEvent event) {
        events.add(event);
    }

    public void hideMenuItem() {
        for (Component com : panelMenu.getComponents()) {
            if (com instanceof MenuItem) {
                ((MenuItem) com).hideMenuItem();
            }
        }
        revalidate();
    }

    public boolean isHideMenuTitleOnMinimum() {
        return hideMenuTitleOnMinimum;
    }

    public int getMenuTitleLeftInset() {
        return menuTitleLeftInset;
    }

    public int getMenuTitleVgap() {
        return menuTitleVgap;
    }

    public int getMenuMaxWidth() {
        return menuMaxWidth;
    }

    public int getMenuMinWidth() {
        return menuMinWidth;
    }

    private JLabel header;
    private JScrollPane scroll;
    private JPanel panelMenu;

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
                return new Dimension(5, 5);
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
                int y = UIScale.scale(5); // Bắt đầu từ 5px vì header bị ẩn
                int gap = UIScale.scale(3); // Giảm gap
                int sheaderFullHgap = UIScale.scale(headerFullHgap);
                int width = parent.getWidth() - (insets.left + insets.right);
                int height = parent.getHeight() - y - insets.bottom; // Điều chỉnh chiều cao
                int iconWidth = width;
                int iconHeight = 0; // Header không hiển thị nên height = 0
                int hgap = menuFull ? sheaderFullHgap : 0;

                // Header không hiển thị nên không cần setBounds
                header.setBounds(0, 0, 0, 0);

                int menux = x;
                int menuy = y; // Scroll bắt đầu ngay từ y vì không có header
                int menuWidth = width;
                int menuHeight = height; // Sử dụng toàn bộ chiều cao còn lại
                scroll.setBounds(menux, menuy, menuWidth, menuHeight);
            }
        }
    }
}
