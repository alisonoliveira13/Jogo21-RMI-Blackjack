package rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import rmi.models.GameState;



public interface rmiGame extends Remote {

    String connect(String playerName) throws RemoteException;
    GameState startRound(String playerName) throws RemoteException;
    GameState hit(String playerName) throws RemoteException;
    GameState stand(String playerName) throws RemoteException;
}
