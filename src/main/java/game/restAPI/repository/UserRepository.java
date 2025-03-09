package game.restAPI.repository;

import game.database.DatabaseManager;
import game.model.User;

import java.sql.*;

public class UserRepository {

    public boolean createUser(User user) {
        String sql = """
        INSERT INTO users (username, password, name, bio, image, coins, elo, wins, losses)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getName());      // might be null, that’s fine
            stmt.setString(4, user.getBio());
            stmt.setString(5, user.getImage());
            stmt.setInt(6, user.getCoins());        // defaults to 20 in User model
            stmt.setInt(7, user.getElo());          // defaults to 100
            stmt.setInt(8, user.getWins());         // 0
            stmt.setInt(9, user.getLosses());       // 0

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("[DB] Error creating user: " + e.getMessage());
            return false;
        }
    }


    public boolean userExists(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.err.println("[DB] Error checking if user exists: " + e.getMessage());
            return false;
        }
    }

    public boolean validateUser(String username, String password) {
        String sql = "SELECT 1 FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // user found
            }

        } catch (SQLException e) {
            System.err.println("[DB] Error validating user credentials: " + e.getMessage());
            return false;
        }
    }

    public User getUserProfile(String username) {
        String sql = "SELECT name, bio, image FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUsername(username);
                user.setName(rs.getString("name"));
                user.setBio(rs.getString("bio"));
                user.setImage(rs.getString("image"));
                return user;
            }
        } catch (SQLException e) {
            System.err.println("[DB] Error retrieving user profile: " + e.getMessage());
        }
        return null;
    }

    public boolean updateUserProfile(String username, User updatedUser) {
        String sql = "UPDATE users SET name = ?, bio = ?, image = ? WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, updatedUser.getName());
            stmt.setString(2, updatedUser.getBio());
            stmt.setString(3, updatedUser.getImage());
            stmt.setString(4, username);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("[DB] Error updating user profile: " + e.getMessage());
            return false;
        }
    }
}
