-- ============================================
-- SCRIPT TEST: KIỂM TRA XUNG ĐỘT NHÀ CUNG CẤP
-- ============================================

-- Bước 1: Kiểm tra dữ liệu hiện tại
SELECT 
    sp.maSanPham,
    sp.tenSanPham,
    sp.soDangKy,
    ncc.maNhaCungCap,
    ncc.tenNhaCungCap,
    COUNT(DISTINCT dnh.maDonNhapHang) as soLanNhap
FROM SanPham sp
LEFT JOIN LoHang lh ON sp.maSanPham = lh.maSanPham
LEFT JOIN ChiTietDonNhapHang ctdnh ON lh.maLoHang = ctdnh.maLoHang
LEFT JOIN DonNhapHang dnh ON ctdnh.maDonNhapHang = dnh.maDonNhapHang
LEFT JOIN NhaCungCap ncc ON dnh.maNhaCungCap = ncc.maNhaCungCap
GROUP BY sp.maSanPham, sp.tenSanPham, sp.soDangKy, ncc.maNhaCungCap, ncc.tenNhaCungCap
ORDER BY sp.soDangKy, ncc.maNhaCungCap;

-- Bước 2: Kiểm tra số đăng ký nào được nhập bởi nhiều nhà cung cấp (KHÔNG ĐƯỢC PHÉP!)
SELECT 
    sp.soDangKy,
    sp.tenSanPham,
    COUNT(DISTINCT dnh.maNhaCungCap) as soLuongNCC,
    STRING_AGG(DISTINCT ncc.tenNhaCungCap, ', ') as danhSachNCC
FROM SanPham sp
INNER JOIN LoHang lh ON sp.maSanPham = lh.maSanPham
INNER JOIN ChiTietDonNhapHang ctdnh ON lh.maLoHang = ctdnh.maLoHang
INNER JOIN DonNhapHang dnh ON ctdnh.maDonNhapHang = dnh.maDonNhapHang
INNER JOIN NhaCungCap ncc ON dnh.maNhaCungCap = ncc.maNhaCungCap
GROUP BY sp.soDangKy, sp.tenSanPham
HAVING COUNT(DISTINCT dnh.maNhaCungCap) > 1
ORDER BY soLuongNCC DESC;

-- Bước 3: Test query chính (giống trong SanPhamDAO)
-- Ví dụ: Kiểm tra số đăng ký nào đó
DECLARE @soDangKy NVARCHAR(50) = 'VD-12345-67'; -- Thay bằng số đăng ký thật

SELECT DISTINCT 
    ncc.maNhaCungCap, 
    ncc.tenNhaCungCap, 
    ncc.diaChi, 
    ncc.soDienThoai, 
    ncc.email, 
    ncc.maSoThue 
FROM NhaCungCap ncc 
INNER JOIN DonNhapHang dnh ON ncc.maNhaCungCap = dnh.maNhaCungCap 
INNER JOIN ChiTietDonNhapHang ctdnh ON dnh.maDonNhapHang = ctdnh.maDonNhapHang 
INNER JOIN LoHang lh ON ctdnh.maLoHang = lh.maLoHang 
INNER JOIN SanPham sp ON lh.maSanPham = sp.maSanPham 
WHERE sp.soDangKy = @soDangKy;

-- Bước 4: Tạo test data (nếu cần)
/*
-- Tạo 2 NCC test
INSERT INTO NhaCungCap (maNhaCungCap, tenNhaCungCap, diaChi, soDienThoai, email)
VALUES 
    ('NCC99998', 'Công ty Test A', '123 Test St', '0901111111', 'nccA@test.com'),
    ('NCC99999', 'Công ty Test B', '456 Test Ave', '0902222222', 'nccB@test.com');

-- Tạo 2 sản phẩm test
INSERT INTO SanPham (maSanPham, tenSanPham, soDangKy, hoatChat, lieuDung, cachDongGoi, 
                     quocGiaSanXuat, nhaSanXuat, giaNhap, giaBan, hoatDong, thueVAT, maDonVi)
VALUES 
    ('SP99998', 'Sản phẩm Test 1', 'SDK-TEST-001', 'Hoạt chất A', '1 viên/ngày', 'Hộp 10 viên',
     'Việt Nam', 'NSX Test', 50000, 60000, 1, 0.1, 'DV001'),
    ('SP99999', 'Sản phẩm Test 2', 'SDK-TEST-002', 'Hoạt chất B', '2 viên/ngày', 'Hộp 20 viên',
     'Việt Nam', 'NSX Test', 75000, 90000, 1, 0.1, 'DV001');

-- Tạo lô hàng test
INSERT INTO LoHang (maLoHang, tenLoHang, hanSuDung, tonKho, trangThai, maSanPham)
VALUES 
    ('LH99998', 'Lô Test 1', '2026-12-31', 0, 1, 'SP99998'),
    ('LH99999', 'Lô Test 2', '2026-12-31', 0, 1, 'SP99999');
*/

-- Bước 5: Xóa test data (sau khi test xong)
/*
DELETE FROM ChiTietDonNhapHang WHERE maLoHang IN ('LH99998', 'LH99999');
DELETE FROM LoHang WHERE maLoHang IN ('LH99998', 'LH99999');
DELETE FROM SanPham WHERE maSanPham IN ('SP99998', 'SP99999');
DELETE FROM DonNhapHang WHERE maNhaCungCap IN ('NCC99998', 'NCC99999');
DELETE FROM NhaCungCap WHERE maNhaCungCap IN ('NCC99998', 'NCC99999');
*/

-- Bước 6: Report tổng hợp
SELECT 
    '1. Tổng số sản phẩm' as Metric,
    COUNT(*) as Value
FROM SanPham

UNION ALL

SELECT 
    '2. Tổng số nhà cung cấp',
    COUNT(*)
FROM NhaCungCap

UNION ALL

SELECT 
    '3. Số sản phẩm đã từng được nhập',
    COUNT(DISTINCT sp.maSanPham)
FROM SanPham sp
INNER JOIN LoHang lh ON sp.maSanPham = lh.maSanPham
INNER JOIN ChiTietDonNhapHang ctdnh ON lh.maLoHang = ctdnh.maLoHang

UNION ALL

SELECT 
    '4. Số sản phẩm bị conflict (nhiều NCC)',
    COUNT(DISTINCT sp.soDangKy)
FROM SanPham sp
INNER JOIN LoHang lh ON sp.maSanPham = lh.maSanPham
INNER JOIN ChiTietDonNhapHang ctdnh ON lh.maLoHang = ctdnh.maLoHang
INNER JOIN DonNhapHang dnh ON ctdnh.maDonNhapHang = dnh.maDonNhapHang
GROUP BY sp.soDangKy
HAVING COUNT(DISTINCT dnh.maNhaCungCap) > 1;

