-- ==============================================================================
-- FIX: Sua loi encoding cho cot trangThaiXuLy trong bang dontrahang
-- ==============================================================================
-- Van de: Database luu "Chua xu ly" (khong dau) nhung code Java dung "Chưa xử lý" (co dau)
-- Giai phap: Cap nhat du lieu va DEFAULT constraint de khop voi code Java
-- ==============================================================================

USE IUHPharmacityManagement;

PRINT N'=== BẮT ĐẦU SỬA LỖI ENCODING ===';
PRINT N'';

-- Buoc 1: Cap nhat du lieu cu tu "Chua xu ly" (khong dau) -> "Chưa xử lý" (co dau)
PRINT N'Bước 1: Cập nhật dữ liệu cũ...';
UPDATE dbo.dontrahang
SET trangThaiXuLy = N'Chưa xử lý'
WHERE trangThaiXuLy IN ('Chua xu ly', N'Chua x? ly');

DECLARE @rowsUpdated INT = @@ROWCOUNT;
PRINT N'✓ Đã cập nhật ' + CAST(@rowsUpdated AS NVARCHAR) + N' dòng';
PRINT N'';

-- Buoc 2: Xoa DEFAULT constraint cu
PRINT N'Bước 2: Xóa DEFAULT constraint cũ...';
DECLARE @constraintName NVARCHAR(200);
SELECT @constraintName = dc.name
FROM sys.default_constraints dc
WHERE dc.parent_object_id = OBJECT_ID('dbo.dontrahang')
    AND COL_NAME(dc.parent_object_id, dc.parent_column_id) = 'trangThaiXuLy';

IF @constraintName IS NOT NULL
BEGIN
    EXEC('ALTER TABLE dbo.dontrahang DROP CONSTRAINT [' + @constraintName + ']');
    PRINT N'✓ Đã xóa DEFAULT constraint cũ: ' + @constraintName;
END
ELSE
BEGIN
    PRINT N'⚠ Không tìm thấy DEFAULT constraint cũ';
END
PRINT N'';

-- Buoc 3: Them DEFAULT constraint moi voi gia tri CO DAU
PRINT N'Bước 3: Thêm DEFAULT constraint mới...';
ALTER TABLE dbo.dontrahang
ADD CONSTRAINT DF_trangThaiXuLy DEFAULT N'Chưa xử lý' FOR trangThaiXuLy;
PRINT N'✓ Đã thêm DEFAULT constraint với giá trị CÓ DẤU';
PRINT N'';

-- Buoc 4: Kiem tra ket qua
PRINT N'=== KẾT QUẢ SAU KHI SỬA ===';
PRINT N'';

PRINT N'1. DEFAULT Constraint:';
SELECT 
    dc.name AS [Tên Constraint],
    OBJECT_DEFINITION(dc.object_id) AS [Giá trị mặc định]
FROM sys.default_constraints dc
WHERE dc.parent_object_id = OBJECT_ID('dbo.dontrahang')
    AND COL_NAME(dc.parent_object_id, dc.parent_column_id) = 'trangThaiXuLy';

PRINT N'';
PRINT N'2. Dữ liệu hiện tại:';
SELECT trangThaiXuLy AS [Trạng thái], COUNT(*) as [Số lượng]
FROM dbo.dontrahang
GROUP BY trangThaiXuLy;

PRINT N'';
PRINT N'=== HOÀN TẤT ===';
PRINT N'';
PRINT N'Lưu ý: Các giá trị hợp lệ cho trangThaiXuLy:';
PRINT N'  - "Chưa xử lý"   (mặc định)';
PRINT N'  - "Chờ xuất hủy"';
PRINT N'  - "Đã xử lý"';
