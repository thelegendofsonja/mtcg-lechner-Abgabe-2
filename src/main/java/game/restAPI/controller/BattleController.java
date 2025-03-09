package game.restAPI.controller;

import game.restAPI.repository.GameRepository;
import game.restAPI.handler.BattleHandler;
import java.io.OutputStream;
import java.io.IOException;

public class BattleController implements Controller {
    private final GameRepository gameRepository;

    public BattleController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public void handleRequest(String method, OutputStream output, String body, String authenticatedUser, String path) throws IOException {
        if ("POST".equals(method)) {
            BattleHandler.handleStartBattle(output, authenticatedUser);
        } else {
            output.write("HTTP/1.1 405 Method Not Allowed\r\n\r\n".getBytes());
        }
    }
}
