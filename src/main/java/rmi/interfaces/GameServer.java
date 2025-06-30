package rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameServer extends Remote {

    Session createSession(String bettorName, int bettorBalance) throws RemoteException;
}