/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.menu;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.menu.mode.ToolBarAccentColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 * @author PhamTra
 */
public class Menu extends JPanel {

    private final String[][] menuItemsManager = {
        {"Thống kê"},
        {"Báo cáo thu chi"},
        {"Quản lý sản phẩm"},
        {"Quản lý đơn vị tính"},
        {"Quản lý lô hàng"},
        {"Quản lý nhân viên"},
        {"Quản lý khách hàng"},
        {"Quản lý nhà cung cấp"},
        {"Quản lý nhập hàng"},
        {"Quản lý trả hàng"},
        {"Quản lý xuất hủy"},
        {"Quản lý đơn hàng"},
        {"Quản lý khuyến mãi"},
        {"Thông tin cá nhân"},
        {"Chat Bot"},
        {"Trợ giúp"},
        {"Đăng xuất"}
    };

    private final String[][] menuItemsStaff = {
        {"Bán hàng"},
        {"Quản lý sản phẩm"},
        {"Quản lý khách hàng"},
        {"Quản lý nhà cung cấp"},
        {"Phiếu nhập hàng"},
        {"Phiếu trả hàng"},
        {"Phiếu xuất hủy"},
        {"Thống kê cá nhân"},
        {"Thông tin cá nhân"},
        {"Chat Bot"},
        {"Trợ giúp"},
        {"Đăng xuất"}
    };

    private final List<MenuEvent> events = new ArrayList<>();
    private boolean menuFull = true;

    protected final boolean hideMenuTitleOnMinimum = true;
    protected final int menuTitleLeftInset = 8;
    protected final int menuTitleVgap = 8;
    protected final int menuMaxWidth = 280;
    protected final int menuMinWidth = 70;
    protected final int headerFullHgap = 8;
    private final int type;

    private JScrollPane scroll;
    private JPanel panelMenu;
    private ToolBarAccentColor toolBarAccentColor;

    public Menu(int type) {
        this.type = type;
        init();
    }
    public int getType(){
        return this.type;
    }
    public boolean isMenuFull() {
        return menuFull;
    }

    public void setMenuFull(boolean menuFull) {
        this.menuFull = menuFull;
        for (Component com : panelMenu.getComponents()) {
            if (com instanceof MenuItem) {
                ((MenuItem) com).setFull(menuFull);
            }
        }
        if (toolBarAccentColor != null) {
            toolBarAccentColor.setMenuFull(menuFull);
        }
    }

