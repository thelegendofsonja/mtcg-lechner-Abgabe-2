package game.model;

import java.util.List;

public class Deck {
    private List<Card> cards;  // The 4 cards selected for battle

    public Deck(List<Card> cards) {
        if (cards.size() != 4) {
            throw new IllegalArgumentException("A deck must have exactly 4 cards.");
        }
        this.cards = cards;
    }

    // Getters and Setters
    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        if (cards.size() != 4) {
            throw new IllegalArgumentException("A deck must have exactly 4 cards.");
        }
        this.cards = cards;
    }

    @Override
    public String toString() {
        return "Deck{" +
                "cards=" + cards +
                '}';
    }
}
