-- Thêm cột lyDoXuatHuy vào bảng ChiTietHangHong
-- Script này an toàn, chỉ thêm cột nếu chưa tồn tại

USE PharmacityManagement;
GO

-- Kiểm tra xem cột đã tồn tại chưa
IF NOT EXISTS (
    SELECT * 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_NAME = 'ChiTietHangHong' 
    AND COLUMN_NAME = 'lyDoXuatHuy'
)
BEGIN
    -- Thêm cột lyDoXuatHuy (cho phép NULL, sau này có thể update dữ liệu cũ)
    ALTER TABLE ChiTietHangHong
    ADD lyDoXuatHuy NVARCHAR(500) NULL;
    
    PRINT N'✓ Đã thêm cột lyDoXuatHuy vào bảng ChiTietHangHong';
END
ELSE
BEGIN
    PRINT N'✓ Cột lyDoXuatHuy đã tồn tại trong bảng ChiTietHangHong';
END
GO

-- Kiểm tra kết quả
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    CHARACTER_MAXIMUM_LENGTH,
    IS_NULLABLE
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'ChiTietHangHong'
ORDER BY ORDINAL_POSITION;
GO

-- (Tùy chọn) Update dữ liệu cũ với giá trị mặc định
UPDATE ChiTietHangHong
SET lyDoXuatHuy = N''
WHERE lyDoXuatHuy IS NULL;
GO

PRINT N'✓ Hoàn thành! Bạn có thể chạy lại ứng dụng.';
GO


