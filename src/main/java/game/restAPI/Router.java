package game.restAPI;

import game.restAPI.controller.*;
import game.restAPI.repository.*;

import java.util.HashMap;
import java.util.Map;

public class Router {
    private final Map<String, Controller> controllers = new HashMap<>();
    private final UserRepository userRepository = new UserRepository();  // Shared

    public Router() {
        registerRoutes();
    }

    private void registerRoutes() {
        PackageRepository packageRepository = new PackageRepository();
        CardRepository cardRepository = new CardRepository();
        GameRepository gameRepository = new GameRepository();
        TradingRepository tradingRepository = new TradingRepository();
        StatsRepository statsRepository = new StatsRepository();

        // Users & sessions
        controllers.put("/users", new UserController(userRepository));
        controllers.put("/sessions", new LoginController(userRepository));

        // Packages
        controllers.put("/packages", new PackagesController(packageRepository));
        controllers.put("/transactions/packages", new TransactionController(packageRepository));

        // Cards & decks
        controllers.put("/cards", new CardController(cardRepository));
        controllers.put("/deck", new DeckController(cardRepository)); // basic /deck route
        // dynamic /deck?format=plain handled in ClientHandler or DeckController

        // Battles
        controllers.put("/battles", new BattleController(gameRepository));

        // Tradings
        controllers.put("/tradings", new TradingController(tradingRepository));
        // dynamic /tradings/{id} handled in ClientHandler or TradingController

        // Stats & Scoreboard
        controllers.put("/stats", new StatsController(statsRepository));
        controllers.put("/score", new ScoreboardController(statsRepository));

        // Unique Feature: TimeController & HealthCheckUp
        controllers.put("/time", new TimeController());
        controllers.put("/healthcheck", new HealthCheckController());

    }

    public Controller getController(String path) {
        if (path.startsWith("/users/") && path.length() > "/users/".length()) {
            return new UserProfileController(userRepository);
        }

        // For dynamic deck display
        if (path.startsWith("/deck?format=plain")) {
            return controllers.get("/deck");
        }

        // For /tradings/{id}
        if (path.startsWith("/tradings/")) {
            return controllers.get("/tradings");
        }

        return controllers.get(path);
    }
}
