# 📋 Hướng dẫn Import Phiếu Nhập Hàng từ Excel

## 📄 Format File Excel

### 🔑 Các cột BẮT BUỘC cho Sản phẩm:
1. **Mã sản phẩm** - Mã sản phẩm đã có trong hệ thống
2. **Số lượng** - Số lượng nhập vào
3. **Đơn giá nhập** - Giá nhập của sản phẩm

### 📦 Các cột TÙY CHỌN cho Sản phẩm:
- **Tên sản phẩm** - Để tham khảo (không dùng để tìm sản phẩm)
- **Số lô / Lô hàng** - Tên lô hàng (nếu không có sẽ tự sinh: `LOyyyyMMddHHmmss`)
- **Hạn dùng / Hạn sử dụng** - Ngày hết hạn (format: dd/MM/yyyy hoặc dd-MM-yyyy)

### 🏢 Các cột cho Nhà Cung Cấp:
- **Tên NCC** - **BẮT BUỘC** (dùng để tìm hoặc tạo mới NCC)
- **Địa chỉ** - Tùy chọn
- **SĐT / Số điện thoại** - Tùy chọn (nếu có sẽ dùng để tìm NCC)
- **Email** - Tùy chọn
- **Mã số thuế** - Tùy chọn

> ⚠️ **LƯU Ý**: 
> - **KHÔNG CẦN** cột "Mã NCC" - hệ thống sẽ tự động sinh mã khi tạo mới
> - Nếu tìm thấy NCC theo tên hoặc SĐT → dùng NCC có sẵn
> - Nếu không tìm thấy → tự động tạo mới với mã tự sinh (NCC0001, NCC0002,...)

---

## 📊 Ví dụ File Excel

### Header Row (dòng 1):
| Mã sản phẩm | Tên sản phẩm | Số lượng | Đơn giá nhập | Hạn dùng | Số lô | Tên NCC | Địa chỉ | SĐT | Email | Mã số thuế |
|------------|--------------|----------|--------------|----------|-------|---------|---------|-----|-------|------------|
| SP001 | Paracetamol 500mg | 100 | 50000 | 31/12/2025 | LO001 | Công ty Dược A | Hà Nội | 0912345678 | contact@duoca.com | 0123456789 |
| SP002 | Vitamin C 1000mg | 200 | 80000 | 30/06/2026 | LO002 | Công ty Dược A | | | | |
| SP003 | Amoxicillin 500mg | 150 | 120000 | 15/09/2025 | | Công ty Dược A | | | | |

### Giải thích:
- **Dòng 1**: Tạo mới NCC "Công ty Dược A" với đầy đủ thông tin
- **Dòng 2-3**: Dùng lại NCC "Công ty Dược A" (không cần điền lại thông tin)
- Lô hàng dòng 3 sẽ tự động sinh vì không có "Số lô"

---

## ✅ Quy trình Import

### Bước 1: Xử lý Nhà Cung Cấp (từ dòng đầu tiên)
1. Đọc thông tin NCC từ cột "Tên NCC"
2. Tìm theo **tên NCC** trong database
3. Nếu không tìm thấy → tìm theo **SĐT** (nếu có)
4. Nếu vẫn không tìm thấy → **tạo mới** với mã tự động sinh

### Bước 2: Xử lý từng Sản phẩm
1. Đọc **Mã sản phẩm** → tìm trong database
2. Nếu không tìm thấy → **BÁO LỖI** (phải tạo sản phẩm trước)
3. Đọc **Số lô** → tìm trong database
   - Tìm thấy → dùng lô có sẵn
   - Không tìm thấy → tạo lô mới
4. Thêm vào bảng chi tiết đơn nhập

### Bước 3: Hiển thị kết quả
- Thông tin NCC tự động hiển thị lên form
- Danh sách sản phẩm hiển thị trong bảng
- Tính tổng tiền tự động

---

## 🎯 Ví dụ Tạo Mới NCC

### Case 1: NCC mới hoàn toàn
```
Tên NCC: Công ty TNHH ABC
Địa chỉ: 123 Đường XYZ, Q1, HCM  
SĐT: 0909123456
Email: abc@company.com
Mã số thuế: 0123456789-001
```
→ Hệ thống tạo mới với mã: **NCC0001** (hoặc số tiếp theo)

### Case 2: NCC đã tồn tại
```
Tên NCC: Công ty TNHH ABC
```
→ Hệ thống tìm thấy và dùng NCC có sẵn

### Case 3: NCC mới - thông tin tối thiểu
```
Tên NCC: Nhà thuốc XYZ
```
→ Hệ thống tạo mới với chỉ có tên, mã tự sinh: **NCC0002**

---

## 🚨 Các trường hợp lỗi

### ❌ Lỗi Sản phẩm
- Mã sản phẩm không tồn tại → Dòng bị bỏ qua
- Số lượng ≤ 0 → Dòng bị bỏ qua
- Đơn giá ≤ 0 → Dòng bị bỏ qua

### ❌ Lỗi Nhà Cung Cấp
- Không có tên NCC → Không thể tạo đơn nhập
- SĐT sai định dạng → Báo lỗi
- Email sai định dạng → Báo lỗi

### ❌ Lỗi Lô hàng
- Hạn dùng sai format → Dùng giá trị mặc định
- Hạn dùng < ngày hiện tại → Cảnh báo nhưng vẫn import

---

## 📝 Lưu ý quan trọng

1. **Dòng header (dòng 1)** là bắt buộc
2. **Tên cột không phân biệt hoa thường** (VD: "mã sản phẩm", "Mã Sản Phẩm", "MÃ SẢN PHẨM" đều OK)
3. **Thứ tự cột không quan trọng** - hệ thống tự nhận diện
4. **Thông tin NCC chỉ đọc từ dòng đầu tiên** - các dòng sau không cần điền lại
5. **Mã NCC sẽ tự động sinh** theo format: NCC0001, NCC0002, NCC0003...

---

## 🎉 Tips

- Nên điền đầy đủ thông tin NCC ở dòng đầu tiên để tránh trùng lặp
- Nếu muốn dùng NCC có sẵn, chỉ cần điền đúng tên hoặc SĐT
- Có thể import nhiều lần cho cùng 1 NCC
- Lô hàng tự động sinh theo thời gian: `LO20240101120000`

