package game.restAPI.repository;

import game.database.DatabaseManager;
import game.model.Stats;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatsRepository {

    public Stats getStats(String username) {
        String query = "SELECT games_played, games_won, games_lost, elo FROM stats WHERE username = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Stats(
                        username,
                        rs.getInt("games_played"),
                        rs.getInt("games_won"),
                        rs.getInt("games_lost"),
                        rs.getInt("elo")
                );
            }

        } catch (SQLException e) {
            System.err.println("[DB] Failed to retrieve stats for " + username + ": " + e.getMessage());
        }

        return null;
    }

    public List<Stats> getScoreboard() {
        String query = "SELECT username, games_played, games_won, games_lost, elo FROM stats ORDER BY elo DESC";
        List<Stats> scoreboard = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                scoreboard.add(new Stats(
                        rs.getString("username"),
                        rs.getInt("games_played"),
                        rs.getInt("games_won"),
                        rs.getInt("games_lost"),
                        rs.getInt("elo")
                ));
            }

        } catch (SQLException e) {
            System.err.println("[DB] Failed to retrieve scoreboard: " + e.getMessage());
        }

        return scoreboard;
    }

    public Stats getStatsForUser(String username) {
        String sql = "SELECT games_played, games_won, games_lost, elo FROM stats WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Stats(
                        username,
                        rs.getInt("games_played"),
                        rs.getInt("games_won"),
                        rs.getInt("games_lost"),
                        rs.getInt("elo")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
