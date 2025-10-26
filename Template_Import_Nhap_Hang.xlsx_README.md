# Template Import Nhập Hàng

## Cấu trúc file Excel

File Excel để import nhập hàng cần có cấu trúc như sau:

### Header (Dòng 1):
| Mã Sản Phẩm | Số Lượng | Đơn Giá Nhập | Hạn Sử Dụng | Lô Hàng | Mã NCC | Tên NCC | Địa Chỉ NCC | SĐT NCC | Email NCC | Mã Số Thuế |
|-------------|----------|--------------|-------------|---------|--------|---------|-------------|---------|-----------|------------|

### Dữ liệu (Từ dòng 2 trở đi):
| Mã Sản Phẩm | Số Lượng | Đơn Giá Nhập | Hạn Sử Dụng | Lô Hàng | Mã NCC | Tên NCC | Địa Chỉ NCC | SĐT NCC | Email NCC | Mã Số Thuế |
|-------------|----------|--------------|-------------|---------|--------|---------|-------------|---------|-----------|------------|
| SP00001     | 100      | 50000        | 31/12/2025  | LOT001  | NCC0001 | Công ty ABC | 123 Đường XYZ | 0901234567 | abc@company.com | 0123456789 |
| SP00002     | 200      | 75000        | 30/06/2026  | LOT002  | NCC0002 | Công ty XYZ | 456 Đường ABC | 0912345678 | xyz@company.com | 9876543210 |

## Lưu ý:
1. **Mã Sản Phẩm**: Phải tồn tại trong hệ thống (có thể dùng mã sản phẩm hoặc số đăng ký)
2. **Số Lượng**: Số nguyên dương
3. **Đơn Giá Nhập**: Số thực, đơn vị VNĐ
4. **Hạn Sử Dụng**: Định dạng ngày dd/MM/yyyy hoặc định dạng ngày Excel
5. **Lô Hàng**: Tên lô hàng (không bắt buộc). Nếu để trống, có thể chọn lô sau khi import
6. **Thông tin Nhà Cung Cấp**: (Cột 5-10)
   - **Mã NCC**: Mã nhà cung cấp (không bắt buộc - nếu để trống sẽ tự sinh mã)
   - **Tên NCC**: Tên nhà cung cấp (bắt buộc)
   - **Địa Chỉ NCC**: Địa chỉ nhà cung cấp (bắt buộc)
   - **SĐT NCC**: Số điện thoại nhà cung cấp (bắt buộc - định dạng 0xxxxxxxxx, 10 số)
   - **Email NCC**: Email nhà cung cấp (bắt buộc - định dạng email hợp lệ)
   - **Mã Số Thuế**: Mã số thuế (bắt buộc - 10 số hoặc 13 ký tự dạng 0123456789-001)

## Cách sử dụng:
1. Mở file Excel template
2. Điền thông tin sản phẩm theo đúng format
3. Lưu file với định dạng .xlsx
4. Trong giao diện phiếu nhập hàng, click nút "Import Excel"
5. Chọn file Excel vừa tạo
6. Hệ thống sẽ tự động import và hiển thị kết quả

## Cách hoạt động của cột "Lô Hàng":
- **Nếu điền tên lô tồn tại**: Hệ thống sẽ tự động tìm lô hàng theo tên và hiển thị đầy đủ thông tin (Lô, HSD, Tồn) trên button ✅
- **Nếu tên lô không tồn tại**: Hệ thống sẽ **tự động tạo lô mới** với:
  - Tên lô: theo cột "Lô Hàng" trong Excel
  - Hạn sử dụng: theo cột "Hạn Sử Dụng"
  - Tồn kho: theo cột "Số Lượng"
  - Trạng thái: Đang hoạt động
  - Lô mới sẽ được lưu vào database và hiển thị trong **Quản lý lô hàng** 🆕
- **Nếu để trống**: Button hiển thị "Chọn lô", người dùng có thể click để chọn lô sau 📋
- **Tìm kiếm không phân biệt chữ hoa/thường**: "LOT001", "lot001", "Lot001" đều giống nhau

## Ví dụ dữ liệu:

