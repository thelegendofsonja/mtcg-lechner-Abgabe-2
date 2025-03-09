package game.restAPI.controller;

import game.restAPI.repository.CardRepository;
import game.restAPI.handler.CardHandler;
import java.io.OutputStream;
import java.io.IOException;

public class CardController implements Controller {
    private final CardRepository cardRepository;

    public CardController(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public void handleRequest(String method, OutputStream output, String body, String authenticatedUser, String path) throws IOException {
        switch (method) {
            case "GET":
                CardHandler.handleGetCards(output, authenticatedUser);
                break;
            case "POST":
                CardHandler.handleBuyPackage(output, authenticatedUser);
                break;
            default:
                output.write("HTTP/1.1 405 Method Not Allowed\r\n\r\n".getBytes());
        }
    }
}
