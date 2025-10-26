/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.form;

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
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.menu.Menu;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.menu.MenuAction;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.banhang.GD_BanHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.quanlykhachhang.GD_QuanLyKhachHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.quanlynhacungcap.GD_QuanLyNhaCungCap;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.quanlyphieunhaphang.GD_QuanLyPhieuNhapHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.quanlyphieutrahang.GD_QuanLyPhieuTraHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.quanlysanpham.GD_QuanLySanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.quanlyxuathuy.GD_QuanLyXuatHuy;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlynhaphang.GD_QuanLyNhapHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlytrahang.GD_QuanLyTraHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlydonhang.GD_QuanLyDonHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlykhuyenmai.GD_QuanLyKhuyenMai;

/**
 *
 * @author PhamTra
 */
public class MainForm extends JLayeredPane {

    private int type = 2;

    public MainForm() {
        init();
    }

    private void init() {
        setBorder(new EmptyBorder(0, 0, 0, 0));
        setLayout(new MainFormLayout());
        setBackground(java.awt.Color.WHITE);
        putClientProperty(FlatClientProperties.STYLE, ""
                + "background:#FFFFFF;"
                + "border:0,0,0,0");
        menu = new Menu(type); // Sử dụng type thực tế thay vì hard-code
        panelBody = new JPanel(new BorderLayout());
        panelBody.setBackground(java.awt.Color.WHITE);
        // Loại bỏ viền xám
        panelBody.setBorder(null);
        panelBody.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:#FFFFFF;"
                + "border:0,0,0,0");
        initMenuArrowIcon();
        menuButton.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:#D3D3D3;"
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
        initMenuEvent();

        // Hiển thị giao diện chào mừng tương ứng với type khi khởi động
        if (type == 1) {
            // Nhân viên - Hiển thị giao diện chào mừng
            showForm(new WelcomeFormNhanVien());
        } else {
            // Quản lý - Hiển thị giao diện chào mừng
            showForm(new WelcomeFormQuanLy());
        }
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

        String iconFile = getComponentOrientation().isLeftToRight()
                ? "menu_left.svg"
                : "menu_right.svg";

        // Load từ resources/img/
        URL iconUrl = getClass().getResource("/img/" + iconFile);
        FlatSVGIcon svgIcon = new FlatSVGIcon(iconUrl);

        // Icon màu đen
        FlatSVGIcon.ColorFilter colorFilter = new FlatSVGIcon.ColorFilter();
        colorFilter.add(java.awt.Color.decode("#969696"), java.awt.Color.BLACK);
        svgIcon.setColorFilter(colorFilter);
        menuButton.setIcon(svgIcon);
    }

    private void initMenuEvent() {
        menu.addMenuEvent((int index, int subIndex, MenuAction action) -> {
            // Application.mainForm.showForm(new DefaultForm("Form : " + index + " " + subIndex));
            if (type == 1) {
                // Type 1 = Nhân viên (index từ 20-30)
                if (index == 20) {
                    showForm(new GD_BanHang());
                } else if (index == 21) {
                    showForm(new GD_QuanLySanPham());
                } else if (index == 22) {
                    showForm(new GD_QuanLyKhachHang());
                } else if (index == 23) {
                    showForm(new GD_QuanLyNhaCungCap());
                } else if (index == 24) {
                    showForm(new GD_QuanLyPhieuNhapHang());
                } else if (index == 25) {
                    showForm(new GD_QuanLyPhieuTraHang());
                } else if (index == 26) {
                    showForm(new GD_QuanLyXuatHuy());
                } else if (index == 27) {
                    // Thống kê cá nhân (Dashboard nhân viên)
                    showForm(new vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.dashboard.GD_DashBoard());
//                } else if (index == 28) {
//                    showForm(new TABPersonalInformation());
//                } else if (index == 29) {
//                    showForm(new ViewPdfPanel(1));
//                } else if (index == 30) { 
//                    Application.logout();
                } else {
                    action.cancel();
                }
            } else if (type == 2) {
                // Type 2 = Quản lý (index từ 0-14)
                if (index == 0) {
                    // Thống kê (Dashboard quản lý)
                    showForm(new vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.dashboard.GD_DashBoardQuanLy());
//                } else if (index == 1) {
//                    showForm(new TABReport());
                } else if (index == 2) {
                    // Quản lý sản phẩm (quản lý có button xóa)
                    showForm(new GD_QuanLySanPham(true));
                } else if (index == 3) {
                    // Quản lý đơn vị tính
                    showForm(new vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlydonvitinh.GD_QuanLyDonViTinh());
                } else if (index == 4) {
                    // Quản lý nhân viên
                    showForm(new vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlynhanvien.GD_QuanLyNhanVien());
                } else if (index == 5) {
                    // Quản lý khách hàng (quản lý có button xóa)
                    showForm(new vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlykhachhang.GD_QuanLyKhachHang());
                } else if (index == 6) {
                    // Quản lý nhà cung cấp (quản lý)
                    showForm(new vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlynhacungcap.GD_QuanLyNhaCungCap());
                } else if (index == 7) {
                    // Quản lý nhập hàng
                    showForm(new GD_QuanLyNhapHang());
                } else if (index == 8) {
                    // Quản lý trả hàng
                    showForm(new GD_QuanLyTraHang());
                } else if (index == 9) {
                    // Quản lý xuất hủy
                    showForm(new vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlyxuathuy.GD_QuanLyXuatHuy());
                } else if (index == 10) {
                    // Quản lý đơn hàng
                    showForm(new GD_QuanLyDonHang());
                } else if (index == 11) {
                    // Quản lý khuyến mãi
                    showForm(new GD_QuanLyKhuyenMai());
//                } else if (index == 12) {
//                    showForm(new TABPersonalInformation());
//                } else if (index == 13) {
//                    showForm(new ViewPdfPanel(2));
//                } else if (index == 14) { 
//                    Application.logout();
                } else {
                    action.cancel();
                }
            }

        });
    }

    private void setMenuFull(boolean full) {
        String icon;
        if (getComponentOrientation().isLeftToRight()) {
            icon = (full) ? "menu_left.svg" : "menu_right.svg";
        } else {
            icon = (full) ? "menu_right.svg" : "menu_left.svg";
        }

        URL iconUrl = getClass().getResource("/img/" + icon);
        FlatSVGIcon svgIcon = new FlatSVGIcon(iconUrl);

        // tạo scaled thay cho FlatSVGIcon svgIcon = new FlatSVGIcon(iconPath, 0.8f); để bt tải icon thành công hay k
        FlatSVGIcon scaledIcon = svgIcon.derive(0.8f);

        // ok
        FlatSVGIcon.ColorFilter colorFilter = new FlatSVGIcon.ColorFilter();
        colorFilter.add(java.awt.Color.decode("#969696"), java.awt.Color.BLACK);
        scaledIcon.setColorFilter(colorFilter);

        menuButton.setIcon(scaledIcon);
        menu.setMenuFull(full);
        revalidate();
    }

