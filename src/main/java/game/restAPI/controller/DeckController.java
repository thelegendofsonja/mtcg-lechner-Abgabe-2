package game.restAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import game.model.Card;
import game.restAPI.HttpStatus;
import game.restAPI.repository.CardRepository;

import java.io.OutputStream;
import java.io.IOException;
import java.util.List;

public class DeckController implements Controller {
    private final CardRepository cardRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DeckController(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public void handleRequest(String method, OutputStream output, String body, String authenticatedUser, String path) throws IOException {
        try {
            if (!method.equalsIgnoreCase("GET") && !method.equalsIgnoreCase("PUT")) {
                sendResponse(output, HttpStatus.METHOD_NOT_ALLOWED, "Method Not Allowed");
                return;
            }

            if (method.equalsIgnoreCase("GET")) {
                List<Card> deck = cardRepository.getDeck(authenticatedUser);
                if (deck == null || deck.isEmpty()) {
                    sendJsonResponse(output, HttpStatus.NOT_FOUND, "{\"message\":\"No deck set. Please select 4 cards.\"}");
                    return;
                }

                if (path.contains("?format=plain")) {
                    StringBuilder responseText = new StringBuilder();
                    for (Card card : deck) {
                        responseText.append("- ").append(card.getName())
                                .append(" (").append(card.getDamage()).append(")\n");
                    }
                    sendTextResponse(output, HttpStatus.OK, responseText.toString().trim());
                } else {
                    String json = objectMapper.writeValueAsString(deck);
                    sendJsonResponse(output, HttpStatus.OK, json);
                }
            } else if (method.equalsIgnoreCase("PUT")) {
                boolean success = cardRepository.configureDeck(authenticatedUser, body);
                if (success) {
                    sendJsonResponse(output, HttpStatus.OK, "{\"message\":\"Deck configured successfully.\"}");
                } else {
                    sendJsonResponse(output, HttpStatus.BAD_REQUEST, "{\"message\":\"Deck must contain exactly 4 valid cards.\"}");
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // For debugging
            sendResponse(output, HttpStatus.INTERNAL_ERROR, "Internal server error: " + e.getMessage());
        }
    }


    private void sendJsonResponse(OutputStream output, HttpStatus status, String json) throws IOException {
        String response = status.getStatusLine() + "\r\n" +
                "Content-Type: application/json\r\n" +
                "Content-Length: " + json.length() + "\r\n\r\n" +
                json;
        output.write(response.getBytes());
        output.flush();
    }

    private void sendTextResponse(OutputStream output, HttpStatus status, String text) throws IOException {
        String response = status.getStatusLine() + "\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + text.length() + "\r\n\r\n" +
                text;
        output.write(response.getBytes());
        output.flush();
    }

    private void sendResponse(OutputStream output, HttpStatus status, String message) throws IOException {
        String response = status.getStatusLine() + "\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + message.length() + "\r\n\r\n" +
                message;
        output.write(response.getBytes());
        output.flush();
    }
}
