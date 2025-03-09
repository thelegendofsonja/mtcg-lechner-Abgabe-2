package game.restAPI.handler;

import java.io.OutputStream;
import java.io.IOException;

public class TradingHandler {

    private TradingHandler() {
        // Prevent instantiation
    }

    public static void handleGetTrades(OutputStream output, String username) throws IOException {
        // Implement the logic for retrieving trades
        output.write("HTTP/1.1 200 OK\r\n\r\nTrades retrieved".getBytes());
    }

    public static void handleCreateTrade(OutputStream output, String body, String username) throws IOException {
        // Implement the logic for creating a trade
        output.write("HTTP/1.1 201 Created\r\n\r\nTrade created".getBytes());
    }

    public static void handleDeleteTrade(OutputStream output, String body, String username) throws IOException {
        // Implement the logic for deleting a trade
        output.write("HTTP/1.1 200 OK\r\n\r\nTrade deleted".getBytes());
    }
}
