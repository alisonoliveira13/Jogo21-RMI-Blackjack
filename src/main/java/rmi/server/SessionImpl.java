package rmi.server;

import rmi.blackjack.*;
import rmi.interfaces.Session;
import rmi.models.GameState;

import java.rmi.RemoteException;

public class SessionImpl implements Session {
    private Dealer dealer;
    private Bettor bettor;
    private Deck deck;
    private History history;

    @Override
    public void connect(String bettorName, int bettorBalance){
        this.dealer = new Dealer();
        this.bettor = new Bettor(bettorName, bettorBalance);
        this.deck = new Deck();
        this.history = new History();
    }

    public void startNewRound(int betAmount){
        Round round = new Round(dealer, bettor, deck);
        this.history.add(round);
        round.start();
    }

    @Override
    public GameState startNewRound(String playerName) throws RemoteException {
        return null;
    }

    @Override
    public GameState hit(String playerName) throws RemoteException {
        return null;
    }

    @Override
    public GameState stand(String playerName) throws RemoteException {
        return null;
    }

}
