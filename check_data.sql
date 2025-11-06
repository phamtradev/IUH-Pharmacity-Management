-- Kiem tra du lieu thuc te trong bang dontrahang
USE IUHPharmacityManagement;

-- Xem tat ca cac gia tri DISTINCT cua trangThaiXuLy
SELECT DISTINCT trangThaiXuLy, COUNT(*) as SoLuong
FROM dbo.dontrahang
GROUP BY trangThaiXuLy
ORDER BY SoLuong DESC;

-- Xem chi tiet mot vai dong
SELECT TOP 5 maDonTra, trangThaiXuLy, lyDoTra
FROM dbo.dontrahang;
