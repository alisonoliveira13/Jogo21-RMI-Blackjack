package rmi.server;

import rmi.interfaces.GameServer;
import rmi.interfaces.Session;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {

    public static void main(String[] args) {
        try {
            // 1. Cria a instância do serviço remoto (a implementação do jogo)
            GameServerImpl gameServer = new GameServerImpl();

            GameServer stub = (GameServer) UnicastRemoteObject.exportObject(gameServer, 0);

            // 2. Cria o registro RMI na porta padrão (1099)
            Registry registry = LocateRegistry.createRegistry(1099);

            // 3. Publica (registra) o objeto remoto no registro com um nome público.
            registry.rebind("Jogo21", stub);

            System.out.println("[SERVIDOR] Servidor do Jogo 21 está no ar e aguardando conexões...");

        } catch (Exception e) {
            System.err.println("[SERVIDOR] Exceção no servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}