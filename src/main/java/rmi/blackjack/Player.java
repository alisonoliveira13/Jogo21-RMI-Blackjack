package rmi.blackjack;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    private final ArrayList<Card> hand = new ArrayList<>();

    public Player(){

    }

    public List<Card> getHand() {
        return hand;
    }

    public void dropCards(Deck deck){
        deck.addCards(hand);
        hand.clear();
    }

    public void drawCard(Deck deck){
        hand.add(deck.draw());
    }

    public void drawCards(Deck deck, int number){
        for (int i = 0; i < number; i++){
            hand.add(deck.draw());
        }
    }

    public int getScore(){
        int score = 0;
        int aceCount = 0;
        for (Card card : this.hand){
            if (card.getRank() >= 10){
                score += 10;
                continue;
            }
            if (card.getRank() == 1) {
                aceCount += 1;
                continue;
            }
            score += card.getRank();
        }

        while (score > 21 && aceCount > 0) {
            score -= 10;
            aceCount--;
        }
        return score;
    }
}
