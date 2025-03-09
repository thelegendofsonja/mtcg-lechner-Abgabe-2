package game.restAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import game.model.User;
import game.restAPI.repository.UserRepository;

import java.io.IOException;
import java.io.OutputStream;

public class LoginController implements Controller {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void handleRequest(String method, OutputStream output, String body, String authenticatedUser, String path) throws IOException {
        System.out.println("[DEBUG] Handling login request: " + method);

        if (!method.equalsIgnoreCase("POST")) {
            sendResponse(output, 405, "Method Not Allowed");
            return;
        }

        System.out.println("[DEBUG] Raw body: " + body);

        try {
            User loginRequest = objectMapper.readValue(body, User.class);
            boolean valid = userRepository.validateUser(loginRequest.getUsername(), loginRequest.getPassword());

            if (valid) {
                String token = loginRequest.getUsername() + "-mtcgToken";
                sendResponse(output, 200, token);
            } else {
                sendResponse(output, 401, "Login failed: Invalid username or password");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(output, 400, "Bad Request: Could not parse login data");
        }
    }

    private void sendResponse(OutputStream output, int statusCode, String message) throws IOException {
        String reason = switch (statusCode) {
            case 200 -> "OK";
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
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
