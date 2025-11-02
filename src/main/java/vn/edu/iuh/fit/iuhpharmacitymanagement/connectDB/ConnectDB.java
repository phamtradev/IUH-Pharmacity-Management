/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author PhamTra
 */
public class ConnectDB {

    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=IUHPharmacityManagement;encrypt=false;trustServerCertificate=true;integratedSecurity=false";
    private static final String USER = "sa";
    private static final String PASSWORD = "123456A@$";///sapassword

    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Không thể load SQL Server JDBC Driver", e);
        }
    }

    /**
     * Tạo và trả về một connection MỚI mỗi lần gọi. Người gọi chịu trách nhiệm
     * đóng connection sau khi dùng xong (dùng try-with-resources).
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Test connection
    public static boolean testConnection() {
        try (Connection testConn = getConnection()) {
            return testConn != null && !testConn.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