```
Mã Sản Phẩm | Số Lượng | Đơn Giá | Hạn SD      | Lô Hàng | Mã NCC   | Tên NCC        | Địa Chỉ         | SĐT NCC    | Email           | Mã Số Thuế
SP00001     | 100      | 50000   | 31/12/2025  | LOT001  | NCC0001  | Công ty ABC    | 123 Đường XYZ   | 0901234567 | abc@company.com | 0123456789
SP00002     | 200      | 75000   | 30/06/2026  | LOT002  |          | Công ty XYZ    | 456 Đường ABC   | 0912345678 | xyz@company.com | 9876543210-001
SP00003     | 150      | 120000  | 15/03/2027  |         | NCC0001  | Công ty ABC    | 123 Đường XYZ   | 0901234567 | abc@company.com | 0123456789
SP00004     | 300      | 85000   | 20/08/2026  | LOT999  |          | Công ty DEF    | 789 Đường MNO   | 0923456789 | def@company.com | 1234567890

Giải thích:
- Dòng 1 (SP00001): Lô đã tồn tại, NCC đã tồn tại (có mã NCC0001)
- Dòng 2 (SP00002): Lô đã tồn tại, NCC mới (không có mã NCC, sẽ tự tạo)
- Dòng 3 (SP00003): Không có lô, NCC đã tồn tại (dùng lại NCC0001)
- Dòng 4 (SP00004): Lô mới (TỰ ĐỘNG TẠO), NCC mới (TỰ ĐỘNG TẠO)
```

## Kết quả sau khi Import:

1. **SP00001 & SP00002**: Button sẽ hiển thị thông tin như:
   ```
   Lô: LOT001 | HSD: 31/12/2025 | Tồn: 100
   ```

2. **SP00003**: Button hiển thị "Chọn lô", người dùng có thể click để chọn lô từ danh sách

3. **SP00004**: Hệ thống tự động tạo lô mới "LOT999" với:
   - Tên lô: LOT999
   - HSD: 20/08/2026
   - Tồn kho: 300
   - Lô này được lưu vào database và có thể xem trong **Quản lý lô hàng**
   - Button hiển thị đầy đủ thông tin lô mới tạo 🆕

## Lưu ý quan trọng:

### Về Sản phẩm:
- Nếu có sản phẩm không tồn tại trong hệ thống, hệ thống sẽ báo lỗi và bỏ qua sản phẩm đó

### Về Lô hàng:
- **Tính năng tự động tạo lô mới**: Khi import Excel, nếu tên lô chưa tồn tại, hệ thống sẽ tự động tạo lô mới và lưu vào database
- Lô mới tạo sẽ có:
  - Mã lô: Tự động sinh (LHxxxxx)
  - Tên lô: Theo cột "Lô Hàng" trong Excel
  - HSD: Theo cột "Hạn Sử Dụng"
  - Tồn kho: Theo cột "Số Lượng"
  - Trạng thái: Đang hoạt động
- Lô hàng phải thuộc về sản phẩm tương ứng (không thể dùng lô của sản phẩm A cho sản phẩm B)
- Sau khi import, vẫn có thể thay đổi lô bằng cách click vào button "Chọn lô"
- Lô mới tạo có thể xem và quản lý trong màn hình **Quản lý lô hàng**

### Về Nhà cung cấp: 🆕
- **Tính năng tự động tạo nhà cung cấp mới**: Khi import Excel, hệ thống sẽ:
  1. Tìm nhà cung cấp theo **Số điện thoại** (ưu tiên cao nhất)
  2. Nếu không tìm thấy, tìm theo **Mã NCC** (nếu có điền)
  3. Nếu không tìm thấy, **TỰ ĐỘNG TẠO** nhà cung cấp mới với thông tin từ Excel
- Nhà cung cấp mới tạo sẽ có:
  - Mã NCC: Tự động sinh (NCCxxxx) nếu để trống trong Excel
  - Tên, địa chỉ, SĐT, email, mã số thuế: Theo thông tin trong Excel
- Thông tin nhà cung cấp sẽ được hiển thị trong giao diện phiếu nhập hàng
- Nhà cung cấp mới tạo có thể xem và quản lý trong màn hình **Quản lý nhà cung cấp**
- **Validation**: Hệ thống sẽ kiểm tra định dạng SĐT (10 số), email, mã số thuế trước khi tạo

