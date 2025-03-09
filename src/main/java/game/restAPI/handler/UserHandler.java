package game.restAPI.handler;

import java.io.OutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserHandler {
    private static final Map<String, String> users = new HashMap<>();

    public static Map<String, String> getUsers() {
        return users;
    }

    public static void handleUserRegistration(OutputStream output, String body) throws IOException {
        String username = null;
        String password = null;

        try {
            // Attempt to parse the body as JSON-like input
            if (body.contains("{") && body.contains("}")) {
                String[] keyValuePairs = body.replace("{", "").replace("}", "").replace("\"", "").split(",");
                for (String pair : keyValuePairs) {
                    String[] keyValue = pair.split(":");
                    if (keyValue.length == 2) {
                        if (keyValue[0].trim().equalsIgnoreCase("username")) {
                            username = keyValue[1].trim();
                        } else if (keyValue[0].trim().equalsIgnoreCase("password")) {
                            password = keyValue[1].trim();
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Log and proceed with error handling
            System.err.println("Failed to parse request body: " + body);
        }

        // Validate input
        if (username == null || password == null) {
            String response = "HTTP/1.1 400 Bad Request\r\nContent-Type: application/json\r\n\r\n" +
                    "{\"error\":\"Missing username or password.\"}";
            output.write(response.getBytes());
            return;
        }

        // Check if the user already exists
        if (users.containsKey(username)) {
            String response = "HTTP/1.1 409 Conflict\r\nContent-Type: application/json\r\n\r\n" +
                    "{\"error\":\"User already exists.\"}";
            output.write(response.getBytes());
            return;
        }

        // Register the user
        users.put(username, password);
        String response = "HTTP/1.1 201 Created\r\nContent-Type: application/json\r\n\r\n" +
                "{\"message\":\"User created successfully.\"}";
        output.write(response.getBytes());
    }
}
