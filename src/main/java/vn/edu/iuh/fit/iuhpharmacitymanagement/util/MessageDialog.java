/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.util;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author PhamTra
 */
public class MessageDialog {

    public static void info(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void warning(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Warning!", JOptionPane.WARNING_MESSAGE);
    }

    public static void error(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error!", JOptionPane.ERROR_MESSAGE);
    }

    public static boolean confirm(Component parent, String message, String title) {
        int result = JOptionPane.showConfirmDialog(parent, message, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.OK_OPTION;
    }

    public static String prompt(Component parent, String message, String title) {
        return JOptionPane.showInputDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

}
