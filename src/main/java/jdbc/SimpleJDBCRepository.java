package jdbc;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleJDBCRepository {
    private static final String createUserSQL = "INSERT INTO myusers (firstname, lastname, age) VALUES (?, ?, ?)";
    private static final String updateUserSQL = "UPDATE myusers SET firstname = ?, lastname = ?, age = ? WHERE id = ?";
    private static final String deleteUser = "DELETE FROM myusers WHERE id = ?";
    private static final String findUserByIdSQL = "SELECT * FROM myusers WHERE id = ?";
    private static final String findUserByNameSQL = "SELECT * FROM myusers WHERE firstname = ?";
    private static final String findAllUserSQL = "SELECT * FROM myusers";
    private DataSource dataSource;
    private Connection connection = null;
    private PreparedStatement ps = null;
    private Statement st = null;

    {
        try {
            dataSource = CustomDataSource.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Long createUser(User user) throws SQLException{
        if (user.getFirstName() == null) {
            throw new IllegalArgumentException("Firstname cannot be null");
        }
        try (Connection connection = dataSource.getConnection(); PreparedStatement ps = connection.prepareStatement(createUserSQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setInt(3, user.getAge());
            ps.executeUpdate();

            ResultSet rs;
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            } else {
                throw new SQLException("Unable to create user, no ID obtained.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public User findUserById(Long userId) throws SQLException {
        try (Connection connection = dataSource.getConnection(); PreparedStatement ps = connection.prepareStatement(findUserByIdSQL)) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(rs.getLong("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getInt("age"));
            } else {
                return null;
            }
        }
    }

    public User findUserByName(String userName) throws SQLException {
        try (Connection connection = dataSource.getConnection(); PreparedStatement ps = connection.prepareStatement(findUserByNameSQL)) {
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(rs.getLong("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getInt("age"));
            } else {
                return null;
            }
        }
    }

    public List<User> findAllUser() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection(); PreparedStatement ps = connection.prepareStatement(findAllUserSQL)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                users.add(new User(rs.getLong("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getInt("age")));
            }
            return users;
        }
    }

    public User updateUser(User user) throws SQLException {
        try (Connection connection = dataSource.getConnection(); PreparedStatement ps = connection.prepareStatement(updateUserSQL)) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setInt(3, user.getAge());
            ps.setLong(4, user.getId());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 1) {
                return user;
            } else {
                return null;
            }
        }
    }

    public void deleteUser(Long userId) throws SQLException {
        try (Connection connection = dataSource.getConnection(); PreparedStatement ps = connection.prepareStatement(deleteUser)) {
            ps.setLong(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
