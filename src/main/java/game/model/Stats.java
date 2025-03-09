package game.model;

public class Stats {
    private String username;
    private int gamesPlayed;
    private int gamesWon;
    private int gamesLost;
    private int elo;

    public Stats() {
        // default constructor for Jackson
    }

    public Stats(String username, int gamesPlayed, int gamesWon, int gamesLost, int elo) {
        this.username = username;
        this.gamesPlayed = gamesPlayed;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;
        this.elo = elo;
    }

    public String getUsername() {
        return username;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public int getElo() {
        return elo;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public void setGamesLost(int gamesLost) {
        this.gamesLost = gamesLost;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }
}
