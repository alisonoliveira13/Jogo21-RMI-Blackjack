package rmi.server;

import rmi.interfaces.GameServer;
import rmi.interfaces.Session;
import java.rmi.RemoteException;

public class GameServerImpl implements GameServer {

    public GameServerImpl() throws RemoteException {
        super();
    }

    @Override
    public Session createSession(String bettorName, int bettorBalance) throws RemoteException {
        SessionImpl newSession = new SessionImpl();
        newSession.connect(bettorName, bettorBalance);
        return newSession;
    }
}