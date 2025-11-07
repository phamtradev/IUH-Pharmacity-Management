# 🔐 TÍNH NĂNG KIỂM TRA XUNG ĐỘT NHÀ CUNG CẤP - SỐ ĐĂNG KÝ

## 📋 MÔ TẢ

**Business Rule:** Một số đăng ký sản phẩm chỉ được phép nhập bởi **MỘT nhà cung cấp duy nhất**.

Nếu:
- **Số Đăng Ký A** đã được nhập bởi **Nhà Cung Cấp X**
- Thì **Nhà Cung Cấp Y** sẽ **KHÔNG được phép** nhập **Số Đăng Ký A** → **Hiển thị thông báo lỗi!**

---

## 🏗️ KIẾN TRÚC

### 1. **Database Layer (DAO)**

File: `src/main/java/.../dao/SanPhamDAO.java`

**SQL Query:**
```sql
SELECT DISTINCT ncc.maNhaCungCap, ncc.tenNhaCungCap 
FROM NhaCungCap ncc
INNER JOIN DonNhapHang dnh ON ncc.maNhaCungCap = dnh.maNhaCungCap
INNER JOIN ChiTietDonNhapHang ctdnh ON dnh.maDonNhapHang = ctdnh.maDonNhapHang
INNER JOIN LoHang lh ON ctdnh.maLoHang = lh.maLoHang
INNER JOIN SanPham sp ON lh.maSanPham = sp.maSanPham
WHERE sp.soDangKy = ?
```

**Method:**
```java
public List<String> getMaNhaCungCapBySoDangKy(String soDangKy)
```

→ **Trả về:** Danh sách mã nhà cung cấp đã từng nhập số đăng ký này.

---

### 2. **Business Layer (BUS)**

File: `src/main/java/.../bus/SanPhamBUS.java`

**Method 1: Kiểm tra xung đột**
```java
public boolean kiemTraNhaCungCapCoTheNhapSoDangKy(String soDangKy, String maNhaCungCap)
```

→ **Logic:**
- Nếu chưa có NCC nào nhập → `true` (OK)
- Nếu cùng NCC đã nhập → `true` (OK)
- Nếu khác NCC đã nhập → `false` (CONFLICT!)

**Method 2: Lấy tên NCC đã nhập (để hiển thị lỗi)**
```java
public String layTenNhaCungCapDaNhapSoDangKy(String soDangKy)
```

---

### 3. **Presentation Layer (GUI)**

File: `src/main/java/.../quanlyphieunhaphang/GD_QuanLyPhieuNhapHang.java`

#### ✅ **Validation 1: Import từ Excel** (Dòng ~786-807)

```java
if (nhaCungCap != null && nhaCungCap.getMaNhaCungCap() != null) {
    boolean coTheNhap = sanPhamBUS.kiemTraNhaCungCapCoTheNhapSoDangKy(soDangKy, maNCC);
    
    if (!coTheNhap) {
        // Báo lỗi và skip sản phẩm này
        errors.append("Dòng " + i + ": Sản phẩm đã được nhập bởi NCC khác!\n");
        errorCount++;
        continue;
    }
}
```

#### ✅ **Validation 2: Thêm thủ công** (Dòng ~591-617)

```java
if (nhaCungCapHienTai != null && nhaCungCapHienTai.getMaNhaCungCap() != null) {
    boolean coTheNhap = sanPhamBUS.kiemTraNhaCungCapCoTheNhapSoDangKy(soDangKy, maNCC);
    
    if (!coTheNhap) {
        // Hiển thị notification lỗi
        Notifications.getInstance().show(Notifications.Type.ERROR, 
            "❌ KHÔNG THỂ THÊM! Sản phẩm đã được nhập bởi NCC khác!");
        return;
    }
}
```

---

## 🧪 HƯỚNG DẪN TEST

### **Bước 1: Chuẩn bị dữ liệu**

1. **Tạo 2 Nhà Cung Cấp:**
   - NCC A: `NCC00001` - "Công ty A" - SĐT: `0901111111`
   - NCC B: `NCC00002` - "Công ty B" - SĐT: `0902222222`

2. **Tạo 2 Sản Phẩm:**
   - SP1: `SP00001` - Số đăng ký: `SDK-001`
   - SP2: `SP00002` - Số đăng ký: `SDK-002`

---

### **Test Case 1: ✅ Nhập lần đầu (OK)**

**Kịch bản:**
- NCC A nhập SP1 (SDK-001) → **Thành công**
- Vì chưa có NCC nào nhập SDK-001 trước đó

**Thao tác:**
1. Mở màn hình **Quản Lý Phiếu Nhập Hàng**
2. Tìm NCC A (SĐT: `0901111111`)
3. Thêm sản phẩm SP1 (SDK-001)
4. Nhập hàng thành công

**Kết quả mong đợi:**
```
✅ [BUS] Số đăng ký 'SDK-001' chưa được nhập bởi ai → OK
✓ Import thành công
```

---

### **Test Case 2: ✅ Cùng NCC nhập lại (OK)**

