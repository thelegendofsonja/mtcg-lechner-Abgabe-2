package game;

import game.restAPI.HttpServer;

import java.time.Instant;

public class Main {
    public static final Instant serverStartTime = Instant.now();
    public static void main(String[] args) {
        // Define the port for the server to listen on
        int port = 10001; // Change this if needed

        // Print a message indicating the server is starting
        System.out.println("Starting the MTCG server...");

        // Create and start the HTTP server
        try {
            HttpServer server = new HttpServer(port, 10); // 10 threads in the thread pool
            server.start(); // Start the server
        } catch (Exception e) {
            System.err.println("Failed to start the server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
