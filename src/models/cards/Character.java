package models.cards;

import fileio.CardInput;

public abstract class Character extends Card {
    protected int health;

    public Character(CardInput cardInput) {
        super(cardInput);
        this.health = cardInput.getHealth();
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}