**Kịch bản:**
- NCC A nhập SP1 (SDK-001) lần 2 → **Thành công**
- Vì NCC A đã từng nhập SDK-001 → Được phép

**Thao tác:**
1. Tìm NCC A lại
2. Thêm SP1 (SDK-001) lần nữa
3. Nhập hàng thành công

**Kết quả mong đợi:**
```
✅ [BUS] Nhà cung cấp NCC00001 đã từng nhập số đăng ký 'SDK-001' → OK
✓ Import thành công
```

---

### **Test Case 3: ❌ NCC khác nhập (CONFLICT)**

**Kịch bản:**
- NCC B muốn nhập SP1 (SDK-001) → **Bị từ chối!**
- Vì SDK-001 đã được nhập bởi NCC A

**Thao tác (Thêm thủ công):**
1. Tìm NCC B (SĐT: `0902222222`)
2. Thêm sản phẩm SP1 (SDK-001) bằng cách nhập mã

**Kết quả mong đợi:**
```
❌ [BUS] CONFLICT! Số đăng ký 'SDK-001' đã được nhập bởi [NCC00001]

Thông báo lỗi:
┌────────────────────────────────────────┐
│ ❌ KHÔNG THỂ THÊM!                     │
│ Sản phẩm 'Tên SP1' (SDK: SDK-001)     │
│ đã được nhập bởi nhà cung cấp NCC00001.│
│ Không thể nhập từ nhà cung cấp khác!   │
└────────────────────────────────────────┘
```

**Thao tác (Import Excel):**
1. Tạo file Excel với:
   ```
   Mã SP   | Số lượng | Đơn giá | Hạn dùng    | Mã NCC    | Tên NCC   | SĐT
   SDK-001 | 10       | 50000   | 01/01/2025  | NCC00002  | Công ty B | 0902222222
   ```
2. Import file Excel

**Kết quả mong đợi:**
```
❌ Import thất bại
Lỗi: Dòng 2: Sản phẩm 'Tên SP1' (SDK: SDK-001) đã được nhập bởi nhà cung cấp NCC00001. 
Không thể nhập từ nhà cung cấp khác!
```

---

### **Test Case 4: ✅ NCC khác nhập SP khác (OK)**

**Kịch bản:**
- NCC B nhập SP2 (SDK-002) → **Thành công**
- Vì SDK-002 chưa được nhập bởi ai

**Thao tác:**
1. Tìm NCC B
2. Thêm sản phẩm SP2 (SDK-002)
3. Nhập hàng thành công

**Kết quả mong đợi:**
```
✅ [BUS] Số đăng ký 'SDK-002' chưa được nhập bởi ai → OK
✓ Import thành công
```

---

## 📊 BẢNG TỔNG HỢP TEST CASES

| Test Case | NCC | Số Đăng Ký | Đã được nhập bởi | Kết quả | Thông báo |
|-----------|-----|-----------|------------------|---------|-----------|
| TC1       | A   | SDK-001   | -                | ✅ OK   | Chưa có ai nhập |
| TC2       | A   | SDK-001   | A                | ✅ OK   | Cùng NCC |
| TC3       | B   | SDK-001   | A                | ❌ FAIL | NCC khác đã nhập |
| TC4       | B   | SDK-002   | -                | ✅ OK   | Chưa có ai nhập |

---

## 🔍 DEBUG LOGS

Khi chạy, console sẽ hiển thị:

```
🔍 [DAO] Số đăng ký 'SDK-001' đã được nhập bởi 1 nhà cung cấp: [NCC00001]
✅ [BUS] Nhà cung cấp NCC00001 đã từng nhập số đăng ký 'SDK-001' → OK

🔍 [DAO] Số đăng ký 'SDK-001' đã được nhập bởi 1 nhà cung cấp: [NCC00001]
❌ [BUS] CONFLICT! Số đăng ký 'SDK-001' đã được nhập bởi [NCC00001], không thể cho NCC NCC00002 nhập!
```

---

## ✅ CHECKLIST HOÀN THÀNH

- [x] Tạo SQL query lấy NhaCungCap từ soDangKy
- [x] Tạo method kiểm tra supplier conflict trong BUS
- [x] Thêm validation vào import Excel
- [x] Thêm validation vào thêm thủ công
- [ ] Test với dữ liệu thực tế

---

## 📝 GHI CHÚ

1. **Tại sao cần rule này?**
   - Đảm bảo chất lượng sản phẩm
   - Tránh nhầm lẫn về nguồn gốc
   - Kiểm soát nhà cung cấp theo số đăng ký

2. **Nếu muốn cho phép nhiều NCC nhập cùng SDK?**
   - Sửa logic trong `kiemTraNhaCungCapCoTheNhapSoDangKy()`
   - Thay `return false` → `return true`

3. **Nếu muốn hiển thị tên NCC thay vì mã?**
   - Sửa SQL query để JOIN thêm cột `tenNhaCungCap`
   - Update method `layTenNhaCungCapDaNhapSoDangKy()` để return tên

---

**Ngày tạo:** 2025-11-07  
**Người tạo:** AI Assistant  
**Version:** 1.0

