package game.restAPI.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;

public class TimeController implements Controller {
    @Override
    public void handleRequest(String method, OutputStream output, String body, String authenticatedUser, String path) throws IOException {
        if (!method.equalsIgnoreCase("GET")) {
            sendResponse(output, 405, "Method Not Allowed");
            return;
        }

        String json = "{\"serverTime\": \"" + LocalDateTime.now().toString() + "\"}";
        sendResponse(output, 200, json, "application/json");
    }

    private void sendResponse(OutputStream output, int statusCode, String message, String contentType) throws IOException {
        String reason = switch (statusCode) {
            case 200 -> "OK";
            case 405 -> "Method Not Allowed";
            default -> "Error";
        };

        String response = "HTTP/1.1 " + statusCode + " " + reason + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Content-Length: " + message.length() + "\r\n\r\n" +
                message;

        output.write(response.getBytes());
        output.flush();
    }

    private void sendResponse(OutputStream output, int statusCode, String message) throws IOException {
        sendResponse(output, statusCode, message, "text/plain");
    }
}
