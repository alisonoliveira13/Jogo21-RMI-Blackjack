package rmi.blackjack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Dealer extends Player implements Serializable {

    public Dealer() {
        super();
    }

    public Card getFlippedCard(){
        for (Card card : this.getHand()){
            if (card.isFlipped()){
                return card;
            }
        }
        return null;
    }

    public void play(Deck deck){
        this.getHand().getLast().setFlipped(true);
        while (this.getScore() < 17) {
            this.drawCard(deck);
            this.getHand().getLast().setFlipped(true);
        }
    }
}
