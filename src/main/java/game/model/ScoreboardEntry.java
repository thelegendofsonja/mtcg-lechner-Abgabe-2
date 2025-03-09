package game.model;

public class ScoreboardEntry {
    private String username;
    private int elo;

    public ScoreboardEntry(String username, int elo) {
        this.username = username;
        this.elo = elo;
    }

    public String getUsername() {
        return username;
    }

    public int getElo() {
        return elo;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }
}
