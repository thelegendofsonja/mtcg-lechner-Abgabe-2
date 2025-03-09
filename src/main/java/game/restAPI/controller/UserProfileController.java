package game.restAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import game.model.User;
import game.restAPI.HttpStatus;
import game.restAPI.repository.UserRepository;

import java.io.IOException;
import java.io.OutputStream;

public class UserProfileController implements Controller {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void handleRequest(String method, OutputStream output, String path, String body, String authenticatedUser) throws IOException {
        String requestedUser = path.substring(path.lastIndexOf("/") + 1);

        if (method.equalsIgnoreCase("GET")) {
            handleGet(requestedUser, output);
        } else if (method.equalsIgnoreCase("PUT")) {
            handlePut(requestedUser, authenticatedUser, body, output);
        } else {
            sendResponse(output, HttpStatus.FORBIDDEN, "Method Not Allowed");
        }
    }


    private void handleGet(String username, OutputStream output) throws IOException {
        User user = userRepository.getUserProfile(username);
        if (user != null) {
            String json = objectMapper.writeValueAsString(user);
            sendJsonResponse(output, HttpStatus.OK, json);
        } else {
            sendResponse(output, HttpStatus.NOT_FOUND, "User not found");
        }
    }

    private void handlePut(String targetUser, String tokenUser, String body, OutputStream output) throws IOException {
        if (!targetUser.equals(tokenUser)) {
            sendResponse(output, HttpStatus.FORBIDDEN, "Forbidden: You can only update your own profile");
            return;
        }

        try {
            System.out.println("[DEBUG] Raw body: " + body);
            User updatedProfile = objectMapper.readValue(body, User.class);
            boolean success = userRepository.updateUserProfile(tokenUser, updatedProfile);

            if (success) {
                sendResponse(output, HttpStatus.OK, "Profile updated successfully");
            } else {
                sendResponse(output, HttpStatus.NOT_FOUND, "User not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(output, HttpStatus.BAD_REQUEST, "Bad Request: Invalid profile data");
        }
    }

    private String extractUsernameFromPath(String path) {
        if (path == null || !path.startsWith("/users/")) {
            return null;
        }
        return path.substring("/users/".length());
    }

    private void sendResponse(OutputStream output, HttpStatus status, String message) throws IOException {
        String response = status.getStatusLine() + "\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + message.length() + "\r\n\r\n" +
                message;
        output.write(response.getBytes());
        output.flush();
    }

    private void sendJsonResponse(OutputStream output, HttpStatus status, String json) throws IOException {
        String response = status.getStatusLine() + "\r\n" +
                "Content-Type: application/json\r\n" +
                "Content-Length: " + json.length() + "\r\n\r\n" +
                json;
        output.write(response.getBytes());
        output.flush();
    }
}
