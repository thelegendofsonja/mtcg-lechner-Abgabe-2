package game.restAPI.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.time.Instant;
import game.Main;

public class HealthCheckController implements Controller {
    @Override
    public void handleRequest(String method, OutputStream output, String body, String authenticatedUser, String path) throws IOException {
        if (!method.equalsIgnoreCase("GET")) {
            sendResponse(output, 405, "Method Not Allowed");
            return;
        }

        Duration uptime = Duration.between(Main.serverStartTime, Instant.now());

        long minutes = uptime.toMinutes();
        long seconds = uptime.minusMinutes(minutes).getSeconds();

        String message = "Server has been up for " + minutes + " minute(s) and " + seconds + " second(s).";
        sendResponse(output, 200, message);
    }

    private void sendResponse(OutputStream output, int statusCode, String message) throws IOException {
        String reason = switch (statusCode) {
            case 200 -> "OK";
            case 405 -> "Method Not Allowed";
            default -> "Error";
        };

        String response = "HTTP/1.1 " + statusCode + " " + reason + "\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + message.length() + "\r\n\r\n" +
                message;

        output.write(response.getBytes());
        output.flush();
    }
}
