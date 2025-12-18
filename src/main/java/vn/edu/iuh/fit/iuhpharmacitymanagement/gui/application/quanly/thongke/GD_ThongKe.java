package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.thongke;

import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.dashboard.Panel_ThongKeTheoThoiGian;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.dashboard.Panel_ThongKeThueLai;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.dashboard.Panel_XuHuongMuaHang;

import javax.swing.*;

/**
 * Giao diện Thống kê cho quản lý
 * @author PhamTra
 */
public class GD_ThongKe extends JPanel {

    private JTabbedPane tabbedStats;

    public GD_ThongKe() {
        UIManager.put("TabbedPane.tabHeight", 40);
        initComponents();
        tabbedStats.add("Thống kê theo thời gian", new Panel_ThongKeTheoThoiGian());
        tabbedStats.add("Thống kê sản phẩm", new Panel_XuHuongMuaHang());
        tabbedStats.add("Thống kê Thuế & Lãi", new Panel_ThongKeThueLai());
        tabbedStats.setSelectedIndex(0);
    }

    private void initComponents() {
        tabbedStats = new JTabbedPane();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(BorderFactory.createMatteBorder(0, 6, 0, 6, new java.awt.Color(255, 255, 255)));
        setMinimumSize(new java.awt.Dimension(1130, 800));
        setPreferredSize(new java.awt.Dimension(1130, 800));
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        tabbedStats.setBackground(new java.awt.Color(255, 255, 255));
        tabbedStats.setFont(new java.awt.Font("Segoe UI", 0, 16));
        add(tabbedStats);
    }
}

