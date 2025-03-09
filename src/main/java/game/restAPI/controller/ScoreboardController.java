package game.restAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import game.model.Stats;
import game.restAPI.HttpStatus;
import game.restAPI.repository.StatsRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ScoreboardController implements Controller {
    private final StatsRepository statsRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ScoreboardController(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @Override
    public void handleRequest(String method, OutputStream output, String path, String body, String authenticatedUser) throws IOException {
        if (!method.equalsIgnoreCase("GET")) {
            sendResponse(output, HttpStatus.METHOD_NOT_ALLOWED, "Method Not Allowed");
            return;
        }

        List<Stats> scoreboard = statsRepository.getScoreboard();
        String json = objectMapper.writeValueAsString(scoreboard);
        sendJsonResponse(output, HttpStatus.OK, json);
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
