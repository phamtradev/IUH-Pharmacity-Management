package vn.edu.iuh.fit.iuhpharmacitymanagement.common;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Custom TableCellRenderer để hiển thị hình ảnh trong JTable
 */
public class ImageTableCellRenderer extends DefaultTableCellRenderer {
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        if (value instanceof ImageIcon) {
            JLabel label = new JLabel();
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);
            label.setIcon((ImageIcon) value);
            
            if (isSelected) {
                label.setBackground(new Color(0xF5F5F5));
                label.setOpaque(true);
            } else {
                label.setBackground(table.getBackground());
                label.setOpaque(false);
            }
            
            return label;
        }
        
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}

