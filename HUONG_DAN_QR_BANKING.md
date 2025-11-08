# Hướng Dẫn Sử Dụng QR Banking - Pharmacity Management

## 📋 Mục Lục
1. [Giới Thiệu](#giới-thiệu)
2. [Các File Đã Tạo](#các-file-đã-tạo)
3. [Cách Sử Dụng](#cách-sử-dụng)
4. [Kỹ Thuật](#kỹ-thuật)

---

## 🎯 Giới Thiệu

Chức năng **QR Banking** đơn giản cho phép hiển thị thông tin thanh toán qua mã QR (chỉ demo).

### Tính Năng
- ✅ Tạo mã QR chứa thông tin thanh toán
- ✅ Hiển thị: Số tiền, Mã đơn hàng, Thông tin tài khoản
- ✅ Tích hợp trong dialog xác nhận hóa đơn
- ✅ Giao diện đẹp, dễ sử dụng

---

## 📁 Các File Đã Tạo

### 1. `QRBankingUtil.java`
**Đường dẫn:** `src/main/java/vn/edu/iuh/fit/iuhpharmacitymanagement/util/QRBankingUtil.java`

**Chức năng:** Utility class để tạo QR code

**Methods:**
```java
// Tạo QR code đơn giản chứa thông tin thanh toán
generatePharmacityQR(maDonHang, amount, size)

// Generate QR code từ text
generateQRCode(content, width, height)

// Các getter để lấy thông tin
getBankName(), getAccountNumber(), getAccountName(), formatMoney()
```

**Nội dung QR:**
- Tên ngân hàng: MB Bank
- Số tài khoản: 0123456789  
- Tên tài khoản: PHARMACITY STORE
- Số tiền và mã đơn hàng

---

### 2. `Dialog_QRBanking.java`
**Đường dẫn:** `src/main/java/vn/edu/iuh/fit/iuhpharmacitymanagement/gui/dialog/Dialog_QRBanking.java`

**Chức năng:** Dialog hiển thị QR code thanh toán

**UI Components:**
- QR Code (350x350 pixels)
- Tên ngân hàng: MB Bank
- Số tài khoản: 0123456789
- Tên chủ TK: PHARMACITY MANAGEMENT
- Số tiền thanh toán
- Nội dung CK
- Nút Đóng

---

### 3. `Panel_DonHang.java` (Đã Chỉnh Sửa)
**Đường dẫn:** `src/main/java/vn/edu/iuh/fit/iuhpharmacitymanagement/gui/application/nhanvien/banhang/Panel_DonHang.java`

**Thay Đổi:**
- ✅ Thêm nút "Thanh Toán QR" vào panel thanh toán
- ✅ Logic hiển thị/ẩn nút dựa vào có sản phẩm hay không
- ✅ Method `hienThiQRBanking()` để xử lý sự kiện click

---

## 🚀 Cách Sử Dụng

### Bước 1: Build Project
```bash
mvn clean compile
```

### Bước 2: Chạy Ứng Dụng
```bash
mvn exec:java
# Hoặc chạy từ IDE (NetBeans/IntelliJ)
```

### Bước 3: Vào Màn Hình Bán Hàng
1. Đăng nhập với tài khoản nhân viên
2. Vào module **Bán Hàng**
3. Thêm sản phẩm vào giỏ hàng

### Bước 4: Thanh Toán QR
1. Sau khi thêm sản phẩm, nút **"Thanh Toán QR"** sẽ xuất hiện
2. Click vào nút này
3. Dialog QR Banking sẽ hiện ra với:
   - Mã QR code
   - Thông tin tài khoản
   - Số tiền cần thanh toán
   - Nội dung chuyển khoản

### Bước 5: Quét QR Code
- Mở app ngân hàng trên điện thoại
- Chọn chức năng **"Quét QR"** hoặc **"Chuyển khoản QR"**
- Quét mã QR trên màn hình
- Xác nhận thanh toán

---

## 📱 Test Với App Ngân Hàng

### Option 1: Test Với App Ngân Hàng Thật
> ⚠️ **LƯU Ý:** Đây chỉ là mô phỏng, không tạo giao dịch thật!

**Apps Hỗ Trợ Quét QR:**
- MB Bank
- VietinBank
- VCB (Vietcombank)
- Techcombank
- BIDV
- ACB
- TPBank
- SHB
- Sacombank
- ... và hầu hết các ngân hàng Việt Nam

**Cách Test:**
1. Mở app ngân hàng trên điện thoại
2. Tìm chức năng "Quét QR" hoặc "Chuyển khoản QR"
3. Quét mã QR từ màn hình máy tính
4. App sẽ hiển thị thông tin (nếu format đúng)

### Option 2: Test Với QR Reader Online
Nếu không có app ngân hàng, có thể test bằng:

**Website:**
- https://webqr.com/
- https://zxing.org/w/decode

**Cách Test:**
1. Chụp ảnh màn hình QR code
2. Upload lên website
3. Xem nội dung đã mã hóa

**Kết quả mong đợi:**
```
BANK:970422|ACC:0123456789|NAME:PHARMACITY MANAGEMENT|AMOUNT:150000|DESC:THANHTOAN DH08112025XXXX
```

### Option 3: Test Với QR Reader App
**Apps đề xuất:**
- QR Code Reader (iOS/Android)
- ZXing Decoder (Android)
- Camera mặc định của iPhone (hỗ trợ QR)

---

## 🔧 Kỹ Thuật

### Dependencies
Project đã có sẵn thư viện ZXing trong `pom.xml`:
```xml
<dependency>
    <groupId>com.google.zxing</groupId>
    <artifactId>core</artifactId>
    <version>3.5.0</version>
</dependency>
<dependency>
    <groupId>com.google.zxing</groupId>
    <artifactId>javase</artifactId>
    <version>3.5.0</version>
</dependency>
```

### QR Code Format
**Mô phỏng đơn giản:**
```
BANK:<mã ngân hàng>|ACC:<số TK>|NAME:<tên CK>|AMOUNT:<số tiền>|DESC:<nội dung>
```

**Ví dụ:**
```
BANK:970422|ACC:0123456789|NAME:PHARMACITY MANAGEMENT|AMOUNT:150000|DESC:THANHTOAN DH08112025XXXX
```

> 📝 **Ghi chú:** Format thực tế của VietQR phức tạp hơn (EMVCo QR Code Standard). Đây là mô phỏng đơn giản cho mục đích học tập.

### Error Correction Level
QR Code sử dụng **Error Correction Level H** (30% khả năng phục hồi):
- Cho phép QR vẫn đọc được khi bị hỏng 30%
- Phù hợp cho thanh toán quan trọng

---

## 🎨 Giao Diện

### Nút "Thanh Toán QR"
- **Vị trí:** Bên cạnh nút "Bán Hàng" trong Panel_ThanhToan
- **Màu:** Xanh dương (#2196F3)
- **Kích thước:** 220x50 pixels
- **Icon:** 💳 + "Thanh Toán QR"
- **Ẩn/hiện:** Tự động dựa vào giỏ hàng

### Dialog QR Banking
- **Kích thước:** 500x650 pixels
- **Modal:** Chặn tương tác với cửa sổ chính
- **QR Size:** 350x350 pixels
- **Font:** Segoe UI (13pt cho label, 14pt-bold cho value)

---

## ⚙️ Tùy Chỉnh

### Thay Đổi Thông Tin Tài Khoản
Mở file `QRBankingUtil.java`, tìm các constants ở đầu class:

```java
private static final String BANK_NAME = "MB Bank";
private static final String ACCOUNT_NUMBER = "0123456789";
private static final String ACCOUNT_NAME = "PHARMACITY STORE";
```

Thay đổi giá trị theo nhu cầu của bạn.

### Thay Đổi Kích Thước QR
Mở file `Dialog_QRBanking.java`, tìm dòng:

```java
BufferedImage qrImage = QRBankingUtil.generatePharmacityQR(maDonHang, soTien, 350);
```

Thay `350` thành kích thước mong muốn (pixels).

---

## 🐛 Xử Lý Lỗi

### Lỗi: "Lỗi khi hiển thị QR Code"
**Nguyên nhân:**
- Thư viện ZXing chưa được thêm vào classpath
- Payload quá dài (>4296 ký tự với QR Code)

**Giải pháp:**
```bash
mvn clean install
```

### Lỗi: Dialog không hiện
**Nguyên nhân:**
- Chưa có sản phẩm trong giỏ
- `tongTienHang` = 0

**Giải pháp:**
- Thêm ít nhất 1 sản phẩm vào giỏ trước khi nhấn "Bán Hàng"

### QR hiển thị nhỏ/mờ
**Giải pháp:**
- Tăng kích thước QR lên 400-500 pixels trong code
- Điều chỉnh độ phân giải màn hình

---

## 📞 Hỗ Trợ

### Liên Hệ
- **Tác giả:** PhamTraPhamTra
- **Email:** your-email@example.com
- **GitHub:** https://github.com/yourusername

### Tài Liệu Tham Khảo
- [ZXing Documentation](https://github.com/zxing/zxing) - Thư viện tạo QR Code

---

## 📝 Changelog

### Version 1.0 (08/11/2025)
- ✅ Tạo QRBankingUtil (đơn giản hóa)
- ✅ Tạo Dialog_QRBanking
- ✅ Tích hợp vào Dialog_XacNhanHoaDon
- ✅ QR code demo với thông tin cơ bản

---

## 📄 License
MIT License - Free to use for educational purposes

---

**🎉 Chúc bạn test thành công!** 

Nếu có vấn đề, hãy kiểm tra lại các bước hoặc liên hệ để được hỗ trợ.

