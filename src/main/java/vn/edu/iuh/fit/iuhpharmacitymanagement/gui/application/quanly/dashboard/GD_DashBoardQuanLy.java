package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.dashboard;

import javax.swing.UIManager;

/**
 *
 * @author PhamTra
 */
public class GD_DashBoardQuanLy extends javax.swing.JPanel {

    public GD_DashBoardQuanLy() {
        initComponents();
        // Hiển thị trực tiếp Panel_TongQuan không cần tab
        add(new Panel_TongQuan());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createMatteBorder(0, 6, 0, 6, new java.awt.Color(255, 255, 255)));
        setMinimumSize(new java.awt.Dimension(1130, 800));
        setPreferredSize(new java.awt.Dimension(1130, 800));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));
    }// </editor-fold>//GEN-END:initComponents
}
