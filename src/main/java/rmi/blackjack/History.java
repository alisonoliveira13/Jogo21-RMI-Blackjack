package rmi.blackjack;

import java.util.ArrayList;

public class History {
    private final ArrayList<Round> roundHistory = new ArrayList<>();

    public History(){

    }

    public void add(Round round){
        roundHistory.addFirst(round);
    }

    public ArrayList<Round> getRoundHistory() {
        return roundHistory;
    }
}
