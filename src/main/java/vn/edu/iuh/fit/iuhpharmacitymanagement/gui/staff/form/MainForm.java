/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.staff.form;

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
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.staff.form.other.FormInbox;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.staff.form.other.FormRead;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.staff.menu.Menu;

/**
 *
 * @author PhamTra
 */
public class MainForm extends JLayeredPane {

    public MainForm() {
        init();
    }

    private void init() {
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(new MainFormLayout());
        menu = new Menu();
        panelBody = new JPanel(new BorderLayout());
        initMenuArrowIcon();
        menuButton.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.button.background;"
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
        String icon = (getComponentOrientation().isLeftToRight()) ? "menu_left.svg" : "menu_right.svg";
        FlatSVGIcon svgIcon = new FlatSVGIcon("icon/svg/" + icon, 0.8f);
        FlatSVGIcon.ColorFilter colorFilter = new FlatSVGIcon.ColorFilter();
        // Light mode: icon tối, Dark mode: icon sáng
        colorFilter.add(java.awt.Color.decode("#969696"),
                       java.awt.Color.decode("#404040"), // Màu tối cho light mode
                       java.awt.Color.decode("#E0E0E0")); // Màu sáng cho dark mode
        svgIcon.setColorFilter(colorFilter);
        menuButton.setIcon(svgIcon);
    }

//    private void initMenuEvent() {
//        menu.addMenuEvent((int index, int subIndex, MenuAction action) -> {
//            // Application.mainForm.showForm(new DefaultForm("Form : " + index + " " + subIndex));
//            if (index == 0) {
//                MenuForm.showForm(new FormDashboard());
//            } else if (index == 1) {
//                if (subIndex == 1) {
//                    MenuForm.showForm(new FormInbox());
//                } else if (subIndex == 2) {
//                    MenuForm.showForm(new FormRead());
//                } else {
//                    action.cancel();
//                }
//            } else if (index == 9) {
//                MenuForm.logout();
//            } else {
//                action.cancel();
//            }
//        });
//    }
    private void setMenuFull(boolean full) {
        String icon;
        if (getComponentOrientation().isLeftToRight()) {
            icon = (full) ? "menu_left.svg" : "menu_right.svg";
        } else {
            icon = (full) ? "menu_right.svg" : "menu_left.svg";
        }
        FlatSVGIcon svgIcon = new FlatSVGIcon("icon/svg/" + icon, 0.8f);
        FlatSVGIcon.ColorFilter colorFilter = new FlatSVGIcon.ColorFilter();
        // Light mode: icon tối, Dark mode: icon sáng
        colorFilter.add(java.awt.Color.decode("#969696"),
                       java.awt.Color.decode("#404040"), // Màu tối cho light mode
                       java.awt.Color.decode("#E0E0E0")); // Màu sáng cho dark mode
        svgIcon.setColorFilter(colorFilter);
        menuButton.setIcon(svgIcon);
        menu.setMenuFull(full);
        revalidate();
    }

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
                int y = insets.top;
                int width = parent.getWidth() - (insets.left + insets.right);
                int height = parent.getHeight() - (insets.top + insets.bottom);
                int menuWidth = UIScale.scale(menu.isMenuFull() ? menu.getMenuMaxWidth() : menu.getMenuMinWidth());
                int menuX = ltr ? x : x + width - menuWidth;
                menu.setBounds(menuX, y, menuWidth, height);
                int menuButtonWidth = menuButton.getPreferredSize().width;
                int menuButtonHeight = menuButton.getPreferredSize().height;
                int menubX;
                if (ltr) {
                    menubX = (int) (x + menuWidth - (menuButtonWidth * (menu.isMenuFull() ? 0.5f : 0.3f)));
                } else {
                    menubX = (int) (menuX - (menuButtonWidth * (menu.isMenuFull() ? 0.5f : 0.7f)));
                }
                menuButton.setBounds(menubX, UIScale.scale(30), menuButtonWidth, menuButtonHeight);
                int gap = UIScale.scale(5);
                int bodyWidth = width - menuWidth - gap;
                int bodyHeight = height;
                int bodyx = ltr ? (x + menuWidth + gap) : x;
                int bodyy = y;
                panelBody.setBounds(bodyx, bodyy, bodyWidth, bodyHeight);
            }
        }
    }
}
