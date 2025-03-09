package game.restAPI.handler;

import java.io.OutputStream;
import java.io.IOException;
import java.util.Map;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SessionHandler {
    private static final Map<String, String> users = UserHandler.getUsers();

    public static void handleLogin(OutputStream output, String body) throws IOException {
        String username;
        String password;

        try {
            JsonObject requestJson = JsonParser.parseString(body).getAsJsonObject();
            username = requestJson.get("Username").getAsString();
            password = requestJson.get("Password").getAsString();
        } catch (Exception e) {
            sendJsonResponse(output, 400, "Invalid JSON format", "Bad Request");
            return;
        }

        if (username == null || password == null) {
            sendJsonResponse(output, 400, "Missing username or password", "Bad Request");
            return;
        }

        if (users.containsKey(username) && users.get(username).equals(password)) {
            String token = username + "-mtcgToken";
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("token", token);
            sendJsonResponse(output, 200, jsonResponse.toString(), "OK");
        } else {
            sendJsonResponse(output, 401, "Invalid credentials", "Unauthorized");
        }
    }

    private static void sendJsonResponse(OutputStream output, int statusCode, String message, String statusText) throws IOException {
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("message", message);
        String response = "HTTP/1.1 " + statusCode + " " + statusText + "\r\n" +
                "Content-Type: application/json\r\n" +
                "Content-Length: " + jsonResponse.toString().length() + "\r\n\r\n" +
                jsonResponse;
        output.write(response.getBytes());
        output.flush();
    }
}
