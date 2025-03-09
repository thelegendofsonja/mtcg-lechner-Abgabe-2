package game.restAPI.controller;

import game.restAPI.handler.SessionHandler;
import java.io.IOException;
import java.io.OutputStream;

public class SessionController {
    public void handleRequest(String method, OutputStream output, String body, String authenticatedUser, String path) throws IOException {
        SessionHandler.handleLogin(output, body);
    }
}
