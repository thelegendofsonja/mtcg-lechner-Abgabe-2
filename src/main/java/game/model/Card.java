package game.model;

import java.util.UUID;

public class Card {
    private UUID id;
    private String name;
    private double damage;
    private String elementType; // fire, water, normal
    private boolean isMonster;  // true if it's a monster card, false if it's a spell card

    public Card(UUID id, String name, double damage, String elementType, boolean isMonster) {
        this.id = id;
        this.name = name;
        this.damage = damage;
        this.elementType = elementType;
        this.isMonster = isMonster;
    }


    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public String getElementType() {
        return elementType;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

    public boolean isMonster() {
        return isMonster;
    }

    public void setMonster(boolean isMonster) {
        this.isMonster = isMonster;
    }

    @Override
    public String toString() {
        return "Card{" +
                "name='" + name + '\'' +
                ", damage=" + damage +
                ", elementType='" + elementType + '\'' +
                ", isMonster=" + isMonster +
                '}';
    }
}
