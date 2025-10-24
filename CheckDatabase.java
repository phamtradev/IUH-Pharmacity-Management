import java.sql.*;

public class CheckDatabase {
    public static void main(String[] args) {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=IUHPharmacityManagement;encrypt=false;trustServerCertificate=true;integratedSecurity=false";
        String user = "sa";
        String password = "sapassword";
        
        System.out.println("Checking database values for vaiTro...\n");
        
        try {
            // Load driver từ Maven dependency
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✓ Connected to database successfully\n");
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT TOP 5 maNhanVien, tenNhanVien, vaiTro FROM NhanVien");
            
            System.out.println("=== FIRST 5 EMPLOYEES ===\n");
            int count = 0;
            while (rs.next()) {
                count++;
                String ma = rs.getString("maNhanVien");
                String ten = rs.getString("tenNhanVien");
                String vai = rs.getString("vaiTro");
                
                System.out.println(count + ". " + ma + " - " + ten);
                System.out.println("   vaiTro = [" + vai + "]");
                System.out.println("   Length: " + vai.length());
                
                // Convert to bytes to see encoding
                byte[] bytes = vai.getBytes("UTF-8");
                System.out.print("   Bytes: ");
                for (byte b : bytes) {
                    System.out.printf("%02X ", b);
                }
                System.out.println();
                
                // Check validation
                vai = vai.trim();
                if (vai.equalsIgnoreCase("Nhân viên") || vai.equalsIgnoreCase("Quản lý")) {
                    System.out.println("   ✓ VALID (with equalsIgnoreCase)");
                } else {
                    System.out.println("   ✗ INVALID");
                    System.out.println("   Expected: 'Nhân viên' or 'Quản lý'");
                    System.out.println("   Got: '" + vai + "'");
                }
                System.out.println();
            }
            
            if (count == 0) {
                System.out.println("No employees found in database!");
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

