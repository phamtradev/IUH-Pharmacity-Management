# Template Import Sản Phẩm Nhập Hàng

File Excel cần có các cột sau (thứ tự không quan trọng):

## Các cột bắt buộc cho SẢN PHẨM:
- **Mã sản phẩm**: Mã SP hoặc số đăng ký
- **Số lượng**: Số lượng nhập
- **Đơn giá nhập**: Giá nhập

## Các cột tùy chọn cho SẢN PHẨM:
- **Tên sản phẩm**: (chỉ để tham khảo, không dùng để tìm kiếm)
- **Hạn sử dụng**: (định dạng ngày, nếu không có mặc định 2 năm sau)
- **Số lô** hoặc **Lô hàng**: Tên lô hàng (nếu có, hệ thống sẽ tự động tạo lô mới vào CSDL)

## Các cột tùy chọn cho NHÀ CUNG CẤP:
- **Mã NCC**: Mã nhà cung cấp (nếu đã tồn tại)
- **Tên NCC**: Tên nhà cung cấp (bắt buộc nếu muốn tạo mới)
- **Địa chỉ**: Địa chỉ nhà cung cấp
- **SĐT NCC**: Số điện thoại (định dạng 10 số, bắt đầu bằng 0)
- **Email**: Email nhà cung cấp
- **Mã số thuế**: Mã số thuế (10-13 ký tự)

## Ví dụ đầy đủ:

| Mã sản phẩm | Số lượng | Đơn giá nhập | Hạn sử dụng | Lô Hàng  | Mã NCC   | Tên NCC          | Địa chỉ      | SĐT NCC    | Email           | Mã số thuế       |
|-------------|----------|--------------|-------------|----------|----------|------------------|--------------|------------|-----------------|------------------|
| 123         | 50       | 22000        | 26/11/2027  | LH0026   | NCC0001  | trà              | trà vĩnh     | 069857833  | tra@gmail.com   | 0123456789-002   |

## Ví dụ chỉ có sản phẩm (không có nhà cung cấp):

| Mã sản phẩm | Tên sản phẩm      | Số lượng | Đơn giá nhập | Hạn sử dụng | Số lô    |
|-------------|-------------------|----------|--------------|-------------|----------|
| SP001       | Paracetamol 500mg | 100      | 15000        | 31/12/2026  | LOT-2024 |
| VD-123-45   | Amoxicillin 250mg | 50       | 25000        | 30/06/2027  | LOT-2025 |

## Lưu ý:
✓ Khi import, hệ thống sẽ:
  - **Đối với Sản phẩm**: 
    • Tự động tạo lô hàng mới vào database nếu lô chưa tồn tại
    • Hiển thị số lượng lô hàng mới được tạo
  - **Đối với Nhà cung cấp**: 
    • Nếu có Mã NCC: tìm theo mã, không tìm thấy thì tạo mới
    • Nếu có Tên NCC: tìm theo tên hoặc SĐT, không tìm thấy thì tạo mới
    • Tự động điền thông tin nhà cung cấp vào form sau khi import
    • Lưu nhà cung cấp mới vào database (lấy từ dòng đầu tiên)
  
⚠ **Lưu ý quan trọng**:
  - Thông tin nhà cung cấp chỉ lấy từ **dòng đầu tiên** (dòng 2 của Excel)
  - SĐT phải đúng định dạng: 10 số, bắt đầu bằng 0
  - Email phải đúng định dạng: example@domain.com
  - Mã số thuế: 10 số hoặc 13 ký tự dạng 0123456789-001

