-- Kiểm tra các lô hàng có tồn kho âm
SELECT maLoHang, tenLoHang, tonKho, trangThai
FROM LoHang 
WHERE tonKho < 0 
ORDER BY tonKho;

-- Đếm số lô hàng có tồn kho âm
SELECT COUNT(*) AS soLuongLoHangAmTonKho
FROM LoHang 
WHERE tonKho < 0;

-- Fix: Update các lô hàng âm về 0
-- UPDATE LoHang SET tonKho = 0 WHERE tonKho < 0;

