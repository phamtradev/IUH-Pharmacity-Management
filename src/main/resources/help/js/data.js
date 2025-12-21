const questionsData = {
  // Mẫu 1 thẻ (Nhân viên) dùng hình sẵn có trong thư mục img
  "Nhân viên": [
    {
      question: "Làm thế nào để thêm nhân viên?",
      answer: [
        { type: "text", content: "Mở chức năng Quản lý nhân viên" },
        { type: "image", content: "../img/QuanLyNhanVien_QuanLy.png" },
        {
          type: "text",
          content: "Chọn nút Thêm",
        },
        {
          type: "image",
          content: "../img/QuanLyNhanVien_ThemNhanVien_QuanLy.png",
        },
        {
          type: "text",
          content: "Nhập đầy đủ thông tin và nhấn Thêm",
        },
        {
          type: "image",
          content: "../img/QuanLyNhanVien_FormThemNhanVien_QuanLy.png",
        },
      ],
    },
    {
      question: "Làm thế nào để tìm kiếm nhân viên?",
      answer: [
        { type: "text", content: "Mở chức năng Quản lý nhân viên" },
        { type: "image", content: "../img/QuanLyNhanVien_QuanLy.png" },
        {
          type: "text",
          content:
            "Người dùng nhập thông tin của nhân viên cần tìm (tên, số điện thoại, email) và nhấn tìm kiếm",
        },
        {
          type: "image",
          content: "../img/QuanLyNhanVien_TimKiem_QuanLy.png",
        },
      ],
    },
    {
      question: "Đặt lại mật khẩu nhân viên?",
      answer: [
        {
          type: "text",
          content:
            "1. Chọn nhân viên cần đặt lại mật khẩu và chọn nút đặt lại mật khẩu",
        },
        {
          type: "image",
          content: "../img/QuanLyNhanVien_DatLaiMatKhau_QuanLy.png",
        },
        {
          type: "text",
          content: "Nhập mật khẩu mới và bấm xác nhận",
        },
        {
          type: "image",
          content: "../img/QuanLyNhanVien_FormDatLaiMatKhau_QuanLy.png",
        },
      ],
    },
    {
      question: "Làm thế nào để sửa thông tin nhân viên?",
      answer: [
        { type: "text", content: "Mở chức năng Quản lý nhân viên" },
        { type: "image", content: "../img/QuanLyNhanVien_QuanLy.png" },
        {
          type: "text",
          content: "Chọn nhân viên cần sửa và chọn nút Sửa",
        },
        { type: "image", content: "../img/QuanLyNhanVien_Sua_QuanLy.png" },
        {
          type: "text",
          content: "Nhập lại thông tin mới và nhấn sửa",
        },
        { type: "image", content: "../img/QuanLyNhanVien_FormSua_QuanLy.png" },
      ],
    },
  ],

  // XUẤT HUỶ
  "Xuất hủy": [
    {
      question:
        "Làm thế nào để nhân viên tạo phiếu xuất hủy khi người quản lý xử lý đơn trả hàng cần xuất hủy?",
      answer: [
        {
          type: "text",
          content: "Mở chức năng tạo phiếu xuất hủy ở giao diện nhân viên",
        },
        { type: "image", content: "../img/PhieuXuatHuy_NhanVien.png" },
        {
          type: "text",
          content:
            "Những đơn hàng xuất hủy được hiển thị ở danh sách thông tin xuất hủy, nhân viên nhấn nút tạo phiếu",
        },
        {
          type: "image",
          content: "../img/PhieuXuatHuy_TaoPhieu_NhanVien.png",
        },
        {
          type: "text",
          content:
            "Sau khi ấn tạo phiếu, hệ thống sẽ hiển thị phiếu xuất hủy, nhân viên có thể in phiếu xuất hủy nếu muốn",
        },
        {
          type: "image",
          content: "../img/PhieuXuatHuy_FormXuatHuy_NhanVien.png",
        },
      ],
    },
    {
      question:
        "Làm thế nào để người quản lý xuất hủy đơn trả hàng chưa xử lý?",
      answer: [
        {
          type: "text",
          content:
            "Quản lý chọn đơn hàng chưa xử lý ở mục quản lý đơn trả hàng",
        },
        {
          type: "image",
          content: "../img/DonTraHang_ChonDonHangChuaXuLy_QuanLy.png",
        },
        {
          type: "text",
          content: "Chọn nút xuất hủy ",
        },
        {
          type: "image",
          content: "../img/DonTraHang_NhanXuatHuy_QuanLy.png",
        },
        {
          type: "text",
          content:
            "Lúc này đơn trả hàng sẽ được xuất hủy ở màn hình của nhân viên ",
        },
        {
          type: "image",
          content: "../img/DonTraHang_XemDonTra_QuanLy.png",
        },
      ],
    },
    {
      question: "Làm thế nào để tìm kiếm đơn xuất hủy?",
      answer: [
        {
          type: "text",
          content:
            "Chọn thời gian cần tìm kiếm, nhập hoặc quét mã đơn xuất hủy",
        },
        {
          type: "image",
          content: "../img/QuanLyXuatHuy_NhapTimKiem_QuanLy.png",
        },
        {
          type: "text",
          content:
            "Nhấn nút Tìm kiếm và hệ thống sẽ hiển thị danh sách thông tin xuất hủy cần tìm",
        },
        {
          type: "image",
          content: "../img/QuanLyXuatHuy_TimKiem_QuanLy.png",
        },
      ],
    },
  ],
  // NHẬP HÀNG
  "Nhập hàng": [
    {
      question: "Làm thế nào để người quản lý tìm kiếm đơn nhập hàng?",
      answer: [
        {
          type: "text",
          content: "Mở chức năng quản lý nhập hàng",
        },
        { type: "image", content: "../img/QuanLyNhapHang_QuanLy.png" },
        {
          type: "text",
          content:
            "Chọn thời gian muốn tìm kiếm hoặc mã đơn hàng muốn tìm kiếm",
        },
        {
          type: "image",
          content: "../img/QuanLyNhapHang_TImKiem_QuanLy.png",
        },
        {
          type: "text",
          content: "nhấn nút Tìm kiếm",
        },
        {
          type: "image",
          content: "../img/QuanLyNhapHang_NhanTimKiem_QuanLy.png",
        },
      ],
    },
    {
      question: "Xuất file Excel danh sách đơn nhập hàng?",
      answer: [
        {
          type: "text",
          content: "Mở chức năng quản lý nhập hàng",
        },
        { type: "image", content: "../img/QuanLyNhapHang_QuanLy.png" },
        {
          type: "text",
          content: "chọn nút Xuất excel trên thanh công cụ ",
        },
        {
          type: "image",
          content: "../img/QuanLyNhapHang_XuatExcel_QuanLy.png",
        },
        {
          type: "text",
          content: "Chọn ví trí ổ đĩa lưu file excel phù hợp và nhấn save",
        },
        {
          type: "image",
          content: "../img/QuanLyNhapHang_ChonNoiLuuExcel_QuanLy.png",
        },
      ],
    },
    {
      question: "Làm thế nào để nhân viên lập phiếu nhập hàng?",
      answer: [
        {
          type: "text",
          content: "Mở chức năng phiếu lập hàng ở giao diện nhân viên",
        },
        {
          type: "image",
          content: "../img/PhieuNhapHang_NhanVien.png",
        },
        {
          type: "text",
          content:
            "Nhân viên nhập số đăng ký hoặc quét mã vạch hoặc import Excel",
        },
        {
          type: "image",
          content: "../img/PhieuNhapHang_SearchBar_NhanVien.png",
        },
        {
          type: "text",
          content: "Khi import Excel chọn file Excel cần import và nhấn open",
        },
        {
          type: "image",
          content: "../img/PhieuNhapHang_ChonFileExcel_NhanVien.png",
        },
        {
          type: "text",
          content: "Nhân viên nhấn chọn lô ",
        },
        {
          type: "image",
          content: "../img/PhieuNhapHang_ChonLo_NhanVien.png",
        },
        {
          type: "text",
          content: "Nhân viên chọn lô có sẵn hoặc tạo lô mới và nhấn xác nhận ",
        },
        {
          type: "image",
          content: "../img/PhieuNhapHang_ChonLoCoSan_NhanVien.png",
        },
        {
          type: "image",
          content: "../img/PhieuNhapHang_TaoLoMoi_NhanVien.png",
        },
        {
          type: "text",
          content: "Nhấn nút nhập hàng để nhập hàng",
        },
        {
          type: "image",
          content: "../img/PhieuNhapHang_NhapHang_NhanVien.png",
        },
      ],
    },
  ],
  //KHÁCH HÀNG
  "Khách hàng": [
    {
      question: "Làm thế nào để người quản lý hoặc nhân viên thêm khách hàng?",
      answer: [
        {
          type: "text",
          content:
            "Mở chức năng quản lý khách hàng (Chức năng này đều có ở quản lý và nhân viên và sẽ lấy vai trò là quản lý làm hướng dẫn mẫu)",
        },
        { type: "image", content: "../img/QuanLyKhachHang_QuanLy.png" },
        {
          type: "text",
          content: "Người dùng chọn chức năng Thêm",
        },
        {
          type: "image",
          content: "../img/QuanLyKhachHang_Them_QuanLy.png",
        },
        {
          type: "text",
          content:
            "Người dùng nhập đầy đủ thông tin vào biểu mẫu và nhấn nút thêm",
        },
        {
          type: "image",
          content: "../img/QuanLyKhachHang_FormThem_QuanLy.png",
        },
      ],
    },
    {
      question:
        "Hướng dẫn để người quản lý hoặc nhân viên sửa thông tin khách hàng?",
      answer: [
        {
          type: "text",
          content:
            "Mở chức năng quản lý khách hàng (Chức năng này đều có ở quản lý và nhân viên và sẽ lấy vai trò là quản lý làm hướng dẫn mẫu)",
        },
        { type: "image", content: "../img/QuanLyKhachHang_QuanLy.png" },
        {
          type: "text",
          content:
            "Người dùng chọn Khách hàng cần sửa thông tin và nhấn nút sửa",
        },
        {
          type: "image",
          content: "../img/QuanLyKhachHang_Sua_QuanLy.png",
        },
        {
          type: "text",
          content: "Người dùng chọn thông tin cần sửa để sửa và nhấn xác nhận",
        },
        {
          type: "image",
          content: "../img/QuanLyKhachHang_FormSua_QuanLy.png",
        },
      ],
    },
    {
      question:
        "Làm thế nào để người quản lý hoặc nhân viên tìm kiếm khách hàng?",
      answer: [
        {
          type: "text",
          content:
            "Mở chức năng quản lý khách hàng (Chức năng này đều có ở quản lý và nhân viên và sẽ lấy vai trò là quản lý làm hướng dẫn mẫu)",
        },
        { type: "image", content: "../img/QuanLyKhachHang_QuanLy.png" },
        {
          type: "text",
          content:
            "Người dùng nhập thông tin của Khách hàng cần tìm và nhấn nút tìm kiếm, thông tin khách hàng sẽ được hiển thị ở mục danh sách thông tin khách hàng",
        },
        {
          type: "image",
          content: "../img/QuanLyKhachHang_TimKiem_QuanLy.png",
        },
      ],
    },
  ],
  //QUẢN LÝ NHÀ CUNG CẤP
  "Nhà cung cấp": [
    {
      question:
        "Làm thế nào để người quản lý hoặc nhân viên thêm nhà cung cấp?",
      answer: [
        {
          type: "text",
          content:
            "Mở chức năng quản lý nhà cung cấp(Chức năng này đều có ở quản lý và nhân viên và sẽ lấy vai trò là quản lý làm hướng dẫn mẫu)",
        },
        { type: "image", content: "../img/QuanLyNhaCungCap_QuanLy.png" },
        {
          type: "text",
          content: "Người dùng chọn chức năng Thêm",
        },
        {
          type: "image",
          content: "../img/QuanLyNhaCungCap_Them_QuanLy.png",
        },
        {
          type: "text",
          content:
            "Người dùng nhập đầy đủ thông tin vào biểu mẫu và nhấn nút thêm",
        },
        {
          type: "image",
          content: "../img/QuanLyNhaCungCap_FormThem_QuanLy.png",
        },
      ],
    },
    {
      question:
        "Hướng dẫn để sửa thông tin nhà cung cấp với vai trò người quản lý hoặc nhân viên?",
      answer: [
        {
          type: "text",
          content:
            "Mở chức năng quản lý nhà cung cấp (Chức năng này đều có ở quản lý và nhân viên và sẽ lấy vai trò là quản lý làm hướng dẫn mẫu)",
        },
        { type: "image", content: "../img/QuanLyNhaCungCap_QuanLy.png" },
        {
          type: "text",
          content:
            "Người dùng chọn nhà cung cấp cần sửa thông tin và nhấn nút sửa",
        },
        {
          type: "image",
          content: "../img/QuanLyNhaCungCap_Sua_QuanLy.png",
        },
        {
          type: "text",
          content: "Người dùng chọn thông tin cần sửa để sửa và nhấn xác nhận",
        },
        {
          type: "image",
          content: "../img/QuanLyNhanVien_FormSua_QuanLy.png",
        },
      ],
    },
    {
      question:
        "Làm thế nào để người quản lý hoặc nhân viên tìm kiếm nhà cung cấp?",
      answer: [
        {
          type: "text",
          content:
            "Mở chức năng tạo quản lý nhà cung cấp (Chức năng này đều có ở quản lý và nhân viên và sẽ lấy vai trò là quản lý làm hướng dẫn mẫu)",
        },
        { type: "image", content: "../img/QuanLyNhaCungCap_QuanLy.png" },
        {
          type: "text",
          content:
            "Người dùng nhập thông tin của nhà cung cấp cần tìm và nhấn nút tìm kiếm, thông tin nhà cung cấp sẽ được hiển thị ở mục danh sách thông tin nhà cung cấp",
        },
        {
          type: "image",
          content: "../img/QuanLyNhanVien_TimKiem_QuanLy.png",
        },
      ],
    },
  ],
  //QUẢN LÝ ĐƠN VỊ TÍNH
  "Đơn vị tính": [
    {
      question: "Làm thế nào để người quản lý thêm đơn vị tính?",
      answer: [
        {
          type: "text",
          content: "Mở chức năng quản lý đơn vị tính",
        },
        { type: "image", content: "../img/QuanLyDonViTinh_QuanLy.png" },
        {
          type: "text",
          content: "Người dùng chọn chức năng Thêm",
        },
        {
          type: "image",
          content: "../img/QuanLyDonViTinh_Them_QuanLy.png",
        },
        {
          type: "text",
          content:
            "Người dùng nhập tên đơn vị tính vào biểu mẫu và nhấn nút thêm",
        },
        {
          type: "image",
          content: "../img/QuanLyDonViTinh_FormThem_QuanLy.png",
        },
      ],
    },
    {
      question: "Hướng dẫn để sửa thông tin đơn vị tính ?",
      answer: [
        {
          type: "text",
          content: "Mở chức năng quản lý đơn vị tính ",
        },
        { type: "image", content: "../img/QuanLyDonViTinh_QuanLy.png" },
        {
          type: "text",
          content:
            "Người dùng chọn đơn vị tính cần sửa thông tin và nhấn nút sửa",
        },
        {
          type: "image",
          content: "../img/QuanLyDonViTinh_Sua_QuanLy.png",
        },
        {
          type: "text",
          content: "Người dùng sửa lại thông tin và nhấn sửa",
        },
        {
          type: "image",
          content: "../img/QuanLyDonViTinh_FormSua_QuanLy.png",
        },
      ],
    },
    {
      question: "Làm thế nào để người quản lý tìm kiếm đơn vị tính?",
      answer: [
        {
          type: "text",
          content: "Mở chức năng tạo quản lý đơn vị tính ",
        },
        { type: "image", content: "../img/QuanLyDonViTinh_QuanLy.png" },
        {
          type: "text",
          content:
            "Người dùng nhập thông tin của đơn vị tính cần tìm và nhấn nút tìm kiếm, thông tin đơn vị tính sẽ được hiển thị ở mục danh sách thông tin đơn vị tính",
        },
        {
          type: "image",
          content: "../img/QuanLyDonViTinh_TimKiem_QuanLy.png",
        },
      ],
    },
  ],
  //QUẢN LÝ KHUYẾN MÃI
  "Khuyến mãi": [
    {
      question: "Làm thế nào để người quản lý thêm khuyến mãi?",
      answer: [
        {
          type: "text",
          content: "Mở chức năng quản lý khuyến mãi",
        },
        { type: "image", content: "../img/QuanLyKhuyenMai_QuanLy.png" },
        {
          type: "text",
          content: "Người dùng chọn chức năng Thêm",
        },
        {
          type: "image",
          content: "../img/QuanLyKhuyenMai_Them_QuanLy.png",
        },
        {
          type: "text",
          content:
            "Người dùng nhập thông tin khuyến mãi vào biểu mẫu và nhấn nút lưu",
        },
        {
          type: "image",
          content: "../img/QuanLyKhuyenMai_FormThem_QuanLy.png",
        },
      ],
    },
    {
      question: "Hướng dẫn để sửa thông tin khuyến mãi?",
      answer: [
        {
          type: "text",
          content: "Mở chức năng quản lý khuyến mãi ",
        },
        { type: "image", content: "../img/QuanLyKhuyenMai_QuanLy.png" },
        {
          type: "text",
          content:
            "Người dùng chọn chương trình khuyến mãi cần sửa thông tin và nhấn nút sửa",
        },
        {
          type: "image",
          content: "../img/QuanLyKhuyenMai_Sua_QuanLy.png",
        },
        {
          type: "text",
          content: "Người dùng sửa lại thông tin và nhấn cập nhập",
        },
        {
          type: "image",
          content: "../img/QuanLyKhuyenMai_FormSua_QuanLy.png",
        },
      ],
    },
    {
      question: "Xem chi tiết một chương trình khuyến mãi?",
      answer: [
        {
          type: "text",
          content: "Mở chức năng tạo quản lý khuyến mãi ",
        },
        { type: "image", content: "../img/QuanLyKhuyenMai_QuanLy.png" },
        {
          type: "text",
          content:
            "Người dùng nhập chọn chương trình khuyến mãi cần xem cần tìm và nhấn nút xem chi tiết, thông tin chương trình khuyến mãi sẽ hiển thi ra màn hình",
        },
        {
          type: "image",
          content: "../img/QuanLyKhuyenMai_XemChiTiet_QuanLy.png",
        },
        {
          type: "text",
          content: "                                        ",
        },
        {
          type: "image",
          content: "../img/QuanLyKhuyenMai_ThongTinChiTietKhuyenMai_QuanLy.png",
        },
      ],
    },
    {
      question: "Tìm kiếm chương trình khuyến mãi phải làm sao?",
      answer: [
        {
          type: "text",
          content: "Mở chức năng tạo quản lý khuyến mãi",
        },
        { type: "image", content: "../img/QuanLyKhuyenMai_QuanLy.png" },
        {
          type: "text",
          content:
            "Người dùng nhập thông tin của chương trình khuyến mãi cần tìm và nhấn nút tìm kiếm, thông tin khuyến mãi sẽ được hiển thị ở mục danh sách thông tin khuyến mãi",
        },
        {
          type: "image",
          content: "../img/QuanLyKhuyenMai_TimKiem.png",
        },
      ],
    },
  ],
  //TRẢ HÀNG
  "Trả hàng": [
    {
      question: "Làm thế nào để người quản lý xử lý đơn trả hàng?",
      answer: [
        {
          type: "text",
          content: "Mở chức năng quản lý trả hàng",
        },
        { type: "image", content: "../img/QuanLyTraHang_QuanLy.png" },
        {
          type: "text",
          content: "Người dùng chọn đơn trả hàng cần xử lý",
        },
        {
          type: "image",
          content: "../img/QuanLyTraHang_XuLyDonTraHang-ChonSanPham_QuanLy.png",
        },
        {
          type: "text",
          content: "Chọn nút Thêm vào kho",
        },
        {
          type: "image",
          content: "../img/QuanLyTraHang_XuLyDonTraHang-ThemVaoKho_QuanLy.png",
        },
        {
          type: "text",
          content:
            "Chọn yes thì đơn trả hàng sẽ được thêm vào kho và số lượng lô hàng trong kho sẽ được cập nhập lại",
        },
        {
          type: "image",
          content: "../img/QuanLyTraHang_XuLyDonTraHang-ThanhCong_QuanLy.png",
        },
        {
          type: "text",
          content: "Chọn no thì quay lại bước 1 và chọn nút xuất huỷ",
        },
        {
          type: "image",
          content: "../img/QuanLyTraHang_XuatHuy_QuanLy.png",
        },
        {
          type: "text",
          content:
            "Chọn yes thì đơn trả hàng sẽ được xuất huỷ ở màn hình phiếu xuất huỷ của nhân viên",
        },
        {
          type: "image",
          content: "../img/QuanLyTraHang_XuatHuy-ThanhCong_QuanLy.png",
        },
      ],
    },
    {
      question: "Xuất Excel đơn trả hàng?",
      answer: [
        {
          type: "text",
          content: "Mở chức năng quản lý trả hàng",
        },
        { type: "image", content: "../img/QuanLyTraHang_QuanLy.png" },
        {
          type: "text",
          content: "Người dùng chọn nút Xuất excel trên thanh công cụ",
        },
        {
          type: "image",
          content: "../img/QuanLyTraHang_XuatExcel_QuanLy.png",
        },
        {
          type: "text",
          content: "Chọn vị trí ổ đĩa lưu file excel phù hợp ",
        },
        {
          type: "image",
          content: "../img/QuanLyTraHang_XuatExcel-ChonViTriLuu_QuanLy.png",
        },
        {
          type: "text",
          content: "Nhấn Save",
        },
        {
          type: "image",
          content: "../img/QuanLyTraHang_XuatExcel-Save_QuanLy.png",
        },
      ],
    },
    {
      question: "Tìm kiếm đơn trả hàng phải làm sao?",
      answer: [
        {
          type: "text",
          content: "Mở chức năng tạo quản lý trả hàng",
        },
        { type: "image", content: "../img/QuanLyTraHang_QuanLy.png" },
        {
          type: "text",
          content:
            "Người dùng nhập thông tin của đơn trả hàng cần tìm và nhấn nút tìm kiếm, thông tin đơn trả hàng sẽ được hiển thị ở mục danh sách thông tin đơn trả hàng",
        },
        {
          type: "image",
          content: "../img/QuanLyXuatHuy_TimKiem_QuanLy.png",
        },
      ],
    },
    {
      question: "Làm sao để nhân viên lập phiếu trả hàng",
      answer: [
        {
          type: "text",
          content: "Mở chức năng phiếu trả hàng ở giao diện nhân viên",
        },
        { type: "image", content: "../img/PhieuTraHang_NhanVien.png" },
        {
          type: "text",
          content:
            "Nhân viên quét hoặc nhập hoá đơn khách muốn trả và nhấn tìm kiếm",
        },
        {
          type: "image",
          content: "../img/PhieuNhapHang_SearchBar_NhanVien.png",
        },
        {
          type: "text",
          content:
            "Màn hình lúc này hiển thị tất cả sản phẩm của hoá đơn nhân viên có thể chọn cụ thể sản phẩm muốn trả hoặc nhấn nút trả tất cả nếu muốn trả tất cả sản phẩm",
        },
        {
          type: "image",
          content: "../img/PhieuTraHang_ChonSanPhamTra_NhanVien.png",
        },
        {
          type: "text",
          content:
            "Nhân viên nhập lý do cho 1 sản phẩm hoặc nhập lý do cho tất cả ",
        },
        {
          type: "image",
          content: "../img/PhieuTraHang_LyDoTra_NhanVien.png",
        },
        {
          type: "text",
          content: "Sau khi nhập lý do, nhấn xác nhận và ấn nút tạo phiếu ",
        },
        {
          type: "image",
          content: "../img/PhieuTraHang_FormLyDoTra_NhanVien.png",
        },
        {
          type: "text",
          content: "Sau khi nhấn tạo phiếu sẽ hiển thị đơn trả hàng",
        },
        {
          type: "image",
          content: "../img/PhieuTraHang_XacNhanTraHang_NhanVien.png",
        },
      ],
    },
  ],
  "Bán hàng": [
    {
      question: "Làm thế nào để nhân viên bán hàng?",
      answer: [
        {
          type: "text",
          content: "Bước 1: Mở chức năng bán hàng ở giao diện nhân viên",
        },
        { type: "image", content: "../img/BanHang_NhanVien.png" },
        {
          type: "text",
          content:
            "Bước 2: Nhân viên quét mã vạch sản phẩm hoặc nhập số đăng ký sản phẩm vào thanh tìm kiếm",
        },
        {
          type: "text",
          content: 'Bước 3: Nhân viên nhấn nút "Thêm"',
        },
        {
          type: "text",
          content:
            "Bước 3.1: (Tùy chọn): Nhân viên nhấn dấu “+” nếu khách hàng mua hơn 1 sản phẩm",
        },
        {
          type: "text",
          content: "Bước 4: Nhân viên nhập số tiền khách đưa",
        },
        {
          type: "image",
          content: "../img/BanHang_Ban_NhanVien.png",
        },
        {
          type: "text",
          content: 'Bước 5: Nhấn "Bán hàng"',
        },
        {
          type: "image",
          content: "../img/BanHang_ThanhToan_NhanVien.png",
        },
        {
          type: "text",
          content:
            "Bước 5.1(Tùy chọn): Nhân viên nhấn dấu <span class='text-danger'>Xóa</span> nếu khách hàng không mua nữa.",
        },
      ],
    },
  ],
  //Thuốc
  Thuốc: [
    {
      question: "Làm thế nào để nhân viên thêm sản phẩm?",
      answer: [
        {
          type: "text",
          content: "Bước 1: Chọn nút “Thêm” trên thanh công cụ.",
        },
        {
          type: "image",
          content: "../img/themSanPhamB1.png",
        },
        {
          type: "text",
          content:
            "Bước 2: Người dùng nhập đầy đủ thông tin vào biểu mẫu.<br/>" +
            "Biểu mẫu cho phép nhập thông tin sản phẩm bao gồm:<br/>" +
            "• Tên sản phẩm<br/>" +
            "• Loại sản phẩm<br/>" +
            "• Số đăng ký<br/>" +
            "• Giá nhập<br/>" +
            "• Hoạt chất<br/>" +
            "• Giá bán<br/>" +
            "• Thuế VAT (%)<br/>" +
            "• Liều lượng<br/>" +
            "• Quy trình đóng gói<br/>" +
            "• Nhà sản xuất<br/>" +
            "• Đơn vị tính<br/>" +
            "• Xuất xứ",
        },
        {
          type: "text",
          content: 'Bước 3: Nhân viên nhấn nút "Thêm" để lưu sản phẩm.',
        },
        {
          type: "image",
          content: "../img/themSanPhamB2.png",
        },
        {
          type: "text",
          content:
            'Bước 3.1: Nhân viên nhấn nút "Thoát" nếu không muốn thêm sản phẩm mới nữa.',
        },
      ],
    },
    {
      question: "Làm thế nào để sửa sản phẩm?",
      answer: [
        {
          type: "text",
          content:
            "Bước 1: Người dùng chọn sản phẩm cần sửa thông tin (Khi chọn sản phẩm, ngay hàng của sản phẩm đó sẽ có màu xám nhạt hiện lên cho biết người dùng đang chọn sản phẩm nào).",
        },

        {
          type: "text",
          content: "Bước 2: Người dùng chọn chức năng “Sửa”.",
        },
        {
          type: "image",
          content: "../img/suaSPB1.png",
        },
        {
          type: "text",
          content: "Bước 3: Người dùng sửa thông tin cần thay đổi.",
        },
        {
          type: "text",
          content: "Bước 4: Nhấn “Sửa” để lưu thông tin mới.",
        },
        {
          type: "image",
          content: "../img/suaSPB2.png",
        },
        {
          type: "text",
          content:
            "Bước 4.1 (Tùy chọn): Người dùng nhấn “Thoát” nếu không muốn sửa thông tin nữa.",
        },
      ],
    },
    {
      question: "Làm thế nào để xem chi tiết sản phẩm?",
      answer: [
        {
          type: "text",
          content:
            "Bước 1: Người dùng chọn sản phẩm cần sửa thông tin (Khi chọn sản phẩm, ngay hàng của sản phẩm đó sẽ có màu xám nhạt hiện lên cho biết người dùng đang chọn sản phẩm nào).",
        },
        {
          type: "text",
          content: "Bước 2: Nhấn xem chi tiết",
        },
        {
          type: "image",
          content: "../img/xemCTSPb1.png",
        },
        {
          type: "text",
          content: "Bước 3: người dùng chọn đóng để quay lại.",
        },
        {
          type: "image",
          content: "../img/xemCTSPb2.png",
        },
      ],
    },
    {
      question: "Làm thế nào để xóa sản phẩm?",
      answer: [
        {
          type: "text",
          content:
            "Bước 1: Người dùng chọn sản phẩm cần sửa thông tin (Khi chọn sản phẩm, ngay hàng của sản phẩm đó sẽ có màu xám nhạt hiện lên cho biết người dùng đang chọn sản phẩm nào).",
        },
        {
          type: "text",
          content: "Bước 2: Nhấn xóa",
        },
        {
          type: "image",
          content: "../img/xoaSPB1.png",
        },
        {
          type: "text",
          content: "Bước 3: Người dùng xác nhận xóa sản phẩm đã chọn",
        },
        {
          type: "image",
          content: "../img/xoaSPB2.png",
        },
      ],
    },
    {
      question: "Làm thế nào để tìm sản phẩm?",
      answer: [
        {
          type: "text",
          content:
            "Bước 1: Người dùng nhập thông tin của sản phẩm cần tìm (tên, số đăng ký), chọn loại sản phẩm và trạng thái hoạt động của sản phẩm.",
        },
        {
          type: "text",
          content: "Bước 2: Nhấn tìm kiếm.",
        },
        {
          type: "text",
          content:
            "Bước 3: Kiểm tra dữ liệu trên bảng mà hệ thống đã lọc theo dữ liệu tìm kiếm của người dùng.",
        },
        {
          type: "image",
          content: "/img/timSP.png",
        },
      ],
    },
  ],
  "Giá bán": [
    {
      question: "Làm thế nào để phát hành hay chỉnh sửa giá bán sản phẩm?",
      answer: [
        {
          type: "text",
          content:
            "Bước 1: Chọn chức năng Quản lý giá bán từ menu Quản lý bán hàng.",
        },
        { type: "image", content: "../img/giaBanB1.png" },
        {
          type: "text",
          content: "Bước 2: Chọn theo loại sản phảm ",
        },
        {
          type: "text",
          content:
            "Bước 3: Nhập các thông tin cần thiết cho giá: giá nhập, lãi,...",
        },
        {
          type: "image",
          content: "../img/giaBanB2.png",
        },
        {
          type: "text",
          content: "Bước 4: Chọn sản phẩm cần phát hành",
        },
        {
          type: "image",
          content: "../img/giaBanB3.png",
        },
      ],
    },
  ],
  //Lô hàng
  "Lô hàng": [
    // {
    //   question: "Làm thế nào để người quản lý sửa lô hàng tính?",
    //   answer: [
    //     {
    //       type: "text",
    //       content: "Mở chức năng quản lý lô hàng",
    //     },
    //     { type: "image", content: "../img/suaLoHangB1.png" },
    //     {
    //       type: "text",
    //       content: "Người dùng chọn chức năng Thêm",
    //     },
    //     {
    //       type: "image",
    //       content: "../img/QuanLyDonViTinh_Them_QuanLy.png",
    //     },
    //     {
    //       type: "text",
    //       content:
    //         "Người dùng nhập tên đơn vị tính vào biểu mẫu và nhấn nút thêm",
    //     },
    //     {
    //       type: "image",
    //       content: "../img/QuanLyDonViTinh_FormThem_QuanLy.png",
    //     },
    //   ],
    // },
    {
      question: "Hướng dẫn để sửa thông tin lô hàng ?",
      answer: [
        {
          type: "text",
          content: "Mở chức năng quản lý lô hàng ",
        },
        { type: "image", content: "../img/suaLoHangB1.png" },
        {
          type: "text",
          content: "Người dùng chọn lô hàng cần sửa thông tin và nhấn nút sửa",
        },
        {
          type: "image",
          content: "../img/suaLoHangB2.png",
        },
        {
          type: "text",
          content: "Người dùng sửa lại thông tin và cập nhật",
        },
        {
          type: "image",
          content: "../img/suaLoHangB3.png",
        },
      ],
    },
    {
      question: "Làm thế nào để người quản lý tìm kiếm Lô hàng?",
      answer: [
        {
          type: "text",
          content: "Mở chức năng tạo quản lý lô hàng ",
        },
        { type: "image", content: "../img/suaLoHangB1.png" },
        {
          type: "text",
          content:
            "Người dùng chọn trạng thái, nhập thông tin của lô hàng cần tìm và nhấn nút tìm kiếm, thông tin lô hàng sẽ được hiển thị ở mục danh sách thông tin lô hàng",
        },
        {
          type: "image",
          content: "../img/timLoHang.png",
        },
      ],
    },
    {
      question: "Làm thế nào để người quản lý xóa Lô hàng?",
      answer: [
        {
          type: "text",
          content: "Mở chức năng tạo quản lý lô hàng ",
        },
        { type: "image", content: "../img/suaLoHangB1.png" },
        {
          type: "text",
          content: "Chon lô hàng cần xóa và nhấn nút xóa",
        },
        {
          type: "image",
          content: "../img/xoaLoHangB1.png",
        },
        {
          type: "text",
          content: "Xác nhận xóa lô hàng",
        },
        {
          type: "image",
          content: "../img/xoaLoHangB2.png",
        },
      ],
    },
  ],
  //đơn hàng
  //Lô hàng
  "Đơn hàng": [
    {
      question: "Hướng dẫn để xem chi tiết đơn hàng?",
      answer: [
        {
          type: "text",
          content: "Mở chức năng quản đơn hàng ",
        },
        { type: "image", content: "../img/donHangB1.png" },
        {
          type: "text",
          content:
            "Người dùng chọn đơn hàng cần xem thông tin và nhấn nút xem chi tiết",
        },
        {
          type: "image",
          content: "../img/xemDonHangB2.png",
        },
      ],
    },
    {
      question: "Làm thế nào để người quản lý tìm kiếm Đơn hàng?",
      answer: [
        {
          type: "text",
          content: "Mở chức năng tạo quản lý đơn hàng ",
        },
        { type: "image", content: "../img/donHangB1.png" },
        {
          type: "text",
          content:
            "Người dùng chọn thời gian, nhập thông tin của đơn hàng cần tìm và nhấn nút tìm kiếm, thông tin đơn hàng sẽ được hiển thị ở mục danh sách thông tin đơn hàng",
        },
        {
          type: "image",
          content: "../img/timDonHangB1.png",
        },
      ],
    },
    {
      question: "Làm thế nào để người quản lý xuất file excel đơn hàng?",
      answer: [
        {
          type: "text",
          content: "Mở chức năng tạo quản lý đơn hàng ",
        },
        { type: "image", content: "../img/donHangB1.png" },
        {
          type: "text",
          content:
            "Chon đơn hàng cần cần xuất file excel và nhấn nút xuất excel, chọn nơi lưu file excel",
        },
        {
          type: "image",
          content: "../img/xuatExcel.png",
        },
      ],
    },
  ],
  "Thông tin": [
    {
      question: "Xem thông tin phần mềm?",
      answer: [
        {
          type: "text",
          content: `
            <div >
              <h2 style="text-align: center; font-weight: bold; margin-bottom: 0.5rem;">
                PHẦN MỀM QUẢN LÝ BÁN THUỐC
              </h2>

              <p style="text-align: center; font-size: 1.2rem; margin-bottom: 1.5rem;">
                Trường Đại học Công nghiệp TP. Hồ Chí Minh
              </p>

              <div>
                <p><strong>Phiên bản:</strong> RELEASE 1.0</p>
                <p><strong>Giảng viên hướng dẫn:</strong> ThS. Trần Thị Anh Thi</p>
                <p><strong>Lớp học phần:</strong> DHKTMP19A</p>
              </div>

              <div >
                <strong>Nhóm phát triển:</strong>
                <ul>
                  <li>Phạm Văn Trà</li>
                  <li>Đỗ Hoài Nhớ</li>
                  <li>Tô Nguyễn An Thuyên</li>
                  <li>Nguyễn Công Tuyến</li>
                  <li>Phạm Minh Thịnh</li>
                </ul>
              </div>
            </div>
          `,
        },
      ],
    },
  ],
  "Sao lưu": [
    {
      question: "Làm thế nào để sao lưu dữ liệu?",
      answer: [
        {
          type: "text",
          content: "Bước 1: Quản lý vào chức năng quản lý dữ liệu",
        },
        { type: "image", content: "../img/saoLuuB1.png" },
        {
          type: "text",
          content: "Bước 2: Chọn nơi lưu trữ file dữ liệu và chọn sao lưu ngay",
        },
        {
          type: "text",
          content: "Bước 3: Có thể chọn tự động sao lưu theo thời gian",
        },
        {
          type: "image",
          content: "../img/saoLuuB2.png",
        },
      ],
    },
  ],
  "Phục hồi": [
    {
      question: "Phục hồi dữ liệu từ các file đã sao lưu trước đó",
      answer: [
        {
          type: "text",
          content: "Bước 1: Quản lý vào chức năng quản lý dữ liệu",
        },
        { type: "image", content: "../img/saoLuuB1.png" },
        {
          type: "text",
          content:
            "Bước 2: Chọn file dữ liệu đã sao lưu trước đó và chọn khôi phục từ file",
        },
        {
          type: "image",
          content: "../img/phucHoiB2.png",
        },
      ],
    },
    {
      question: "Phục hồi dữ liệu từ bằng key ở trang đăng nhập",
      answer: [
        {
          type: "text",
          content: "Tại trang đăng nhập, chọn khôi phục dữ liệu",
        },
        { type: "image", content: "../img/phucHoiB1.1.png" },
        {
          type: "text",
          content:
            "Bước 2: Nhập key phục hồi dữ liệu và chọn file phù hợp khôi phục dữ liệu",
        },
        {
          type: "image",
          content: "../img/phucHoiB2.2.png",
        },
      ],
    },
  ],
};
