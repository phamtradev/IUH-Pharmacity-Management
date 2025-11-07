-- ==========================================
-- Script: Sửa Primary Key của bảng ChiTietHangHong
-- Mục đích: Cho phép CÙNG 1 lô hàng có NHIỀU lý do xuất hủy khác nhau
-- ==========================================
-- Primary Key CŨ: (maHangHong, maLoHang)
-- Primary Key MỚI: (maHangHong, maLoHang, lyDoXuatHuy)
-- ==========================================

USE PharmacityManagement;
GO

PRINT N'🔧 Bắt đầu sửa Primary Key của bảng ChiTietHangHong...';
GO

-- ========================================== 
-- BƯỚC 1: Kiểm tra cột lyDoXuatHuy đã tồn tại chưa
-- ==========================================
IF NOT EXISTS (
    SELECT * 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_NAME = 'ChiTietHangHong' 
    AND COLUMN_NAME = 'lyDoXuatHuy'
)
BEGIN
    PRINT N'❌ Lỗi: Cột lyDoXuatHuy chưa tồn tại!';
    PRINT N'   → Vui lòng chạy script add_lyDoXuatHuy_column.sql trước!';
    RAISERROR(N'Cột lyDoXuatHuy chưa tồn tại', 16, 1);
    RETURN;
END
GO

-- ==========================================
-- BƯỚC 2: Cập nhật dữ liệu NULL thành giá trị mặc định
-- ==========================================
PRINT N'📝 Đang cập nhật dữ liệu NULL...';
UPDATE ChiTietHangHong
SET lyDoXuatHuy = N'Chưa rõ lý do'
WHERE lyDoXuatHuy IS NULL OR lyDoXuatHuy = '';
GO

PRINT N'✓ Đã cập nhật dữ liệu NULL';
GO

-- ==========================================
-- BƯỚC 3: Tìm tên constraint Primary Key hiện tại
-- ==========================================
DECLARE @pkName NVARCHAR(255);

SELECT @pkName = name
FROM sys.key_constraints
WHERE type = 'PK' 
  AND parent_object_id = OBJECT_ID('dbo.ChiTietHangHong');

IF @pkName IS NOT NULL
BEGIN
    PRINT N'🔍 Tìm thấy Primary Key constraint: ' + @pkName;
    
    -- Xóa Primary Key cũ
    DECLARE @sql NVARCHAR(500) = N'ALTER TABLE ChiTietHangHong DROP CONSTRAINT ' + QUOTENAME(@pkName);
    EXEC sp_executesql @sql;
    
    PRINT N'✓ Đã xóa Primary Key cũ: ' + @pkName;
END
ELSE
BEGIN
    PRINT N'⚠️ Không tìm thấy Primary Key constraint';
END
GO

-- ==========================================
-- BƯỚC 4: Đặt lyDoXuatHuy thành NOT NULL
-- ==========================================
PRINT N'🔧 Đang sửa cột lyDoXuatHuy thành NOT NULL...';
ALTER TABLE ChiTietHangHong
ALTER COLUMN lyDoXuatHuy NVARCHAR(255) NOT NULL;
GO

PRINT N'✓ Đã sửa cột lyDoXuatHuy thành NOT NULL';
GO

-- ==========================================
-- BƯỚC 5: Tạo Primary Key mới (3 cột)
-- ==========================================
PRINT N'🔧 Đang tạo Primary Key mới...';
ALTER TABLE ChiTietHangHong
ADD CONSTRAINT PK_ChiTietHangHong 
PRIMARY KEY (maHangHong, maLoHang, lyDoXuatHuy);
GO

PRINT N'✓ Đã tạo Primary Key mới: (maHangHong, maLoHang, lyDoXuatHuy)';
GO

-- ==========================================
-- BƯỚC 6: Kiểm tra kết quả
-- ==========================================
PRINT N'';
PRINT N'📊 Kiểm tra cấu trúc bảng:';
SELECT 
    COLUMN_NAME AS [Tên cột],
    DATA_TYPE AS [Kiểu dữ liệu],
    CHARACTER_MAXIMUM_LENGTH AS [Độ dài],
    IS_NULLABLE AS [Cho phép NULL]
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'ChiTietHangHong'
ORDER BY ORDINAL_POSITION;
GO

PRINT N'';
PRINT N'🔑 Kiểm tra Primary Key:';
SELECT 
    kc.name AS [Constraint Name],
    c.name AS [Column Name]
FROM sys.key_constraints kc
INNER JOIN sys.index_columns ic ON kc.parent_object_id = ic.object_id AND kc.unique_index_id = ic.index_id
INNER JOIN sys.columns c ON ic.object_id = c.object_id AND ic.column_id = c.column_id
WHERE kc.type = 'PK' 
  AND kc.parent_object_id = OBJECT_ID('dbo.ChiTietHangHong')
ORDER BY ic.key_ordinal;
GO

PRINT N'';
PRINT N'✅ HOÀN THÀNH! Bạn có thể chạy lại ứng dụng.';
PRINT N'';
PRINT N'📌 LƯU Ý:';
PRINT N'   - Primary Key CŨ: (maHangHong, maLoHang)';
PRINT N'   - Primary Key MỚI: (maHangHong, maLoHang, lyDoXuatHuy)';
PRINT N'   - Giờ có thể có NHIỀU dòng cùng lô hàng nhưng KHÁC lý do xuất hủy';
PRINT N'';
PRINT N'📝 Ví dụ dữ liệu hợp lệ sau khi sửa:';
PRINT N'   ✅ (HH001, LH005, "Hết hạn")       → OK';
PRINT N'   ✅ (HH001, LH005, "Bao bì hư")     → OK (cùng lô, khác lý do)';
PRINT N'   ❌ (HH001, LH005, "Hết hạn")       → LỖI (trùng hoàn toàn)';
PRINT N'   ✅ (HH001, LH006, "Hết hạn")       → OK (khác lô)';
GO

