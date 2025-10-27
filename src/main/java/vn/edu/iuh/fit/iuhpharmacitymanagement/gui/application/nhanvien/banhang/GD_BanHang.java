/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.banhang;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Optional;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import raven.toast.Notifications;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.SanPhamBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.SanPhamDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;

/**
 *
 * @author PhamTra
 */
public class GD_BanHang extends javax.swing.JPanel {

    static int transactionNumber = 1;
    private SanPhamBUS sanPhamBUS;
    private Panel_DonHang panelDonHang;

    /**
     * Creates new form LapHoaDonForm
     */
    public GD_BanHang() {
        // Khởi tạo BUS
        sanPhamBUS = new SanPhamBUS(new SanPhamDAO());
        
        lookAndFeelSet();
        initComponents();
        customUI();
        addPanelThanhToan();
        addHeaderRow();
    }

    private void addPanelThanhToan() {
        // Tạo panel wrapper để panel thanh toán chỉ nằm ngang với phần giữa
        javax.swing.JPanel wrapperPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        wrapperPanel.setBackground(java.awt.Color.WHITE);

        panelDonHang = new Panel_DonHang();
        
        // Thêm listener để lắng nghe thay đổi khuyến mãi
        panelDonHang.addPropertyChangeListener("khuyenMaiChanged", evt -> {
            // Khi khuyến mãi thay đổi, cập nhật lại tổng tiền
            capNhatTongTien();
        });
        
        wrapperPanel.add(pnMi, java.awt.BorderLayout.CENTER);
        wrapperPanel.add(panelDonHang, java.awt.BorderLayout.EAST);

        // Xóa pnMi khỏi vị trí cũ và thêm wrapper
        remove(pnMi);
        add(wrapperPanel, java.awt.BorderLayout.CENTER);
    }

