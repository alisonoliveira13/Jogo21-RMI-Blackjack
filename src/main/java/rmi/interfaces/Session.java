package rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import rmi.blackjack.Bettor;
import rmi.blackjack.Round;




public interface Session extends Remote {

    String connect(String bettorName, int bettorBalance) throws RemoteException;
    void startNewRound(int betAmount) throws RemoteException;
    Round getCurrentRound() throws RemoteException;
    String getCurrentRoundGameState() throws RemoteException;
    String getCurrentRoundPossibleActions() throws RemoteException;
    boolean isValidAction(int actionChoice) throws RemoteException;
    void executeAction(int actionChoice) throws RemoteException;
    String getResultLastRound() throws RemoteException;
    String getHistory() throws RemoteException;
    int getBettorBalance() throws RemoteException;
}
