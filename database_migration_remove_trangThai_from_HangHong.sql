-- Script để xóa cột trangThai khỏi bảng HangHong
-- Ngày tạo: 03/11/2025

USE QuanLyNhaThuoc;
GO

-- Kiểm tra xem cột trangThai có tồn tại không
IF EXISTS (
    SELECT * 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_NAME = 'HangHong' 
    AND COLUMN_NAME = 'trangThai'
)
BEGIN
    -- Xóa index nếu tồn tại
    IF EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_HangHong_TrangThai' AND object_id = OBJECT_ID('HangHong'))
    BEGIN
        DROP INDEX IX_HangHong_TrangThai ON HangHong;
        PRINT 'Đã xóa index IX_HangHong_TrangThai.';
    END

    -- Xóa check constraint nếu tồn tại
    IF EXISTS (SELECT 1 FROM sys.check_constraints WHERE name = 'CHK_HangHong_TrangThai')
    BEGIN
        ALTER TABLE HangHong DROP CONSTRAINT CHK_HangHong_TrangThai;
        PRINT 'Đã xóa constraint CHK_HangHong_TrangThai.';
    END

    -- Xóa default constraint nếu tồn tại
    DECLARE @DefaultConstraintName NVARCHAR(200);
    SELECT @DefaultConstraintName = name
    FROM sys.default_constraints
    WHERE parent_object_id = OBJECT_ID('HangHong')
    AND parent_column_id = (SELECT column_id FROM sys.columns WHERE object_id = OBJECT_ID('HangHong') AND name = 'trangThai');
    
    IF @DefaultConstraintName IS NOT NULL
    BEGIN
        EXEC('ALTER TABLE HangHong DROP CONSTRAINT ' + @DefaultConstraintName);
        PRINT 'Đã xóa default constraint ' + @DefaultConstraintName + '.';
    END

    -- Xóa cột trangThai
    ALTER TABLE HangHong DROP COLUMN trangThai;
    
    PRINT 'Đã xóa cột trangThai khỏi bảng HangHong thành công!';
END
ELSE
BEGIN
    PRINT 'Cột trangThai không tồn tại trong bảng HangHong.';
END
GO

