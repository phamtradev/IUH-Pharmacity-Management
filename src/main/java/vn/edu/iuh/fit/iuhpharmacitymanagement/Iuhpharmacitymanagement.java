/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLaf;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.staff.form.MenuForm;

/**
 *
 * @author PhamTra
 */
public class Iuhpharmacitymanagement {

    public static void main(String[] args) {
        FlatLaf.registerCustomDefaultsSource("vn.edu.iuh.fit.iuhpharmacitymanagement.gui.staff.theme");
        FlatDarculaLaf.setup();
        java.awt.EventQueue.invokeLater(() -> {
            new MenuForm().setVisible(true);
        });
    }
}
