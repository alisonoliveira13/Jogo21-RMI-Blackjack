package rmi.models;

import java.io.Serializable;


public class Card implements Serializable {

    private final String rank; // Ex: "A", "2", "10", "K"
    private final String suit;  // Ex: "Copas", "Ouros"
    private final int value;

    public Card(String face, String naipe, int value) {
        this.rank = face;
        this.suit = naipe;
        this.value = value;
    }

    public String getRank() {
        return rank;
    }

    public String getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }


    @Override
    public String toString() {
        return rank + " de " + suit;
    }
}