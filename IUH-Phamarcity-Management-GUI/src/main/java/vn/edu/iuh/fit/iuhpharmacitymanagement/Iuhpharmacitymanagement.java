/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.staff.form.MenuForm;

/**
 *
 * @author PhamTra
 */
public class Iuhpharmacitymanagement {

    public static void main(String[] args) {
        try {
            // Đăng ký custom theme cho menu
            FlatLaf.registerCustomDefaultsSource("vn.edu.iuh.fit.iuhpharmacitymanagement.gui.staff.theme");
            FlatDarkLaf.setup();

            java.awt.EventQueue.invokeLater(() -> {
                new MenuForm().setVisible(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback to system look and feel
            try {
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
                java.awt.EventQueue.invokeLater(() -> {
                    new MenuForm().setVisible(true);
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
