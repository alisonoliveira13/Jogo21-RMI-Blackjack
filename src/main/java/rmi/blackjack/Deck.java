package rmi.blackjack;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Deck implements Serializable {
    private final Stack<Card> cards = new Stack<>();

    public Deck() {
        for (Card.Suit suit : Card.Suit.values()) {
            for (int v = 1; v <= 13; v++) {
                cards.push(new Card(suit, v));
            }
        }
        Collections.shuffle(cards);
    }

    public Card draw() {
        return cards.pop();
    }

    public void addCards(ArrayList<Card> cards){
        this.cards.addAll(cards);
    }

    public void shuffle(){
        Collections.shuffle(cards);
    }

    public int getDeckSize(){
        return this.cards.size();
    }
}