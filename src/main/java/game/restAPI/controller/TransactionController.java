package game.restAPI.controller;

import game.restAPI.repository.PackageRepository;
import java.io.OutputStream;
import java.io.IOException;

public class TransactionController implements Controller {
    private final PackageRepository packageRepository;

    public TransactionController(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Override
    public void handleRequest(String method, OutputStream output, String body, String authenticatedUser, String path) throws IOException {
        System.out.println("[DEBUG] Handling transaction request: " + method);

        if (!method.equals("POST")) {
            sendResponse(output, 405, "Method Not Allowed");
            return;
        }

        // Simulate package acquisition logic
        boolean packageAcquired = acquirePackage(authenticatedUser);
        if (packageAcquired) {
            sendResponse(output, 201, "Package acquired successfully");
        } else {
            sendResponse(output, 403, "Not enough money or no packages available");
        }
    }

    private boolean acquirePackage(String username) {
        // TODO: Implement real package acquisition logic
        return Math.random() > 0.5; // Simulated success/failure
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
            case 409 -> "Conflict";
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
