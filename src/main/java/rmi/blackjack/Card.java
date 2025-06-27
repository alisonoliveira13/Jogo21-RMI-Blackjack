package rmi.blackjack;

public class Card {
    public enum Suit {HEARTS, DIAMONDS, CLUBS, SPADES};
    private final Suit suit;
    private final int rank;

    public Card(Suit suit, int rank){
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public int getRank() {
        return rank;
    }
}