//    private void setMenuFull(boolean full) {
//    String icon;
//    if (getComponentOrientation().isLeftToRight()) {
//        icon = (full) ? "menu_left.svg" : "menu_right.svg";
//    } else {
//        icon = (full) ? "menu_right.svg" : "menu_left.svg";
//    }
//    
//            // Load từ resources/img/
//        URL iconUrl = getClass().getResource("/img/" + icon);
//
//    //thử coi lỗi ở đâu mà k load hình
//    
//    URL resourceUrl = getClass().getResource(iconUrl);
//    
//    if (resourceUrl == null) {
//        System.err.println("sai cmnr");
//    } else {
//        System.out.println("adu ngon" + resourceUrl.toExternalForm());
//    }
//
//    FlatSVGIcon svgIcon = new FlatSVGIcon(iconPath, 0.8f);
//    FlatSVGIcon.ColorFilter colorFilter = new FlatSVGIcon.ColorFilter();
//    // Icon màu đen
//    colorFilter.add(java.awt.Color.decode("#969696"), java.awt.Color.BLACK);
//    svgIcon.setColorFilter(colorFilter);
//    menuButton.setIcon(svgIcon);
//    menu.setMenuFull(full);
//    revalidate();
//}
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
                int y = 0; // Menu bắt đầu từ y = 0 để tràn lên trên
                int width = parent.getWidth() - (insets.left + insets.right);
                int height = parent.getHeight(); // Sử dụng toàn bộ chiều cao
                int menuWidth = UIScale.scale(menu.isMenuFull() ? menu.getMenuMaxWidth() : menu.getMenuMinWidth());
                int menuX = ltr ? x : x + width - menuWidth;
                menu.setBounds(menuX, y, menuWidth, height); // Menu tràn từ trên xuống dưới

                int menuButtonWidth = menuButton.getPreferredSize().width;
                int menuButtonHeight = menuButton.getPreferredSize().height;
                int menubX;
                if (ltr) {
                    menubX = (int) (x + menuWidth - (menuButtonWidth * (menu.isMenuFull() ? 0.5f : 0.3f)));
                } else {
                    menubX = (int) (menuX - (menuButtonWidth * (menu.isMenuFull() ? 0.5f : 0.7f)));
                }
                menuButton.setBounds(menubX, UIScale.scale(35), menuButtonWidth, menuButtonHeight); // Đặt button menu cách top 35px

                int gap = UIScale.scale(5);
                int bodyWidth = width - menuWidth - gap;
                int bodyHeight = height - UIScale.scale(5);
                int bodyx = ltr ? (x + menuWidth + gap) : x;
                int bodyy = UIScale.scale(5);
                panelBody.setBounds(bodyx, bodyy, bodyWidth, bodyHeight);
            }
        }
    }
}
