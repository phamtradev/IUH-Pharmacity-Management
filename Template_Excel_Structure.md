# 📊 Cấu trúc Template Excel - Import Phiếu Nhập Hàng

## Header Row (Dòng 1) - Các cột theo thứ tự đề xuất:

| STT | Tên Cột | Bắt buộc | Ghi chú |
|-----|---------|----------|---------|
| A | **Mã sản phẩm** | ✅ BẮT BUỘC | Mã SP đã có trong hệ thống |
| B | **Tên sản phẩm** | ❌ Tùy chọn | Chỉ để tham khảo |
| C | **Số lượng** | ✅ BẮT BUỘC | Số nguyên > 0 |
| D | **Đơn giá nhập** | ✅ BẮT BUỘC | Số > 0 |
| E | **Hạn dùng** | ❌ Tùy chọn | dd/MM/yyyy hoặc dd-MM-yyyy |
| F | **Số lô** | ❌ Tùy chọn | Nếu không có sẽ tự sinh |
| G | **Tên NCC** | ✅ BẮT BUỘC | Tên nhà cung cấp |
| H | **Địa chỉ** | ❌ Tùy chọn | Địa chỉ NCC |
| I | **SĐT** | ❌ Tùy chọn | 10 số, bắt đầu 0 |
| J | **Email** | ❌ Tùy chọn | Format email hợp lệ |
| K | **Mã số thuế** | ❌ Tùy chọn | Mã số thuế NCC |

---

## 📝 Data Rows (Từ dòng 2 trở đi)

### ✅ Ví dụ 1: Import với NCC mới (đầy đủ thông tin)

| Mã sản phẩm | Tên sản phẩm | Số lượng | Đơn giá nhập | Hạn dùng | Số lô | Tên NCC | Địa chỉ | SĐT | Email | Mã số thuế |
|------------|--------------|----------|--------------|----------|-------|---------|---------|-----|-------|------------|
| SP001 | Paracetamol 500mg | 100 | 50000 | 31/12/2025 | LO001 | Công ty Dược Phát Đạt | 123 Nguyễn Văn Cừ, Q5, HCM | 0912345678 | contact@duocphatdat.com | 0123456789-001 |
| SP002 | Vitamin C 1000mg | 200 | 80000 | 30/06/2026 | LO002 | Công ty Dược Phát Đạt | | | | |
| SP003 | Amoxicillin 500mg | 150 | 120000 | 15/09/2025 | | Công ty Dược Phát Đạt | | | | |

**Kết quả:**
- ✅ Tạo NCC mới "Công ty Dược Phát Đạt" với mã tự sinh: **NCC0001**
- ✅ Import 3 sản phẩm
- ✅ Tạo 2 lô hàng: LO001, LO002
- ✅ Lô hàng dòng 3 tự sinh: `LO20240126150000`

---

### ✅ Ví dụ 2: Import với NCC đã tồn tại (chỉ cần tên)

| Mã sản phẩm | Tên sản phẩm | Số lượng | Đơn giá nhập | Hạn dùng | Số lô | Tên NCC | Địa chỉ | SĐT | Email | Mã số thuế |
|------------|--------------|----------|--------------|----------|-------|---------|---------|-----|-------|------------|
| SP004 | Ibuprofen 400mg | 50 | 90000 | 20/08/2025 | LO003 | Công ty Dược Phát Đạt | | | | |

**Kết quả:**
- ✅ Tìm thấy NCC "Công ty Dược Phát Đạt" trong DB
- ✅ Dùng NCC có sẵn (mã: NCC0001)
- ✅ Import 1 sản phẩm với lô LO003

---

### ✅ Ví dụ 3: Import với NCC mới (thông tin tối thiểu)

| Mã sản phẩm | Tên sản phẩm | Số lượng | Đơn giá nhập | Hạn dùng | Số lô | Tên NCC | Địa chỉ | SĐT | Email | Mã số thuế |
|------------|--------------|----------|--------------|----------|-------|---------|---------|-----|-------|------------|
| SP005 | Omeprazole 20mg | 80 | 110000 | | | Nhà thuốc Bình An | | | | |