    private void addHeaderRow() {
        // Tạo panel header với GridBagLayout giống HỆT Panel_ChiTietSanPham
        javax.swing.JPanel headerPanel = new javax.swing.JPanel();
        headerPanel.setBackground(new java.awt.Color(245, 245, 245));
        headerPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(200, 200, 200)));
        headerPanel.setMaximumSize(new java.awt.Dimension(32767, 50));
        headerPanel.setMinimumSize(new java.awt.Dimension(800, 50));
        headerPanel.setPreferredSize(new java.awt.Dimension(1000, 50));

        // Sử dụng GridBagLayout với constraints giống Panel_ChiTietSanPham
        headerPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        gbc.insets = new java.awt.Insets(10, 8, 10, 8); // Giống Panel_ChiTietSanPham
        gbc.gridy = 0;
        gbc.weighty = 1.0;

        // 1. Hình ảnh - 80x100px (giống lblHinh)
        javax.swing.JLabel lblHeaderImg = new javax.swing.JLabel("Hình ảnh");
        lblHeaderImg.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderImg.setPreferredSize(new java.awt.Dimension(80, 30));
        lblHeaderImg.setMinimumSize(new java.awt.Dimension(80, 30));
        lblHeaderImg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderImg, gbc);

        // 2. Tên sản phẩm - 180px (giống lblTenSP)
        javax.swing.JLabel lblHeaderName = new javax.swing.JLabel("Tên sản phẩm");
        lblHeaderName.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderName.setPreferredSize(new java.awt.Dimension(180, 30));
        lblHeaderName.setMinimumSize(new java.awt.Dimension(180, 30));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        headerPanel.add(lblHeaderName, gbc);

        // 3. Lô hàng - 100px (giống jLabel1)
        javax.swing.JLabel lblHeaderBatch = new javax.swing.JLabel("Lô hàng");
        lblHeaderBatch.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderBatch.setPreferredSize(new java.awt.Dimension(100, 30));
        lblHeaderBatch.setMinimumSize(new java.awt.Dimension(100, 30));
        lblHeaderBatch.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderBatch, gbc);

        // 4. Số lượng - 150px (giống pnSpinner với nút +/-)
        javax.swing.JLabel lblHeaderQty = new javax.swing.JLabel("Số lượng");
        lblHeaderQty.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderQty.setPreferredSize(new java.awt.Dimension(150, 30));
        lblHeaderQty.setMinimumSize(new java.awt.Dimension(150, 30));
        lblHeaderQty.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 3;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderQty, gbc);

        // 5. Giảm giá - 70px (giống txtDiscount)
        javax.swing.JLabel lblHeaderDiscount = new javax.swing.JLabel("Giảm giá");
        lblHeaderDiscount.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderDiscount.setPreferredSize(new java.awt.Dimension(70, 30));
        lblHeaderDiscount.setMinimumSize(new java.awt.Dimension(70, 30));
        lblHeaderDiscount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 4;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderDiscount, gbc);

        // 6. Đơn giá - 100px (giống txtDonGia)
        javax.swing.JLabel lblHeaderPrice = new javax.swing.JLabel("Đơn giá");
        lblHeaderPrice.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderPrice.setPreferredSize(new java.awt.Dimension(100, 30));
        lblHeaderPrice.setMinimumSize(new java.awt.Dimension(100, 30));
        lblHeaderPrice.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        gbc.gridx = 5;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderPrice, gbc);

        // 7. Tổng tiền - 120px (giống txtTongTien)
        javax.swing.JLabel lblHeaderTotal = new javax.swing.JLabel("Tổng tiền");
        lblHeaderTotal.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderTotal.setPreferredSize(new java.awt.Dimension(120, 30));
        lblHeaderTotal.setMinimumSize(new java.awt.Dimension(120, 30));
        lblHeaderTotal.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        gbc.gridx = 6;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderTotal, gbc);

        // 8. Chức năng - 70px (giống pnXoa)
        javax.swing.JLabel lblHeaderAction = new javax.swing.JLabel("Chức năng");
        lblHeaderAction.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderAction.setPreferredSize(new java.awt.Dimension(70, 30));
        lblHeaderAction.setMinimumSize(new java.awt.Dimension(70, 30));
        lblHeaderAction.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 7;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderAction, gbc);

        containerPanel.add(headerPanel);
    }

    private void customUI() {
        txtTimSanPham.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Vui lòng quét mã vạch hoặc nhập số đăng ký");
        // Thêm viền cho text field
        txtTimSanPham.putClientProperty(FlatClientProperties.OUTLINE, "default");
        txtTimSanPham.putClientProperty(FlatClientProperties.STYLE,
                "arc:8;"
                + "borderWidth:1;"
                + "borderColor:#CCCCCC"
        );

        // Style cho button Thêm
        btnMa.putClientProperty(FlatClientProperties.STYLE,
                "arc:8;"
                + "borderWidth:0;"
                + "focusWidth:0"
        );

        // Style cho button Xóa trắng
        btnXoa.putClientProperty(FlatClientProperties.STYLE,
                "arc:8;"
                + "borderWidth:0;"
                + "focusWidth:0"
        );
    }

    private void lookAndFeelSet() {
        UIManager.put("TextComponent.arc", 8);
        UIManager.put("Component.arc", 8);
        UIManager.put("Button.arc", 10);
        UIManager.put("TabbedPane.selectedBackground", Color.white);
        UIManager.put("TabbedPane.tabHeight", 45);
        UIManager.put("ToggleButton.selectedBackground", new Color(81, 154, 244));
        UIManager.put("ToggleButton.selectedForeground", Color.WHITE);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnMi = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        txtTimSanPham = new javax.swing.JTextField();
        btnMa = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        scrollPaneProducts = new javax.swing.JScrollPane();
        containerPanel = new javax.swing.JPanel();

        setBackground(new java.awt.Color(0, 0, 0));
        setLayout(new java.awt.BorderLayout());

        pnMi.setBackground(new java.awt.Color(204, 204, 204));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(204, 204, 204)));
        jPanel1.setToolTipText("");

        txtTimSanPham.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimSanPham.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTimSanPhamKeyPressed(evt);
            }
        });

        btnMa.setBackground(new java.awt.Color(115, 165, 71));
        btnMa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnMa.setForeground(new java.awt.Color(255, 255, 255));
        btnMa.setText("Thêm");
        btnMa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMaActionPerformed(evt);
            }
        });

        btnXoa.setBackground(new java.awt.Color(220, 53, 69));
        btnXoa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnXoa.setForeground(new java.awt.Color(255, 255, 255));
        btnXoa.setText("Xóa trắng");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(txtTimSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMa, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtTimSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                    .addComponent(btnMa, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnXoa, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setMinimumSize(new java.awt.Dimension(400, 300));
        jPanel2.setPreferredSize(new java.awt.Dimension(600, 500));
        jPanel2.setLayout(new java.awt.BorderLayout());

        // Container panel để chứa các Panel_ChiTietSanPham
        containerPanel.setBackground(new java.awt.Color(255, 255, 255));
        containerPanel.setLayout(new javax.swing.BoxLayout(containerPanel, javax.swing.BoxLayout.Y_AXIS));

        scrollPaneProducts.setViewportView(containerPanel);
        scrollPaneProducts.setBorder(null);
        scrollPaneProducts.getVerticalScrollBar().setUnitIncrement(16);

        jPanel2.add(scrollPaneProducts, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout pnMiLayout = new javax.swing.GroupLayout(pnMi);
        pnMi.setLayout(pnMiLayout);
        pnMiLayout.setHorizontalGroup(
            pnMiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnMiLayout.setVerticalGroup(
            pnMiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        add(jPanel1, java.awt.BorderLayout.NORTH);
        add(pnMi, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnMaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMaActionPerformed
        timSanPham();
    }//GEN-LAST:event_btnMaActionPerformed

    private void txtTimSanPhamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimSanPhamKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            timSanPham();
        }
    }//GEN-LAST:event_txtTimSanPhamKeyPressed
    
    /**
     * Tìm sản phẩm theo số đăng ký
     */
    private void timSanPham() {
        String soDangKy = txtTimSanPham.getText().trim();
        
        // Kiểm tra input rỗng
        if (soDangKy.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_CENTER, 
                "Vui lòng nhập số đăng ký sản phẩm");
            txtTimSanPham.requestFocus();
            return;
        }
        
        // Tìm sản phẩm theo số đăng ký
        Optional<SanPham> sanPhamOpt = sanPhamBUS.timSanPhamTheoSoDangKy(soDangKy);
        
        if (sanPhamOpt.isPresent()) {
            SanPham sanPham = sanPhamOpt.get();
            
            // Kiểm tra sản phẩm có đang hoạt động không
            if (!sanPham.isHoatDong()) {
                Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_CENTER, 
                    "Sản phẩm '" + sanPham.getTenSanPham() + "' đã ngưng bán");
                return;
            }
            
            // Thêm sản phẩm vào giỏ hàng
            themSanPhamVaoGioHang(sanPham);
            
            // Thông báo thành công
            Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_CENTER, 
                "Đã thêm sản phẩm: " + sanPham.getTenSanPham());
            
            // Xóa text field và focus
            txtTimSanPham.setText("");
            txtTimSanPham.requestFocus();
        } else {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, 
                "Không tìm thấy sản phẩm với số đăng ký: " + soDangKy);
            txtTimSanPham.selectAll();
            txtTimSanPham.requestFocus();
        }
    }
    
    /**
     * Thêm sản phẩm vào giỏ hàng (container panel)
     */
    private void themSanPhamVaoGioHang(SanPham sanPham) {
        // Tạo panel chi tiết sản phẩm với dữ liệu thực
        Panel_ChiTietSanPham panelChiTiet = new Panel_ChiTietSanPham(sanPham);
        
        // Thêm listener để cập nhật tổng tiền khi có thay đổi
        panelChiTiet.addPropertyChangeListener("tongTien", evt -> capNhatTongTien());
        
        containerPanel.add(panelChiTiet);
        containerPanel.revalidate();
        containerPanel.repaint();
        
        // Cập nhật tổng tiền ngay sau khi thêm
        capNhatTongTien();
    }
    
    /**
     * Cập nhật tổng tiền đơn hàng từ tất cả các sản phẩm
     */
    public void capNhatTongTien() {
        double tongTienHang = 0;
        double tongGiamGiaSanPham = 0;
        double giamGiaHoaDon = 0;
        
        // Thu thập danh sách sản phẩm và số lượng trong giỏ hàng
        java.util.Map<vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham, Integer> danhSachSanPham = 
            new java.util.HashMap<>();
        
        // Duyệt qua tất cả các Panel_ChiTietSanPham để tính tổng tiền và thu thập sản phẩm
        for (Component comp : containerPanel.getComponents()) {
            if (comp instanceof Panel_ChiTietSanPham) {
                Panel_ChiTietSanPham panel = (Panel_ChiTietSanPham) comp;
                tongTienHang += panel.getTongTien();
                
                // Thêm vào danh sách sản phẩm
                danhSachSanPham.put(panel.getSanPham(), panel.getSoLuong());
                
                // Reset giảm giá sản phẩm về 0
                panel.setGiamGia(0);
            }
        }
        
        // Tự động tìm và áp dụng khuyến mãi tốt nhất
        panelDonHang.tuDongApDungKhuyenMai(tongTienHang, danhSachSanPham);
        
        // Lấy khuyến mãi đã chọn từ Panel_DonHang (sau khi tự động áp dụng)
        vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhuyenMai khuyenMai = panelDonHang.getKhuyenMaiDaChon();
        
        // Tính giảm giá dựa trên khuyến mãi được áp dụng
        for (Component comp : containerPanel.getComponents()) {
            if (comp instanceof Panel_ChiTietSanPham) {
                Panel_ChiTietSanPham panel = (Panel_ChiTietSanPham) comp;
                
                // Nếu có khuyến mãi sản phẩm, tính giảm giá
                if (khuyenMai != null && khuyenMai.getLoaiKhuyenMai() == vn.edu.iuh.fit.iuhpharmacitymanagement.constant.LoaiKhuyenMai.SAN_PHAM) {
                    // Kiểm tra xem sản phẩm có trong chương trình khuyến mãi không
                    java.util.List<vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietKhuyenMaiSanPham> danhSachCTKM = 
                        panelDonHang.getChiTietKhuyenMaiSanPhamBUS().timTheoMaKhuyenMai(khuyenMai.getMaKhuyenMai());
                    
                    for (vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietKhuyenMaiSanPham ctkm : danhSachCTKM) {
                        if (ctkm.getSanPham().getMaSanPham().equals(panel.getSanPham().getMaSanPham())) {
                            // Sản phẩm có trong chương trình khuyến mãi
                            double giamGia = panel.getTongTien() * (khuyenMai.getGiamGia() / 100.0);
                            tongGiamGiaSanPham += giamGia;
                            // Cập nhật giảm giá cho panel (hiển thị % giảm giá)
                            panel.setGiamGia(khuyenMai.getGiamGia());
                            break;
                        }
                    }
                }
            }
        }
        
        // Nếu có khuyến mãi hóa đơn, tính giảm giá
        if (khuyenMai != null && khuyenMai.getLoaiKhuyenMai() == vn.edu.iuh.fit.iuhpharmacitymanagement.constant.LoaiKhuyenMai.DON_HANG) {
            giamGiaHoaDon = tongTienHang * (khuyenMai.getGiamGia() / 100.0);
        }
        
        // Cập nhật vào Panel_DonHang
        if (panelDonHang != null) {
            panelDonHang.updateTongTienHang(tongTienHang);
            panelDonHang.updateDiscountProduct(tongGiamGiaSanPham);
            panelDonHang.updateDiscountOrder(giamGiaHoaDon);
        }
    }

    private JPanel createTabTitle(JTabbedPane tabbedPane, String title, Component tabComponent) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);

        // Label hiển thị tên tab
        JLabel label = new JLabel(title);
        panel.add(label);

        // Nút close
        JButton closeButton = new JButton("x");
        closeButton.setMargin(new Insets(0, 1, 0, 0));
        closeButton.setPreferredSize(new Dimension(15, 15));

        // Hành động khi nhấn nút close
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = tabbedPane.indexOfComponent(tabComponent);
                if (index != -1 && tabbedPane.getTabCount() != 1) {
                    tabbedPane.remove(index);  // Xóa tab tương ứng
                }
            }
        });

        panel.add(Box.createRigidArea(new Dimension(5, 0)));
        panel.add(closeButton);

        return panel;
    }

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {
        // Xóa nội dung text field và đặt focus
        txtTimSanPham.setText("");
        txtTimSanPham.requestFocus();
        Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đã xóa trắng thành công !");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMa;
    private javax.swing.JButton btnXoa;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel pnMi;
    private javax.swing.JScrollPane scrollPaneProducts;
    private javax.swing.JPanel containerPanel;
    private javax.swing.JTextField txtTimSanPham;
    // End of variables declaration//GEN-END:variables

}
