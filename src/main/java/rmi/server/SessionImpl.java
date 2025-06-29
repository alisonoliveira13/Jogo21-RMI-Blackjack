package rmi.server;

import rmi.blackjack.*;
import rmi.interfaces.Session;


public class SessionImpl implements Session {
    private Dealer dealer;
    private Bettor bettor;
    private Deck deck;
    private History history;
    private Round currentRound;

    @Override
    public String connect(String bettorName, int bettorBalance){
        this.dealer = new Dealer();
        this.bettor = new Bettor(bettorName, bettorBalance);
        this.deck = new Deck();
        this.history = new History();
        return "Conectado com sucesso!";
    }

    @Override
    public void startNewRound(int betAmount){
        Round round = new Round(dealer, bettor, deck, betAmount);
        this.history.add(round);
        this.currentRound = round;
        round.start();
    }

    public Round getCurrentRound() {
        return currentRound;
    }

    public String getCurrentRoundGameState(){
        return this.currentRound.getGameStateString();
    }

    public String getCurrentRoundPossibleActions(){
        return this.currentRound.getAvailableActionsString();
    }

    public boolean isValidAction(int actionChoice){
        return this.currentRound.isValidAction(actionChoice);
    }

    public void executeAction(int actionChoice){
        this.currentRound.executeAction(actionChoice - 1);
        if (this.currentRound.getResult() != null){
            if (this.currentRound.getResult() == true) {
                this.bettor.setBalance(this.bettor.getBalance() + this.currentRound.getBetAmount());
            } else if (this.currentRound.getResult() == false){
                this.bettor.setBalance(this.bettor.getBalance() - this.currentRound.getBetAmount());
            }
            this.currentRound = null;
        }
    }

    public String getResultLastRound(){
        return this.history.getRoundHistory().getLast().getResultString();
    }

    public int getBettorBalance() {
        return bettor.getBalance();
    }

    public String getHistory(){
        return this.history.getHistoryString();
    }
}
