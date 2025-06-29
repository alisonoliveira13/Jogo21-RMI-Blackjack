package rmi.blackjack;

import java.io.Serializable;
import java.util.ArrayList;

public class History implements Serializable {
    private final ArrayList<Round> roundHistory = new ArrayList<>();

    public History(){

    }

    public void add(Round round){
        roundHistory.addFirst(round);
    }

    public ArrayList<Round> getRoundHistory() {
        return roundHistory;
    }

    public String getHistoryString(){
        StringBuilder history = new StringBuilder();
        for (Round round : this.roundHistory){
            history.append(round).append("\n");
        }
        return history.toString();
    }
}
