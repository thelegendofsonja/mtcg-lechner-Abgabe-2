package game.model;

import java.util.List;

public class Package {
    private List<Card> cards;

    public Package(List<Card> cards) {
        this.cards = cards;
    }

    // Getters and Setters
    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        return "Package{" +
                "cards=" + cards +
                '}';
    }
}