    private void init() {
        setLayout(new MenuLayout());
        putClientProperty(FlatClientProperties.STYLE,
                "border:20,2,2,2;"
                + "background:$Menu.background;"
                + "arc:10");

        scroll = new JScrollPane();
        panelMenu = new JPanel(new MenuItemLayout(this));
        panelMenu.putClientProperty(FlatClientProperties.STYLE,
                "border:5,5,5,5;"
                + "background:$Menu.background");

        scroll.setViewportView(panelMenu);
        scroll.putClientProperty(FlatClientProperties.STYLE, "border:null");

        JScrollBar vscroll = scroll.getVerticalScrollBar();
        vscroll.setUnitIncrement(10);

        createMenu();

        toolBarAccentColor = new ToolBarAccentColor(this);
        toolBarAccentColor.setVisible(FlatUIUtils.getUIBoolean("AccentControl.show", false));

        add(scroll);
        add(toolBarAccentColor);
    }

//    private void createMenu() {
//        int index = 0;
//        String[][] items = (type == 2) ? menuItemsManager : menuItemsStaff;
//
//        for (String[] item : items) {
//            if (item[0].startsWith("~") && item[0].endsWith("~")) {
//                panelMenu.add(createTitle(item[0]));
//            } else {
//                int iconIndex;
//                if (type == 2) { // Nhân viên
//                    // Map chính xác icon cho từng chức năng của nhân viên
//                    switch (index) {
//                        case 0: iconIndex = 20; break;  // Bán hàng -> 20.svg
//                        case 1: iconIndex = 21; break;  // Quản lý sản phẩm -> 21.svg
//                        case 2: iconIndex = 22; break;  // Quản lý khách hàng -> 22.svg
//                        case 3: iconIndex = 23; break;  // Quản lý nhà cung cấp -> 23.svg
//                        case 4: iconIndex = 24; break;  // Phiếu nhập hàng -> 24.svg
//                        case 5: iconIndex = 25; break;  // Phiếu trả hàng -> 25.svg
//                        case 6: iconIndex = 26; break;  // Phiếu xuất hủy -> 26.svg
//                        case 7: iconIndex = 27; break;  // Thống kê cá nhân -> 27.svg
//                        case 8: iconIndex = 28; break;  // Thông tin cá nhân -> 28.svg
//                        case 9: iconIndex = 29; break;  // Hướng dẫn sử dụng -> 29.svg
//                        case 10: iconIndex = 30; break; // Đăng xuất -> 30.svg
//                        default: iconIndex = 20; break;
//                    }
//                } else { // Quản lý
//                    iconIndex = index;
//                }
//                panelMenu.add(new MenuItem(this, item, iconIndex, events));
//                index++;
//            }
//        }
//    }
    private void createMenu() {
        int index = 0;
        //1 là Nhân viên, 2 là quản lý
        if (type == 1) {
            // Type 1 = Nhân viên (menuItemsStaff) với index 20-30
            // Thêm các menu chính (trừ 2 menu cuối)
            for (int i = 0; i < menuItemsStaff.length - 2; i++) {
                String menuName = menuItemsStaff[i][0];
                if (menuName.startsWith("~") && menuName.endsWith("~")) {
                    panelMenu.add(createTitle(menuName));
                } else {
                    MenuItem menuItem = new MenuItem(this, menuItemsStaff[i], 20 + index++, events);
                    panelMenu.add(menuItem);
                }
            }

            // Thêm glue để đẩy các menu xuống dưới
            panelMenu.add(Box.createVerticalGlue());

            // Thêm separator
            panelMenu.add(createSeparator());

            // Thêm 2 menu cuối (Trợ giúp và Đăng xuất)
            for (int i = menuItemsStaff.length - 2; i < menuItemsStaff.length; i++) {
                MenuItem menuItem = new MenuItem(this, menuItemsStaff[i], 20 + index++, events);
                panelMenu.add(menuItem);
            }
        } else {
            // Type 2 = Quản lý (menuItemsManager) với index 0-14
            // Thêm các menu chính (trừ 2 menu cuối)
            for (int i = 0; i < menuItemsManager.length - 2; i++) {
                String menuName = menuItemsManager[i][0];
                if (menuName.startsWith("~") && menuName.endsWith("~")) {
                    panelMenu.add(createTitle(menuName));
                } else {
                    MenuItem menuItem = new MenuItem(this, menuItemsManager[i], index++, events);
                    panelMenu.add(menuItem);
                }
            }

            // Thêm glue để đẩy các menu xuống dưới
            panelMenu.add(Box.createVerticalGlue());

            // Thêm separator
            panelMenu.add(createSeparator());

            // Thêm 2 menu cuối (Trợ giúp và Đăng xuất)
            for (int i = menuItemsManager.length - 2; i < menuItemsManager.length; i++) {
                MenuItem menuItem = new MenuItem(this, menuItemsManager[i], index++, events);
                panelMenu.add(menuItem);
            }
        }

    }

    private JLabel createTitle(String title) {
        String menuName = title.substring(1, title.length() - 1);
        JLabel lbTitle = new JLabel(menuName);
        lbTitle.putClientProperty(FlatClientProperties.STYLE,
                "font:$Menu.label.font;"
                + "foreground:$Menu.title.foreground");
        return lbTitle;
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE,
                "foreground:$Menu.title.foreground");
        return separator;
    }

    public void setSelectedMenu(int index, int subIndex) {
        runEvent(index, subIndex);
    }

    protected void setSelected(int index, int subIndex) {
        for (Component com : panelMenu.getComponents()) {
            if (com instanceof MenuItem item) {
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
            if (event != null) {
                event.menuSelected(index, subIndex, menuAction);
                if (menuAction.isCancel()) {
                    break;
                }
            }
        }
        if (!menuAction.isCancel()) {
            setSelected(index, subIndex);
            hideMenuItem();
        }
    }

    public void addMenuEvent(MenuEvent event) {
        if (event != null && !events.contains(event)) {
            events.add(event);
        }
    }

    public void hideMenuItem() {
        for (Component com : panelMenu.getComponents()) {
            if (com instanceof MenuItem) {
                ((MenuItem) com).hideMenuItem();
            }
        }
        revalidate();
        repaint();
    }

    @Override
    public void revalidate() {
        super.revalidate();
        if (getParent() != null) {
            getParent().revalidate();
        }
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
                int y = insets.top;
                int gap = UIScale.scale(5);
                int width = parent.getWidth() - (insets.left + insets.right);
                int height = parent.getHeight() - (insets.top + insets.bottom);

                int accentColorHeight = 0;
                if (toolBarAccentColor != null && toolBarAccentColor.isVisible()) {
                    accentColorHeight = toolBarAccentColor.getPreferredSize().height + gap;
                }

                // Scroll panel chiếm toàn bộ không gian từ top
                scroll.setBounds(x, y, width, height - accentColorHeight);

                if (toolBarAccentColor != null && toolBarAccentColor.isVisible()) {
                    int tbWidth = width - gap * 2;
                    int tbHeight = toolBarAccentColor.getPreferredSize().height;
                    int tbx = x + gap;
                    int tby = y + height - tbHeight - gap;
                    toolBarAccentColor.setBounds(tbx, tby, tbWidth, tbHeight);
                }
            }
        }
    }
 
}
