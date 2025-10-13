/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.menu;

import com.formdev.flatlaf.util.UIScale;
import java.awt.*;
import javax.swing.JLabel;

/**
 * Layout manager for menu items
 *
 * @author PhamTra
 */
public class MenuItemLayout implements LayoutManager {

    private final Menu menu;

    public MenuItemLayout(Menu menu) {
        this.menu = menu;
    }

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
            int height = insets.top + insets.bottom;
            int size = parent.getComponentCount();
            for (int i = 0; i < size; i++) {
                Component com = parent.getComponent(i);
                if (com.isVisible()) {
                    if (com instanceof JLabel) {
                        if (menu.isMenuFull() || menu.isHideMenuTitleOnMinimum() == false) {
                            height += com.getPreferredSize().height + (UIScale.scale(menu.getMenuTitleVgap()) * 2);
                        }
                    } else {
                        height += com.getPreferredSize().height;
                    }
                }
            }
            return new Dimension(5, height);
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
            int width = parent.getWidth() - (insets.left + insets.right);
            int height = parent.getHeight() - (insets.top + insets.bottom);
            int size = parent.getComponentCount();
            
            // Tìm vị trí của glue component (Box.Filler)
            int glueIndex = -1;
            for (int i = 0; i < size; i++) {
                Component com = parent.getComponent(i);
                if (com instanceof javax.swing.Box.Filler) {
                    glueIndex = i;
                    break;
                }
            }
            
            // Nếu có glue, tính toán chiều cao của các component sau glue
            int bottomComponentsHeight = 0;
            if (glueIndex != -1) {
                for (int i = glueIndex + 1; i < size; i++) {
                    Component com = parent.getComponent(i);
                    if (com.isVisible()) {
                        bottomComponentsHeight += com.getPreferredSize().height;
                    }
                }
            }
            
            // Layout các component
            for (int i = 0; i < size; i++) {
                Component com = parent.getComponent(i);
                
                // Nếu gặp glue, nhảy xuống vị trí bottom
                if (i == glueIndex) {
                    y = insets.top + height - bottomComponentsHeight;
                    continue;
                }
                
                if (com.isVisible()) {
                    int comHeight = com.getPreferredSize().height;
                    if (com instanceof JLabel) {
                        if (menu.isMenuFull() || menu.isHideMenuTitleOnMinimum() == false) {
                            int menuTitleInset = UIScale.scale(menu.getMenuTitleLeftInset());
                            int menuTitleVgap = UIScale.scale(menu.getMenuTitleVgap());
                            int titleWidth = width - menuTitleInset;
                            y += menuTitleVgap;
                            com.setBounds(x + menuTitleInset, y, titleWidth, comHeight);
                            y += comHeight + menuTitleVgap;
                        } else {
                            com.setBounds(0, 0, 0, 0);
                        }
                    } else {
                        com.setBounds(x, y, width, comHeight);
                        y += comHeight;
                    }
                }
            }
        }
    }
}
