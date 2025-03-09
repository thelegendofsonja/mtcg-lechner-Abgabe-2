package game.restAPI.controller;

import game.restAPI.repository.TradingRepository;
import game.restAPI.handler.TradingHandler;
import java.io.OutputStream;
import java.io.IOException;

public class TradingController implements Controller {
    private final TradingRepository tradingRepository;

    public TradingController(TradingRepository tradingRepository) {
        this.tradingRepository = tradingRepository;
    }

    @Override
    public void handleRequest(String method, OutputStream output, String body, String authenticatedUser, String path) throws IOException {
        System.out.println("[DEBUG] Handling trading request: " + method);

        try {
            switch (method) {
                case "GET":
                    TradingHandler.handleGetTrades(output, authenticatedUser);
                    break;
                case "POST":
                    TradingHandler.handleCreateTrade(output, body, authenticatedUser);
                    break;
                case "DELETE":
                    TradingHandler.handleDeleteTrade(output, body, authenticatedUser);
                    break;
                default:
                    sendResponse(output, 405, "Method Not Allowed");
            }
        } catch (Exception e) {
            System.err.println("[ERROR] TradingController failed: " + e.getMessage());
            sendResponse(output, 500, "Internal Server Error");
        }
    }

    private void sendResponse(OutputStream output, int statusCode, String message) throws IOException {
        String reasonPhrase = switch (statusCode) {
            case 200 -> "OK";
            case 201 -> "Created";
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 405 -> "Method Not Allowed";
            case 500 -> "Internal Server Error";
            default -> "Unknown";
        };

        String response = "HTTP/1.1 " + statusCode + " " + reasonPhrase + "\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + message.length() + "\r\n\r\n" +
                message;
        output.write(response.getBytes());
        output.flush();
        System.out.println("[DEBUG] Sent response: " + statusCode + " " + reasonPhrase);
    }
}
