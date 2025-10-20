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

    private static ConnectDB instance = null;
    private static Connection connection = null;

    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=IUHPharmacityManagementV3;encrypt=false;trustServerCertificate=true;integratedSecurity=false";
    private static final String USER = "sa";
    private static final String PASSWORD = "sapassword";

    private ConnectDB() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static ConnectDB getInstance() {
        if (instance == null) {
            instance = new ConnectDB();
        }
        return instance;
    }

    public static Connection getConnection() {
        if (connection == null) {
            getInstance();
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Test connection
    public static boolean testConnection() {
        try {
            Connection testConn = ConnectDB.getConnection();
            return testConn != null && !testConn.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
