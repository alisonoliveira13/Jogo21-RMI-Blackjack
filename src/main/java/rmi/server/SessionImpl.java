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
        this.bettor.setBalance(this.bettor.getBalance() - betAmount);
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
            if (this.currentRound.getResult()) {
                this.bettor.setBalance(this.bettor.getBalance() + this.currentRound.getBetAmount() * 2);
            }
            this.currentRound = null;
        }
    }

    public String getResultLastRound(){
        return this.history.getRoundHistory().getFirst().getResultString();
    }

    public int getBettorBalance() {
        return bettor.getBalance();
    }

    public String getHistory(){
        return this.history.getHistoryString();
    }

    public void deposit(int value) {
        this.bettor.setBalance(this.bettor.getBalance() + value);
    }

    public void withdraw(int value){
        this.bettor.setBalance(this.bettor.getBalance() - value);
    }
}
