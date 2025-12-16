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
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.net.URI;
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
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.thongtinnhanvien.GD_ThongTinCaNhan;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlynhaphang.GD_QuanLyNhapHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlytrahang.GD_QuanLyTraHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlydonhang.GD_QuanLyDonHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlykhuyenmai.GD_QuanLyKhuyenMai;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlylohang.GD_QuanLyLoHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlygiaban.GD_QuanLyGiaBan;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlythuchi.GD_BaoCaoThuChi;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlydulieu.GD_QuanLyDuLieu;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.chatbot.GD_ChatBot;

/**
 *
 * @author PhamTra
 */
public class MainForm extends JLayeredPane {

    private int type = 1;//1 nv 2 ql

    public MainForm() {
        init();
    }

    public MainForm(int type) {
        this.type = type;
        this.menu = new Menu(type);
        init();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
        add(menu);
        add(panelBody);
        add(menuButton);
        initMenuEvent();

        // Hiển thị giao diện chào mừng tương ứng với type khi khởi động
        if (getType() == 1) {
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
            try {
                if (getType() == 1) {
                    // Type 1 = Nhân viên (index từ 20-32)
                    if (index == 20) {
                        // Dashboard (Thống kê cá nhân) - Vị trí đầu tiên
                        showForm(new vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.dashboard.GD_DashBoard());
                    } else if (index == 21) {
                        showForm(new GD_BanHang());
                    } else if (index == 22) {
                        showForm(new GD_QuanLySanPham());
                    } else if (index == 23) {
                        showForm(new GD_QuanLyKhachHang());
                    } else if (index == 24) {
                        showForm(new GD_QuanLyNhaCungCap());
                    } else if (index == 25) {
                        showForm(new GD_QuanLyPhieuNhapHang());
                    } else if (index == 26) {
                        showForm(new GD_QuanLyPhieuTraHang());
                    } else if (index == 27) {
                        showForm(new GD_QuanLyXuatHuy());
                    } else if (index == 28) {
                        showForm(new GD_ThongTinCaNhan(false));
                    } else if (index == 29) {
                        // Nhóm: Hỗ trợ người dùng
                        if (subIndex == 1) {
                            // AI trợ giúp
                            showForm(new GD_ChatBot());
                        } else if (subIndex == 2) {
                            // Trợ giúp: mở trang hướng dẫn người dùng
                            openHelpPage();
                        } else {
                            action.cancel();
                        }
                    } else if (index == 30) {
                        // Phiên bản
                        javax.swing.JOptionPane.showMessageDialog(
                                this,
                                "PHẦN MỀM QUẢN LÝ BÁN THUỐC TRƯỜNG ĐẠI HỌC CÔNG NGHIỆP THÀNH PHỐ HỒ CHÍ MINH\n\n"
                                + "Phiên Bản: RELEASE 1.0\n"
                                + "Giảng viên hướng dẫn: ThS. Trần Thị Anh Thi\n"
                                + "Lớp học phần: DHKTPM19A\n\n"
                                + "Nhóm phát triển:\n"
                                + "- Phạm Văn Trà\n"
                                + "- Đỗ Hoài Nhớ\n"
                                + "- Tô Nguyễn An Thuyên\n"
                                + "- Nguyễn Công Tuyến\n"
                                + "- Phạm Minh Thịnh",
                                "Thông tin Phiên bản",
                                javax.swing.JOptionPane.INFORMATION_MESSAGE
                        );
                    } else if (index == 31) {
                        // Đăng xuất nhân viên
                        handleLogout();
                    } else {
                        action.cancel();
                    }
                } else if (getType() == 2) {
                    // Type 2 = Quản lý
                    // Menu đã được gom nhóm lại:
                    // 0: Dashboard
                    // 1: Báo cáo thu chi
                    // 2: Quản lý bán hàng (subIndex 1..6)
                    // 3: Quản lý danh mục (subIndex 1..6)
                    // 4: Quản lý dữ liệu
                    // 5: Thông tin cá nhân
                    // 6: Hỗ trợ người dùng (subIndex 1..2)
                    // 7: Phiên bản
                    // 8: Đăng xuất
                    if (index == 0) {
                        // Dashboard
                        showForm(new vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.dashboard.GD_DashBoardQuanLy());
                    } else if (index == 1) {
                        // Báo cáo thu chi
                        showForm(new GD_BaoCaoThuChi());
                    } else if (index == 2) {
                        // Nhóm: Quản lý bán hàng
                        if (subIndex == 1) {
                            // Quản lý đơn hàng
                            showForm(new GD_QuanLyDonHang());
                        } else if (subIndex == 2) {
                            // Quản lý trả hàng
                            showForm(new GD_QuanLyTraHang());
                        } else if (subIndex == 3) {
                            // Quản lý xuất hủy
                            showForm(new vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlyxuathuy.GD_QuanLyXuatHuy());
                        } else if (subIndex == 4) {
                            // Quản lý nhập hàng
                            showForm(new GD_QuanLyNhapHang());
                        } else if (subIndex == 5) {
                            // Quản lý khuyến mãi
                            showForm(new GD_QuanLyKhuyenMai());
                        } else if (subIndex == 6) {
                            // Quản lý giá bán
                            showForm(new GD_QuanLyGiaBan());
                        } else {
                            action.cancel();
                        }
                    } else if (index == 3) {
                        // Nhóm: Quản lý danh mục
                        if (subIndex == 1) {
                            // Quản lý sản phẩm (quản lý có button xóa)
                            showForm(new GD_QuanLySanPham(true));
                        } else if (subIndex == 2) {
                            // Quản lý đơn vị tính
                            showForm(new vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlydonvitinh.GD_QuanLyDonViTinh());
                        } else if (subIndex == 3) {
                            // Quản lý lô hàng
                            showForm(new GD_QuanLyLoHang());
                        } else if (subIndex == 4) {
                            // Quản lý nhà cung cấp (quản lý)
                            showForm(new vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlynhacungcap.GD_QuanLyNhaCungCap());
                        } else if (subIndex == 5) {
                            // Quản lý khách hàng (quản lý có button xóa)
                            showForm(new vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlykhachhang.GD_QuanLyKhachHang());
                        } else if (subIndex == 6) {
                            // Quản lý nhân viên
                            showForm(new vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlynhanvien.GD_QuanLyNhanVien());
                        } else {
                            action.cancel();
                        }
                    } else if (index == 4) {
                        // Quản lý dữ liệu
                        showForm(new GD_QuanLyDuLieu());
                    } else if (index == 5) {
                        // Thông tin cá nhân
                        showForm(new GD_ThongTinCaNhan(true));
                    } else if (index == 6) {
                        // Nhóm: Hỗ trợ người dùng
                        if (subIndex == 1) {
                            // AI trợ giúp
                            showForm(new GD_ChatBot());
                        } else if (subIndex == 2) {
                            // Trợ giúp: mở trang hướng dẫn người dùng
                            openHelpPage();
                        } else {
                            action.cancel();
                        }
                    } else if (index == 7) {
                        // Phiên bản
                        javax.swing.JOptionPane.showMessageDialog(
                                this,
                                "PHẦN MỀM QUẢN LÝ BÁN THUỐC TRƯỜNG ĐẠI HỌC CÔNG NGHIỆP THÀNH PHỐ HỒ CHÍ MINH\n\n"
                                + "Phiên Bản: RELEASE 1.0\n"
                                + "Giảng viên hướng dẫn: ThS. Trần Thị Anh Thi\n"
                                + "Lớp học phần: DHKTPM19A\n\n"
                                + "Nhóm phát triển:\n"
                                + "- Phạm Văn Trà\n"
                                + "- Đỗ Hoài Nhớ\n"
                                + "- Tô Nguyễn An Thuyên\n"
                                + "- Nguyễn Công Tuyến\n"
                                + "- Phạm Minh Thịnh",
                                "Thông tin Phiên bản",
                                javax.swing.JOptionPane.INFORMATION_MESSAGE
                        );
                    } else if (index == 8) {
                        // Đăng xuất quản lý
                        handleLogout();
                    } else {
                        action.cancel();
                    }
                }
            } catch (Exception e) {
                System.err.println("ĐÃ XẢY RA LỖI NGHIÊM TRỌNG KHI TẠO FORM:");
                e.printStackTrace();

                // Hiển thị lỗi cho người dùng
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Không thể mở form. Đã xảy ra lỗi:\n" + e.getMessage(),
                        "Lỗi Giao Diện",
                        javax.swing.JOptionPane.ERROR_MESSAGE
                );
            }

        });
    }

    // Xử lý đăng xuất
    private void handleLogout() {
        int confirm = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn đăng xuất?",
                "Xác nhận đăng xuất",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            // Xóa session người dùng
            vn.edu.iuh.fit.iuhpharmacitymanagement.util.UserSession.getInstance().logout();

            // Tìm MenuForm chứa MainForm này và đóng nó
            javax.swing.SwingUtilities.invokeLater(() -> {
                java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
                if (window != null) {
                    window.dispose();
                }

                // Mở lại LoginFrame
                try {
                    vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.login.LoginFrame loginFrame
                            = new vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.login.LoginFrame();
                    loginFrame.setVisible(true);

                    raven.toast.Notifications.getInstance().show(
                            raven.toast.Notifications.Type.SUCCESS,
                            "Đăng xuất thành công!"
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
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
        // Thu gọn menu về trạng thái tối thiểu (giống khi người dùng bấm nút toggle)
        setMenuFull(false);
    }

    /**
     * Mở trang hướng dẫn người dùng trong trình duyệt mặc định.
     */
    private void openHelpPage() {
        try {
            URI uri = new URI("https://iuh-pharmacity-user-guide.vercel.app/");
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(uri);
            } else {
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Không thể mở trình duyệt trên máy này.\nVui lòng truy cập thủ công: https://iuh-pharmacity-user-guide.vercel.app/",
                        "Không thể mở trình duyệt",
                        javax.swing.JOptionPane.WARNING_MESSAGE
                );
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Không thể mở trang trợ giúp.\nVui lòng truy cập: https://iuh-pharmacity-user-guide.vercel.app/",
                    "Lỗi khi mở trang trợ giúp",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }
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
