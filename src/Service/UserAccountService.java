package Service;

import Database.MYSQLDB;
import Model.UserAccount;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserAccountService {
    // Hàm đăng nhập
    public UserAccount login(String username, String password) {
        Connection connection = null;
        UserAccount user = null;
        try {
            connection = MYSQLDB.getConnection();
            if (connection != null) {
                String query = "SELECT * FROM UserAccount WHERE username = ? AND password = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    user = new UserAccount(
                            resultSet.getInt("id"),
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MYSQLDB.closeConnection(connection);
        }
        return user;
    }

    // Hàm đăng ký
    public boolean register(String username, String password, String role) {
        Connection connection = null;
        try {
            connection = MYSQLDB.getConnection();
            if (connection != null) {
                // Kiểm tra xem tên đăng nhập đã tồn tại hay chưa
                String checkQuery = "SELECT COUNT(*) FROM UserAccount WHERE username = ?";
                PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
                checkStmt.setString(1, username);
                ResultSet resultSet = checkStmt.executeQuery();
                
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    return false; // Tên đăng nhập đã tồn tại
                }

                // Thêm tài khoản mới vào cơ sở dữ liệu
                String insertQuery = "INSERT INTO UserAccount (username, password, role) VALUES (?, ?, ?)";
                PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
                insertStmt.setString(1, username);
                insertStmt.setString(2, password);
                insertStmt.setString(3, role);

                int rowsAffected = insertStmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            MYSQLDB.closeConnection(connection);
        }
        return false;
    }

    // Hàm cập nhật tài khoản
    public boolean updateUserAccount(int id, String username, String password, String role) {
        Connection connection = null;
        try {
            connection = MYSQLDB.getConnection();
            if (connection != null) {
                String query = "UPDATE UserAccount SET username = ?, password = ?, role = ? WHERE id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, role);
                preparedStatement.setInt(4, id);

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0; // Trả về true nếu cập nhật thành công
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MYSQLDB.closeConnection(connection);
        }
        return false; // Trả về false nếu có lỗi
    }

    // Hàm xóa tài khoản
    public boolean deleteUserAccount(int id) {
        Connection connection = null;
        try {
            connection = MYSQLDB.getConnection();
            if (connection != null) {
                String query = "DELETE FROM UserAccount WHERE id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, id);
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0; // Trả về true nếu xóa thành công
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MYSQLDB.closeConnection(connection);
        }
        return false; // Trả về false nếu có lỗi
    }

    // Lấy tất cả tài khoản
    public List<UserAccount> getAllUserAccounts() {
        List<UserAccount> userAccounts = new ArrayList<>();
        Connection connection = null;
        try {
            connection = MYSQLDB.getConnection();
            if (connection != null) {
                String query = "SELECT * FROM UserAccount";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    UserAccount account = new UserAccount(
                            resultSet.getInt("id"),
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("role")
                    );
                    userAccounts.add(account);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MYSQLDB.closeConnection(connection);
        }
        return userAccounts;
    }

    // Kiểm tra xem tên đăng nhập có tồn tại không
    public boolean isUsernameExists(String username) {
        Connection connection = null;
        try {
            connection = MYSQLDB.getConnection();
            if (connection != null) {
                String query = "SELECT COUNT(*) FROM UserAccount WHERE username = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MYSQLDB.closeConnection(connection);
        }
        return false;
    }
}
