/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.form;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.util.UIScale;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.menu.Menu;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.menu.MenuAction;

/**
 *
 * @author PhamTra
 */
public class MainForm extends JLayeredPane {

    public MainForm() {
        init();
    }

    private void init() {
        setBorder(new EmptyBorder(0, 0, 0, 0));
        setLayout(new MainFormLayout());
        setBackground(java.awt.Color.WHITE);
        putClientProperty(FlatClientProperties.STYLE, ""
                + "background:#FFFFFF;"
                + "border:0,0,0,0");
        menu = new Menu(2);
        panelBody = new JPanel(new BorderLayout());
        panelBody.setBackground(java.awt.Color.WHITE);
        // Loại bỏ viền xám
        panelBody.setBorder(null);
        panelBody.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:#FFFFFF;"
                + "border:0,0,0,0");
        initMenuArrowIcon();
        menuButton.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:#D3D3D3;"
                + "arc:999;"
                + "focusWidth:0;"
                + "borderWidth:0");
        menuButton.addActionListener((ActionEvent e) -> {
            setMenuFull(!menu.isMenuFull());
        });
//        initMenuEvent();
        setLayer(menuButton, JLayeredPane.POPUP_LAYER);
        add(menuButton);
        add(menu);
        add(panelBody);
        initMenuEvent();

    }

    @Override
    public void applyComponentOrientation(ComponentOrientation o) {
        super.applyComponentOrientation(o);
        initMenuArrowIcon();
    }

    
    private void initMenuArrowIcon() {
        if (menuButton == null) {
            menuButton = new JButton();
        }

        String iconFile = getComponentOrientation().isLeftToRight()
                ? "menu_left.svg"
                : "menu_right.svg";

        // Load từ resources/img/
        URL iconUrl = getClass().getResource("/img/" + iconFile);
        FlatSVGIcon svgIcon = new FlatSVGIcon(iconUrl);
            
        // Icon màu đen
        FlatSVGIcon.ColorFilter colorFilter = new FlatSVGIcon.ColorFilter();
        colorFilter.add(java.awt.Color.decode("#969696"), java.awt.Color.BLACK);
        svgIcon.setColorFilter(colorFilter);
        menuButton.setIcon(svgIcon);
    }

private void initMenuEvent() {
    menu.addMenuEvent((int index, int subIndex, MenuAction action) -> {
//        System.out.println("Menu clicked: " + index + " - " + subIndex);
        switch (index) {
            case 25:
                showForm(new vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.form.other.QuanLyTraHangGUI());
                break;
            default:
                action.cancel();
                break;
        }
    });
}

    private void setMenuFull(boolean full) {
        String icon;
        if (getComponentOrientation().isLeftToRight()) {
            icon = (full) ? "menu_left.svg" : "menu_right.svg";
        } else {
            icon = (full) ? "menu_right.svg" : "menu_left.svg";
        }

        URL iconUrl = getClass().getResource("/img/" + icon);
        FlatSVGIcon svgIcon = new FlatSVGIcon(iconUrl);

        // tạo scaled thay cho FlatSVGIcon svgIcon = new FlatSVGIcon(iconPath, 0.8f); để bt tải icon thành công hay k
        FlatSVGIcon scaledIcon = svgIcon.derive(0.8f);

    // ok
        FlatSVGIcon.ColorFilter colorFilter = new FlatSVGIcon.ColorFilter();
        colorFilter.add(java.awt.Color.decode("#969696"), java.awt.Color.BLACK);
        scaledIcon.setColorFilter(colorFilter);

        menuButton.setIcon(scaledIcon);
        menu.setMenuFull(full);
        revalidate();
    }
    
//    private void setMenuFull(boolean full) {
//    String icon;
//    if (getComponentOrientation().isLeftToRight()) {
//        icon = (full) ? "menu_left.svg" : "menu_right.svg";
//    } else {
//        icon = (full) ? "menu_right.svg" : "menu_left.svg";
//    }
//    
//            // Load từ resources/img/
//        URL iconUrl = getClass().getResource("/img/" + icon);
//
//    //thử coi lỗi ở đâu mà k load hình
//    
//    URL resourceUrl = getClass().getResource(iconUrl);
//    
//    if (resourceUrl == null) {
//        System.err.println("sai cmnr");
//    } else {
//        System.out.println("adu ngon" + resourceUrl.toExternalForm());
//    }
//
//    FlatSVGIcon svgIcon = new FlatSVGIcon(iconPath, 0.8f);
//    FlatSVGIcon.ColorFilter colorFilter = new FlatSVGIcon.ColorFilter();
//    // Icon màu đen
//    colorFilter.add(java.awt.Color.decode("#969696"), java.awt.Color.BLACK);
//    svgIcon.setColorFilter(colorFilter);
//    menuButton.setIcon(svgIcon);
//    menu.setMenuFull(full);
//    revalidate();
//}

    public void hideMenu() {
        menu.hideMenuItem();
    }

    public void showForm(Component component) {
        panelBody.removeAll();
        panelBody.add(component);
        panelBody.repaint();
        panelBody.revalidate();
    }

    public void setSelectedMenu(int index, int subIndex) {
        menu.setSelectedMenu(index, subIndex);
    }

    private Menu menu;
    private JPanel panelBody;
    private JButton menuButton;

    private class MainFormLayout implements LayoutManager {

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
                boolean ltr = parent.getComponentOrientation().isLeftToRight();
                Insets insets = UIScale.scale(parent.getInsets());
                int x = insets.left;
                int y = 0; // Menu bắt đầu từ y = 0 để tràn lên trên
                int width = parent.getWidth() - (insets.left + insets.right);
                int height = parent.getHeight(); // Sử dụng toàn bộ chiều cao
                int menuWidth = UIScale.scale(menu.isMenuFull() ? menu.getMenuMaxWidth() : menu.getMenuMinWidth());
                int menuX = ltr ? x : x + width - menuWidth;
                menu.setBounds(menuX, y, menuWidth, height); // Menu tràn từ trên xuống dưới

                int menuButtonWidth = menuButton.getPreferredSize().width;
                int menuButtonHeight = menuButton.getPreferredSize().height;
                int menubX;
                if (ltr) {
                    menubX = (int) (x + menuWidth - (menuButtonWidth * (menu.isMenuFull() ? 0.5f : 0.3f)));
                } else {
                    menubX = (int) (menuX - (menuButtonWidth * (menu.isMenuFull() ? 0.5f : 0.7f)));
                }
                menuButton.setBounds(menubX, UIScale.scale(35), menuButtonWidth, menuButtonHeight); // Đặt button menu cách top 35px

                int gap = UIScale.scale(5);
                int bodyWidth = width - menuWidth - gap;
                int bodyHeight = height - UIScale.scale(5);
                int bodyx = ltr ? (x + menuWidth + gap) : x;
                int bodyy = UIScale.scale(5);
                panelBody.setBounds(bodyx, bodyy, bodyWidth, bodyHeight);
            }
        }
    }
}
