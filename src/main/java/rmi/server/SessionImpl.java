package rmi.server;

import rmi.blackjack.*;
import rmi.interfaces.Session;

import java.io.Serializable;
import java.rmi.RemoteException;


public class SessionImpl implements Session, Serializable {
    private Dealer dealer;
    private Bettor bettor;
    private Deck deck;
    private History history;
    private Round currentRound;

    public SessionImpl() throws RemoteException {
        super();
    }

    @Override
    public void connect(String bettorName, int bettorBalance){
        this.dealer = new Dealer();
        this.bettor = new Bettor(bettorName, bettorBalance);
        this.deck = new Deck();
        this.history = new History();
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

    public boolean withdraw(int value){
        if (value <= 0 || value > this.bettor.getBalance()) {
            return false;
        }
        this.bettor.setBalance(this.bettor.getBalance() - value);
        return true;
    }
}
