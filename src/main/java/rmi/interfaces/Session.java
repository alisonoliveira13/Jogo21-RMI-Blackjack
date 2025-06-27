package rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import rmi.models.GameState;



public interface Session extends Remote {

    void connect(String bettorName, int bettorBalance) throws RemoteException;
    GameState startNewRound(String playerName) throws RemoteException;
    GameState hit(String playerName) throws RemoteException;
    GameState stand(String playerName) throws RemoteException;

    void connect(String playerName, int playerBalance);
}
