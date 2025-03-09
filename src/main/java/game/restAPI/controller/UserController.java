package game.restAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import game.model.User;
import game.restAPI.HttpStatus;
import game.restAPI.repository.UserRepository;

import java.io.IOException;
import java.io.OutputStream;

public class UserController implements Controller {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void handleRequest(String method, OutputStream output, String body, String authenticatedUser, String path) throws IOException {
        System.out.println("[DEBUG] Handling user request: " + method);

        if (!method.equalsIgnoreCase("POST")) {
            sendResponse(output, HttpStatus.BAD_REQUEST, "Method Not Allowed");
            return;
        }

        System.out.println("[DEBUG] Raw body: " + body);

        try {
            User user = objectMapper.readValue(body, User.class);

            if (userRepository.userExists(user.getUsername())) {
                sendResponse(output, HttpStatus.CONFLICT, "User already exists");
                return;
            }

            boolean created = userRepository.createUser(user);
            if (created) {
                sendResponse(output, HttpStatus.CREATED, "User registered successfully");
            } else {
                sendResponse(output, HttpStatus.INTERNAL_ERROR, "Failed to create user");
            }

        } catch (Exception e) {
            System.err.println("[ERROR] Failed to parse user data: " + e.getMessage());
            sendResponse(output, HttpStatus.BAD_REQUEST, "Invalid user data");
        }
    }

    private void sendResponse(OutputStream output, HttpStatus status, String message) throws IOException {
        String response = status.getStatusLine() + "\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + message.length() + "\r\n\r\n" +
                message;
        output.write(response.getBytes());
        output.flush();
        System.out.println("[DEBUG] Sent response: " + status.getStatusLine());
    }
}
