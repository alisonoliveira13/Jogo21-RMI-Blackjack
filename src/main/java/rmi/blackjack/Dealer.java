package rmi.blackjack;

import java.util.ArrayList;
import java.util.List;

public class Dealer extends Player {

    public Dealer() {
        super();
    }

    public Card getFlippedCard(){
        return this.getHand().getFirst();
    }
}
