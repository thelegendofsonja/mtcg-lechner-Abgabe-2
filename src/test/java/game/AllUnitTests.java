package game;// 20 unit tests

import game.restAPI.UserManager;
import game.restAPI.repository.PackageRepository;
import game.restAPI.repository.CardRepository;
import game.restAPI.repository.StatsRepository;
import game.restAPI.repository.TradingRepository;
import game.model.Card;
import game.model.Deck;
import game.model.Package;
import game.model.Stats;
import game.model.User;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


// UserManagerTest.java (5 tests)
public class AllUnitTests {
    UserManager userManager = new UserManager(); // Make sure this is correctly instantiated
    PackageRepository packageRepository = new PackageRepository();
    CardRepository cardRepository = new CardRepository();
    StatsRepository statsRepository = new StatsRepository();
    TradingRepository tradingRepository = new TradingRepository();
    // Test that a new user can be registered
    @Test
    void testRegisterNewUserSuccess() {
        String response = String.valueOf(userManager.registerUser("testuser", "password"));
        assertTrue(response.contains("success") || response.contains("201"));
    }

    // Test that registering the same username twice fails
    @Test
    void testRegisterDuplicateUserFails() {
        userManager.registerUser("duplicateuser", "123");
        String response = String.valueOf(userManager.registerUser("duplicateuser", "456"));
        assertTrue(response.contains("already exists") || response.contains("409"));
    }

    // Test login with correct credentials
    @Test
    void testLoginWithCorrectCredentials() {
        userManager.registerUser("loginuser", "pass");
        String response = String.valueOf(userManager.authenticateUser("loginuser", "pass"));
        assertTrue(response.contains("token") || response.contains("200"));
    }

    // Test login fails with incorrect password
    @Test
    void testLoginWithWrongPasswordFails() {
        userManager.registerUser("wrongpassuser", "correct");
        String response = String.valueOf(userManager.authenticateUser("wrongpassuser", "wrong"));
        assertTrue(response.contains("unauthorized") || response.contains("401"));
    }

    // Test that the token format is correct
    @Test
    void testTokenGenerationFormat() {
        userManager.registerUser("tokenuser", "abc");
        String token = String.valueOf(userManager.authenticateUser("tokenuser", "abc"));
        assertTrue(token.contains("Basic tokenuser-mtcgToken"));
    }

    // PackageRepositoryTest.java (3 tests)

    // Test that creating a package makes it available for purchase
    @Test
    void testCreatePackageStoresCorrectly() {
        packageRepository.createPackage(new Package(List.of(new Card(UUID.randomUUID(), "Goblin", 10, "Fire", true))));
        assertFalse(packageRepository.getAvailablePackages().isEmpty());
    }

    // Test that a bought package is removed from availability
    @Test
    void testBuyPackageRemovesFromAvailable() {
        packageRepository.createPackage(new Package(List.of(new Card(UUID.randomUUID(), "Elf", 20, "Water", true))));
        packageRepository.buyPackage("testuser");
        assertTrue(packageRepository.getAvailablePackages().isEmpty());
    }

