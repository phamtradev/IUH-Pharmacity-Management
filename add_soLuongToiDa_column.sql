-- Script SQL Server để thêm cột soLuongToiDa vào bảng KhuyenMai
-- Chạy script này trên database của bạn

USE [YourDatabaseName]; -- Thay đổi tên database của bạn
GO

-- Kiểm tra xem cột soLuongToiDa đã tồn tại chưa (sử dụng dynamic SQL để tránh lỗi parse)
DECLARE @ColumnExists BIT = 0;
DECLARE @SQL NVARCHAR(MAX);

-- Kiểm tra cột có tồn tại không
IF EXISTS (
    SELECT * 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_NAME = 'KhuyenMai' 
    AND COLUMN_NAME = 'soLuongToiDa'
)
BEGIN
    SET @ColumnExists = 1;
    PRINT 'Cột soLuongToiDa đã tồn tại trong bảng KhuyenMai.';
END
ELSE
BEGIN
    -- Thêm cột soLuongToiDa với kiểu INT, giá trị mặc định là 0
    SET @SQL = N'ALTER TABLE KhuyenMai ADD soLuongToiDa INT NOT NULL DEFAULT 0;';
    EXEC sp_executesql @SQL;
    PRINT 'Đã thêm cột soLuongToiDa vào bảng KhuyenMai thành công!';
END
GO

