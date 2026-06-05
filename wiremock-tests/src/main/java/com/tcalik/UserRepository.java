package com.tcalik;

import java.sql.*;

public class UserRepository {

    private final String jdbcUrl;
    private final String username;
    private final String password;

    public UserRepository(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    public void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL" +
                ")";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement()) {

            statement.execute(sql);
        }
    }

    public void save(User user) throws SQLException {
        String sql = "INSERT INTO users (id, name) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, user.getId());
            statement.setString(2, user.getName());

            statement.executeUpdate();
        }
    }

    public User findById(int id) throws SQLException {
        String sql = "SELECT id, name FROM users WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(
                            resultSet.getInt("id"),
                            resultSet.getString("name")
                    );
                }
            }
        }

        return null;
    }
}