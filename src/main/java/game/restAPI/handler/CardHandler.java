package game.restAPI.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class CardHandler {
    private static final Map<String, List<String>> userCards = new HashMap<>();
    private static final Map<String, Integer> userCoins = new HashMap<>();

    private static final String[] ALL_CARDS = {
            "FireDragon", "WaterGoblin", "NormalKnight", "FireElf", "WaterSpell",
            "FireSpell", "NormalSpell", "Kraken", "Wizard", "Ork"
    };

    public static void handleGetCards(OutputStream output, String username) throws IOException {
        List<String> cards = userCards.getOrDefault(username, new ArrayList<>());
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(cards);

        sendJsonResponse(output, 200, jsonResponse);
    }

    public static void handleBuyPackage(OutputStream output, String username) throws IOException {
        // Extract username from request (placeholder logic for now)
        if (username == null || username.isEmpty()) {
            sendJsonResponse(output, 401, new Gson().toJson(Map.of("message", "Unauthorized: Missing username")));
            return;
        }

        // Ensure the user has enough coins
        int coins = userCoins.getOrDefault(username, 20);
        if (coins < 5) {
            sendJsonResponse(output, 400, new Gson().toJson(Map.of("message", "Not enough coins to buy a package.")));
            return;
        }

        // Deduct coins
        userCoins.put(username, coins - 5);

        // Generate 5 random cards
        List<String> newCards = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            newCards.add(ALL_CARDS[random.nextInt(ALL_CARDS.length)]);
        }

        // Add cards to user's stack
        userCards.computeIfAbsent(username, k -> new ArrayList<>()).addAll(newCards);

        Gson gson = new Gson();
        sendJsonResponse(output, 200, gson.toJson(Map.of("new_cards", newCards)));
    }

    private static void sendJsonResponse(OutputStream output, int statusCode, String json) throws IOException {
        String response = "HTTP/1.1 " + statusCode + " OK\r\n" +
                "Content-Type: application/json\r\n" +
                "Content-Length: " + json.length() + "\r\n\r\n" +
                json;
        output.write(response.getBytes());
        output.flush();
    }
}
