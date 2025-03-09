package game.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    public User() {}

    @JsonProperty("username")
    @JsonAlias("Username")
    private String username;

    @JsonProperty("password")
    @JsonAlias("Password")
    private String password;

    private String name;
    private String bio;
    private String image;

    private int coins = 20;
    private int elo = 100;
    private int wins = 0;
    private int losses = 0;

    private List<Card> stack; // Owned cards
    private Deck deck;        // Active deck

    // Constructors
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String name, String bio, String image,
                int coins, int elo, int wins, int losses) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.bio = bio;
        this.image = image;
        this.coins = coins;
        this.elo = elo;
        this.wins = wins;
        this.losses = losses;
    }

    // Getters & Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public int getCoins() { return coins; }
    public void setCoins(int coins) { this.coins = coins; }

    public int getElo() { return elo; }
    public void setElo(int elo) { this.elo = elo; }

    public int getWins() { return wins; }
    public void setWins(int wins) { this.wins = wins; }

    public int getLosses() { return losses; }
    public void setLosses(int losses) { this.losses = losses; }

    public List<Card> getStack() { return stack; }
    public void setStack(List<Card> stack) { this.stack = stack; }

    public Deck getDeck() { return deck; }
    public void setDeck(Deck deck) { this.deck = deck; }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", coins=" + coins +
                ", elo=" + elo +
                ", wins=" + wins +
                ", losses=" + losses +
                ", stack=" + stack +
                ", deck=" + deck +
                '}';
    }
}
