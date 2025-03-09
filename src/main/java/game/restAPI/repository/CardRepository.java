package game.restAPI.repository;

import game.database.DatabaseManager;
import game.model.Card;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CardRepository {

    public List<Card> getDeck(String username) {
        String query = """
        SELECT c.card_id, c.name, c.damage, c.element_type, c.monster_type
        FROM decks d
        JOIN cards c ON d.card1_id = c.card_id
        WHERE d.username = ?
        UNION ALL
        SELECT c.card_id, c.name, c.damage, c.element_type, c.monster_type
        FROM decks d
        JOIN cards c ON d.card2_id = c.card_id
        WHERE d.username = ?
        UNION ALL
        SELECT c.card_id, c.name, c.damage, c.element_type, c.monster_type
        FROM decks d
        JOIN cards c ON d.card3_id = c.card_id
        WHERE d.username = ?
        UNION ALL
        SELECT c.card_id, c.name, c.damage, c.element_type, c.monster_type
        FROM decks d
        JOIN cards c ON d.card4_id = c.card_id
        WHERE d.username = ?
    """;

        List<Card> deck = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, username);
            stmt.setString(3, username);
            stmt.setString(4, username);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                deck.add(new Card(
                        (UUID) rs.getObject("card_id"),
                        rs.getString("name"),
                        rs.getFloat("damage"),
                        rs.getString("element_type"),
                        rs.getBoolean("monster_type")
                ));
            }

        } catch (SQLException e) {
            System.err.println("[DB] Failed to fetch deck: " + e.getMessage());
        }

        return deck;
    }

    public boolean configureDeck(String username, String jsonBody) {
        // Parse input
        List<UUID> cardIds = parseCardIds(jsonBody);
        if (cardIds.size() != 4) {
            return false;
        }

        // Validate ownership
        if (!ownsAllCards(username, cardIds)) {
            return false;
        }

        String query = "INSERT INTO decks (username, card1_id, card2_id, card3_id, card4_id) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON CONFLICT (username) DO UPDATE SET " +
                "card1_id = EXCLUDED.card1_id, " +
                "card2_id = EXCLUDED.card2_id, " +
                "card3_id = EXCLUDED.card3_id, " +
                "card4_id = EXCLUDED.card4_id";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setObject(2, cardIds.get(0));
            stmt.setObject(3, cardIds.get(1));
            stmt.setObject(4, cardIds.get(2));
            stmt.setObject(5, cardIds.get(3));
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("[DB] Failed to configure deck: " + e.getMessage());
            return false;
        }
    }

    private boolean ownsAllCards(String username, List<UUID> cardIds) {
        if (cardIds.size() != 4) return false;

        String query = "SELECT COUNT(*) FROM cards WHERE username = ? AND card_id IN (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setObject(2, cardIds.get(0));
            stmt.setObject(3, cardIds.get(1));
            stmt.setObject(4, cardIds.get(2));
            stmt.setObject(5, cardIds.get(3));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 4;
            }

        } catch (SQLException e) {
            System.err.println("[DB] Ownership check failed: " + e.getMessage());
        }

        return false;
    }


    private List<UUID> parseCardIds(String body) {
        body = body.replaceAll("[\\[\\]\"]", "");
        String[] split = body.split(",");
        List<UUID> ids = new ArrayList<>();
        for (String s : split) {
            if (!s.trim().isEmpty()) {
                ids.add(UUID.fromString(s.trim()));
            }
        }
        return ids;
    }

}
