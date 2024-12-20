package Service;

import Database.MYSQLDB;
import Model.UserAccount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAccountService {

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

        return user; // Trả về null nếu không tìm thấy
    }
}
