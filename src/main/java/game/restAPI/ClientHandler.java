package game.restAPI;

import game.restAPI.controller.Controller;
import game.restAPI.repository.UserRepository;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final Router router;

    public ClientHandler(Socket socket, Router router) {
        this.socket = socket;
        this.router = router;
    }

    @Override
    public void run() {
        try (InputStream input = socket.getInputStream();
             OutputStream output = socket.getOutputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {

            System.out.println("[DEBUG] Client connected from " + socket.getInetAddress());

            String requestLine = reader.readLine();
            if (requestLine == null || requestLine.isEmpty()) {
                sendResponse(output, HttpStatus.BAD_REQUEST, "Bad Request: Empty Request");
                return;
            }

            System.out.println("[DEBUG] Request Line: " + requestLine);
            String[] requestParts = requestLine.split(" ");
            if (requestParts.length < 2) {
                sendResponse(output, HttpStatus.BAD_REQUEST, "Bad Request: Invalid Request Line");
                return;
            }

            String method = requestParts[0];
            String path = requestParts[1];

            Map<String, String> headers = new HashMap<>();
            int contentLength = 0;
            String username = null;

            // Read headers
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                String[] headerParts = line.split(": ", 2);
                if (headerParts.length == 2) {
                    headers.put(headerParts[0], headerParts[1]);

                    if (headerParts[0].equalsIgnoreCase("Content-Length")) {
                        try {
                            contentLength = Integer.parseInt(headerParts[1]);
                        } catch (NumberFormatException e) {
                            sendResponse(output, HttpStatus.BAD_REQUEST, "Bad Request: Invalid Content-Length");
                            return;
                        }
                    }

                    if (headerParts[0].equalsIgnoreCase("Authorization")) {
                        username = extractUsernameFromToken(headerParts[1]);
                    }
                }
            }

            // Check for auth-required routes
            boolean requiresAuth = path.startsWith("/cards") || path.startsWith("/deck")
                    || path.startsWith("/battles") || path.startsWith("/packages")
                    || path.startsWith("/tradings") || path.startsWith("/stats")
                    || path.startsWith("/users/");

            if (requiresAuth) {
                if (username == null || username.isEmpty()) {
                    sendResponse(output, HttpStatus.UNAUTHORIZED, "Unauthorized: Missing or invalid token");
                    return;
                }

                UserRepository userRepo = new UserRepository();
                if (!userRepo.userExists(username)) {
                    sendResponse(output, HttpStatus.UNAUTHORIZED, "Unauthorized: Invalid token user");
                    return;
                }
            }

            // Read body if provided
            String body = "";
            if (contentLength > 0) {
                char[] buffer = new char[contentLength];
                int totalRead = 0;
                while (totalRead < contentLength) {
                    int readNow = reader.read(buffer, totalRead, contentLength - totalRead);
                    if (readNow == -1) break;
                    totalRead += readNow;
                }
                body = new String(buffer, 0, totalRead);
                System.out.println("[DEBUG] Body: " + body);
            }

            // Route the request
            Controller controller = router.getController(path);
            if (controller != null) {
                System.out.println("[DEBUG] Routing to controller for path: " + path);
                controller.handleRequest(method, output, body, username, path);
            } else {
                sendResponse(output, HttpStatus.NOT_FOUND, "Not Found: No matching route");
            }

        } catch (IOException e) {
            System.err.println("[ERROR] " + e.getMessage());
        } finally {
            try {
                socket.close();
                System.out.println("[DEBUG] Connection closed");
            } catch (IOException e) {
                System.err.println("[ERROR] Closing socket failed: " + e.getMessage());
            }
        }
    }

    private void sendResponse(OutputStream output, HttpStatus status, String message) throws IOException {
        String response = status.getStatusLine() + "\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + message.length() + "\r\n\r\n" +
                message;
        output.write(response.getBytes());
        output.flush();
        System.out.println("[DEBUG] Response: " + status.getStatusLine());
    }

    private String extractUsernameFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        String token = authHeader.substring(7).trim();
        if (!token.endsWith("-mtcgToken") || token.length() <= 10) return null;
        return token.substring(0, token.length() - 10);
    }
}