    // Test that buying with no packages throws an error
    @Test
    void testBuyPackageFailsWhenNoneAvailable() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            packageRepository.buyPackage("someone");
        });
        assertTrue(exception.getMessage().contains("No packages available"));
    }

    // CardRepositoryTest.java (2 tests)

    // Test that a user receives their cards after a purchase
    @Test
    void testCardBelongsToUserAfterPurchase() {
        packageRepository.createPackage(new Package(List.of(new Card(UUID.randomUUID(), "Dragon", 50, "Fire", true))));
        packageRepository.buyPackage("userX");
        List<Card> cards = cardRepository.getDeck("userX");
        assertTrue(cards.stream().anyMatch(c -> c.getName().equals("Dragon")));
    }

    // Test that getting cards for a user returns a list
    @Test
    void testGetCardsForUserReturnsCorrectCards() {
        List<Card> cards = cardRepository.getDeck("user1");
        assertNotNull(cards);
    }

    // DeckTest.java (2 tests)

    // Test that only 4 cards are allowed in the deck
    @Test
    void testDeckAcceptsOnly4Cards() {
        Deck deck = new Deck();
        List<Card> cards = List.of(
                new Card("1", "A", 10),
                new Card("2", "B", 10),
                new Card("3", "C", 10),
                new Card("4", "D", 10),
                new Card("5", "E", 10)
        );
        Exception ex = assertThrows(IllegalArgumentException.class, () -> deck.setCards(cards));
        assertTrue(ex.getMessage().contains("4 cards"));
    }

    // Test the sum of damage of all cards in deck
    @Test
    void testGetDamageSumFromDeck() {
        Deck deck = new Deck();
        deck.setCards(List.of(
                new Card(UUID.randomUUID(), "Goblin", 30, "Fire", true),
                new Card(UUID.randomUUID(), "Knight", 40, "Water", true),
                new Card(UUID.randomUUID(), "Elf", 20, "Normal", true),
                new Card(UUID.randomUUID(), "Orc", 10, "Fire", true)
        ));
        int sum = deck.getCards().stream().mapToInt(Card::getDamage).sum();
        assertEquals(100, sum);
    }

    // Test that setting a deck with duplicate card IDs fails
    @Test
    void testDeckRejectsDuplicateCards() {
        Deck deck = new Deck();
        List<Card> duplicateCards = List.of(
                new Card(UUID.randomUUID(), "Goblin", 20, "Fire", true),
                new Card(UUID.randomUUID(), "Goblin", 20, "Fire", true),
                new Card(UUID.randomUUID(), "Elf", 30, "Water", true),
                new Card(UUID.randomUUID(), "Orc", 10, "Fire", true)
        );

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            deck.setCards(duplicateCards);
        });

        assertTrue(ex.getMessage().contains("duplicate"));
    }

    // ProfileControllerTest.java (2 tests)

    // Test that user profile bio is updated and stored
    @Test
    void testGetProfileDataReturnsCorrectInfo() {
        userManager.registerUser("alex", "123");
        userManager.updateUserData("alex", "bio", "image.png");
        User user = userManager.getUser("alex");
        assertEquals("bio", user.getBio());
    }

    // Test that updating another user's profile fails
    @Test
    void testUpdateProfileFailsForOtherUsers() {
        Exception ex = assertThrows(UnauthorizedException.class, () -> {
            userManager.updateUserData("otheruser", "bio", "image.png", "token-from-different-user");
        });
        assertTrue(ex.getMessage().contains("unauthorized"));
    }

    // StatsControllerTest.java (2 tests)

    // Test that new user has initial stats with 0 ELO
    @Test
    void testStatsAreInitializedOnRegistration() {
        userManager.registerUser("newplayer", "pass");
        Stats stats = statsRepository.getStats("newplayer");
        assertNotNull(stats);
        assertEquals(0, stats.getElo());
    }

    // Test that scoreboard is sorted by ELO
    @Test
    void testScoreboardReturnsSortedPlayers() {
        List<Stats> scoreboard = statsRepository.getScoreboard();
        for (int i = 1; i < scoreboard.size(); i++) {
            assertTrue(scoreboard.get(i - 1).getElo() >= scoreboard.get(i).getElo());
        }
    }


    // TradingControllerTest.java (2 tests)

    // Test that a trade is created and visible
    @Test
    void testCreateTradeAddsOffer() {
        Trade trade = new Trade("card123", "Monster", 40);
        tradingRepository.createTrade("trader1", trade);
        assertTrue(tradingRepository.getAllTrades().contains(trade));
    }

    // Test that two cards are swapped after trade
    @Test
    void testExecuteTradeSwapsCards() {
        tradingRepository.createTrade("trader1", new Trade("cardA", "Spell", 20));
        tradingRepository.executeTrade("cardB", "trader2", "cardA");
        List<Card> cards1 = cardRepository.getCards("trader1");
        List<Card> cards2 = cardRepository.getCards("trader2");
        assertTrue(cards1.stream().anyMatch(c -> c.getId().equals("cardB")));
        assertTrue(cards2.stream().anyMatch(c -> c.getId().equals("cardA")));
    }

    // AuthTokenValidationTest.java (1 test)

    // Test that a user cannot access another's protected route with wrong token
    @Test
    void testTokenAccessRestriction() {
        userManager.registerUser("alice", "pass");
        userManager.registerUser("bob", "pass");

        String bobToken = userManager.loginUser("bob", "pass");

        Exception ex = assertThrows(UnauthorizedException.class, () -> {
            userManager.updateUserData("alice", "newBio", "pic.png", bobToken);
        });

        assertTrue(ex.getMessage().contains("unauthorized"));
    }
}