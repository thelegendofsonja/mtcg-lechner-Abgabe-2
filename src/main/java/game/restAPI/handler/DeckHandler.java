package game.restAPI.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DeckHandler {
    private static final Map<String, List<String>> userDecks = new HashMap<>();
    private static final Map<String, List<String>> userCards = new HashMap<>();

    public static void handleGetDeck(OutputStream output, String username) throws IOException {
        List<String> deck = userDecks.get(username);

        if (deck == null || deck.isEmpty()) {
            sendJsonResponse(output, 404, new Gson().toJson(Map.of("message", "No deck set. Please select 4 cards.")));
            return;
        }

        sendJsonResponse(output, 200, new Gson().toJson(deck));
    }

    public static void handleSetDeck(OutputStream output, String body, String username) throws IOException {
        try {
            JsonArray cardArray = JsonParser.parseString(body).getAsJsonArray();
            List<String> selectedCards = new ArrayList<>();

            for (int i = 0; i < cardArray.size(); i++) {
                selectedCards.add(cardArray.get(i).getAsString());
            }

            if (selectedCards.size() != 4) {
                sendJsonResponse(output, 400, new Gson().toJson(Map.of("message", "Deck must contain exactly 4 cards.")));
                return;
            }

            // Ensure user owns all selected cards
            List<String> userOwnedCards = userCards.getOrDefault(username, new ArrayList<>());
            if (!userOwnedCards.containsAll(selectedCards)) {
                sendJsonResponse(output, 400, new Gson().toJson(Map.of("message", "You can only set cards that you own.")));
                return;
            }

            // Set the user's deck
            userDecks.put(username, new ArrayList<>(selectedCards));
            sendJsonResponse(output, 200, new Gson().toJson(Map.of("message", "Deck successfully set.")));
        } catch (Exception e) {
            sendJsonResponse(output, 400, new Gson().toJson(Map.of("message", "Invalid JSON format.")));
        }
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
