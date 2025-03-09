package game.restAPI.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class BattleHandler {
    private static final Queue<String> battleQueue = new LinkedList<>();
    private static final Map<String, List<String>> userDecks = new HashMap<>();
    private static final Random random = new Random();

    public static void handleStartBattle(OutputStream output, String username) throws IOException {
        synchronized (battleQueue) {
            if (battleQueue.isEmpty()) {
                // First player joins the queue
                battleQueue.add(username);
                sendJsonResponse(output, 200, new Gson().toJson(Map.of("message", "Waiting for an opponent...")));
            } else {
                // Match found! Start battle
                String opponent = battleQueue.poll();
                startBattle(username, opponent, output);
            }
        }
    }

    private static void startBattle(String player1, String player2, OutputStream output) throws IOException {
        List<String> deck1 = userDecks.getOrDefault(player1, new ArrayList<>());
        List<String> deck2 = userDecks.getOrDefault(player2, new ArrayList<>());

        if (deck1.size() != 4 || deck2.size() != 4) {
            sendJsonResponse(output, 400, new Gson().toJson(Map.of("message", "Both players must have a deck of 4 cards.")));
            return;
        }

        int rounds = 0;
        String winner = null;
        List<String> battleLog = new ArrayList<>();

        while (rounds < 100 && !deck1.isEmpty() && !deck2.isEmpty()) {
            rounds++;
            String card1 = deck1.get(random.nextInt(deck1.size()));
            String card2 = deck2.get(random.nextInt(deck2.size()));

            int damage1 = calculateDamage(card1, card2);
            int damage2 = calculateDamage(card2, card1);

            battleLog.add(player1 + " (" + card1 + ") [" + damage1 + " dmg] vs " +
                    player2 + " (" + card2 + ") [" + damage2 + " dmg]");

            if (damage1 > damage2) {
                deck2.remove(card2);
            } else if (damage2 > damage1) {
                deck1.remove(card1);
            }

            if (deck1.isEmpty()) {
                winner = player2;
            } else if (deck2.isEmpty()) {
                winner = player1;
            }
        }

        if (winner != null) {
            String loser = winner.equals(player1) ? player2 : player1;
            if (!userDecks.get(loser).isEmpty()) {
                String stolenCard = userDecks.get(loser).remove(0);
                userDecks.get(winner).add(stolenCard);
                battleLog.add(winner + " won and took " + stolenCard + " from " + loser);
            }
        } else {
            battleLog.add("Battle ended in a draw.");
        }

        sendJsonResponse(output, 200, new Gson().toJson(Map.of(
                "winner", winner != null ? winner : "draw",
                "log", battleLog
        )));
    }

    private static int calculateDamage(String attacker, String defender) {
        int baseDamage = random.nextInt(50) + 10;

        if (attacker.contains("Fire") && defender.contains("Normal")) baseDamage += 10;
        if (attacker.contains("Water") && defender.contains("Fire")) baseDamage += 10;
        if (attacker.contains("Normal") && defender.contains("Water")) baseDamage += 10;

        if (attacker.contains("Kraken") && defender.contains("Spell")) baseDamage = 1000;

        return baseDamage;
    }

    private static void sendJsonResponse(OutputStream output, int statusCode, String json) throws IOException {
        String response = "HTTP/1.1 " + statusCode + " OK\r\n" +
                "Content-Type: application/json\r\n" +
                "Content-Length: " + json.length() + "\r\n\r\n" +
                json;
        output.write(response.getBytes());
        output.flush();
    }
}
