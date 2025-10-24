package vn.edu.iuh.fit.iuhpharmacitymanagement.util;

import javax.swing.*;
import java.awt.*;

/**
 * Utility class để hiển thị các dialog thông báo với giao diện đẹp hơn
 * 
 * @author PhamTra
 */
public class MessageDialog {
    
    /**
     * Hiển thị dialog thông báo thành công
     * 
     * @param parent Component cha
     * @param message Nội dung thông báo
     */
    public static void info(Component parent, String message) {
        JOptionPane.showMessageDialog(
            parent,
            message,
            "Thông báo",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Hiển thị dialog cảnh báo
     * 
     * @param parent Component cha
     * @param message Nội dung cảnh báo
     */
    public static void warning(Component parent, String message) {
        JOptionPane.showMessageDialog(
            parent,
            message,
            "Cảnh báo",
            JOptionPane.WARNING_MESSAGE
        );
    }
    
    /**
     * Hiển thị dialog lỗi
     * 
     * @param parent Component cha
     * @param message Nội dung lỗi
     */
    public static void error(Component parent, String message) {
        JOptionPane.showMessageDialog(
            parent,
            message,
            "Lỗi",
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    /**
     * Hiển thị dialog xác nhận (Yes/No)
     * 
     * @param parent Component cha
     * @param message Nội dung câu hỏi
     * @return true nếu người dùng chọn Yes, false nếu chọn No
     */
    public static boolean confirm(Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(
            parent,
            message,
            "Xác nhận",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        return result == JOptionPane.YES_OPTION;
    }
    
    /**
     * Hiển thị dialog xác nhận với tiêu đề tùy chỉnh
     * 
     * @param parent Component cha
     * @param message Nội dung câu hỏi
     * @param title Tiêu đề dialog
     * @return true nếu người dùng chọn Yes, false nếu chọn No
     */
    public static boolean confirm(Component parent, String message, String title) {
        int result = JOptionPane.showConfirmDialog(
            parent,
            message,
            title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        return result == JOptionPane.YES_OPTION;
    }
}
