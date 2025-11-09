-- Script SQL Server đơn giản để thêm cột soLuongToiDa vào bảng KhuyenMai
-- Chạy script này trên database của bạn

USE [YourDatabaseName]; -- Thay đổi tên database của bạn
GO

-- Thêm cột soLuongToiDa với kiểu INT, giá trị mặc định là 0
-- Nếu cột đã tồn tại, sẽ báo lỗi - bạn có thể bỏ qua lỗi đó
ALTER TABLE KhuyenMai
ADD soLuongToiDa INT NOT NULL DEFAULT 0;
GO

