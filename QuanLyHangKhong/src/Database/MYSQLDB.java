package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MYSQLDB {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/quanlychuyenbay"; 
    private static final String DB_USER = "root"; 
    private static final String DB_PASSWORD = "123"; 

    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            if (conn != null) {
                System.out.println("Kết nối đến cơ sở dữ liệu thành công!");
            }
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Kết nối CSDL thất bại.");
            return null;
        }
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