**Kết quả:**
- ✅ Tạo NCC mới "Nhà thuốc Bình An" với mã tự sinh: **NCC0002**
- ✅ Các field optional để trống (null)
- ✅ Hạn dùng mặc định: 2 năm từ ngày nhập
- ✅ Số lô tự sinh: `LO20240126150100`

---

## 🎯 Quy tắc quan trọng

### 1️⃣ Xử lý Nhà Cung Cấp
```
IF (Tìm thấy NCC theo "Tên NCC"):
    ✅ Dùng NCC có sẵn
ELSE IF (Có SĐT && Tìm thấy NCC theo SĐT):
    ✅ Dùng NCC có sẵn
ELSE:
    ✅ Tạo NCC mới với:
       - Mã: Tự sinh (NCC0001, NCC0002,...)
       - Tên: Lấy từ "Tên NCC"
       - Các field khác: Lấy từ Excel (nếu có)
```

### 2️⃣ Xử lý Lô hàng
```
IF (Có "Số lô" && Lô tồn tại trong DB):
    ✅ Dùng lô có sẵn
ELSE:
    ✅ Tạo lô mới với:
       - Tên: "Số lô" từ Excel HOẶC Tự sinh "LO{timestamp}"
       - Hạn dùng: Từ Excel HOẶC Mặc định +2 năm
       - Sản phẩm: Link với mã sản phẩm
```

### 3️⃣ Validation
- ❌ Mã sản phẩm không tồn tại → Skip dòng
- ❌ Số lượng ≤ 0 → Skip dòng  
- ❌ Đơn giá ≤ 0 → Skip dòng
- ❌ SĐT sai format (nếu có) → Báo lỗi
- ❌ Email sai format (nếu có) → Báo lỗi
- ⚠️ Hạn dùng < Ngày hiện tại → Cảnh báo nhưng vẫn import

---

## 📌 Format dữ liệu chi tiết

### 📅 Hạn dùng
- **Format hợp lệ:** 
  - `31/12/2025`
  - `31-12-2025`
  - Excel Date format
- **Giá trị mặc định:** Ngày hiện tại + 2 năm

### 📱 SĐT
- **Format:** 10 chữ số, bắt đầu bằng 0
- **Ví dụ hợp lệ:** 0912345678, 0987654321
- **Regex:** `^0\\d{9}$`

### 📧 Email  
- **Format:** standard email format
- **Ví dụ hợp lệ:** contact@company.com, info@pharmacy.vn
- **Regex:** `^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$`

### 🔢 Số lượng và Đơn giá
- **Số lượng:** Số nguyên dương > 0
- **Đơn giá:** Số thực > 0

---

## 🚀 Tạo file Excel mẫu

### Cách 1: Sử dụng Excel
1. Tạo file mới, sheet đầu tiên
2. Dòng 1: Copy header từ bảng trên
3. Từ dòng 2: Điền dữ liệu sản phẩm
4. Save as: **Template_Import_Nhap_Hang.xlsx**

### Cách 2: Download template có sẵn
- File template được chuẩn bị sẵn với:
  - ✅ Header đầy đủ
  - ✅ 3 dòng dữ liệu mẫu
  - ✅ Data validation cho một số cột
  - ✅ Comment hướng dẫn

---

## 💡 Tips & Best Practices

1. **Thông tin NCC đầy đủ ở dòng đầu tiên**
   - Giúp tránh tạo trùng NCC
   - Dễ quản lý và tra cứu sau này

2. **Điền SĐT khi tạo NCC mới**
   - Giúp tìm kiếm NCC nhanh hơn
   - Tránh tạo trùng NCC có cùng tên

3. **Sử dụng số lô có ý nghĩa**
   - VD: `LO_PARA_012025` (Paracetamol tháng 1/2025)
   - Dễ nhận biết và quản lý kho

4. **Kiểm tra mã sản phẩm trước**
   - Đảm bảo sản phẩm đã được tạo trong hệ thống
   - Tránh lỗi import do mã không tồn tại

5. **Import từng NCC một**
   - Mỗi file Excel nên chỉ có sản phẩm từ 1 NCC
   - Dễ kiểm soát và đối chiếu

