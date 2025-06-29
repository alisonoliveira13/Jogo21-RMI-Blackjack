package rmi.blackjack;

import java.io.Serializable;
import java.util.Map;

public class Card implements Serializable {
    public enum Suit {Copas, Ouros, Paus, Espadas};
    private final Suit suit;
    private final int rank;
    private boolean isFlipped;
    private Map<Integer, String> cardsMap = Map.ofEntries(
            Map.entry(1, "Ás"),
            Map.entry(2, "2"),
            Map.entry(3, "3"),
            Map.entry(4, "4"),
            Map.entry(5, "5"),
            Map.entry(6, "6"),
            Map.entry(7, "7"),
            Map.entry(8, "8"),
            Map.entry(9, "9"),
            Map.entry(10, "10"),
            Map.entry(11, "J"),
            Map.entry(12, "Q"),
            Map.entry(13, "K")
    );

    public Card(Suit suit, int rank){
        this.suit = suit;
        this.rank = rank;
        this.isFlipped = true;
    }

    public Suit getSuit() {
        return suit;
    }

    public int getRank() {
        return rank;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void setFlipped(boolean flipped) {
        isFlipped = flipped;
    }

    public String toString(){
        if (!isFlipped){
            return "DESCONHECIDO";
        }
        return cardsMap.get(this.rank) + " de " + this.getSuit();
    }
}
