-- Xem du lieu thuc te
USE IUHPharmacityManagement;

-- Xem tat ca cac gia tri khac nhau cua trangThaiXuLy
SELECT DISTINCT trangThaiXuLy, COUNT(*) as SoLuong
FROM dbo.dontrahang
GROUP BY trangThaiXuLy;

-- Xem top 10 dong
SELECT TOP 10 *
FROM dbo.dontrahang;

