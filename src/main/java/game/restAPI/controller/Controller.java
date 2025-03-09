package game.restAPI.controller;

import java.io.IOException;
import java.io.OutputStream;

public interface Controller {
    void handleRequest(String method, OutputStream output, String path, String body, String authenticatedUser) throws IOException;
}
