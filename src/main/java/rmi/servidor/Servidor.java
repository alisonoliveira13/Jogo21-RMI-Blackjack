package rmi.servidor;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Servidor {

    public static void main(String[] args) {
        try {
            // 1. Cria a instância do serviço remoto (a implementação do jogo)
            ServidorJogoImpl jogoServico = new ServidorJogoImpl();

            // 2. Cria o registro RMI na porta padrão (1099)
            Registry registry = LocateRegistry.createRegistry(1099);

            // 3. Publica (registra) o objeto remoto no registro com um nome público.
            registry.rebind("Jogo21", jogoServico);

            System.out.println("[SERVIDOR] Servidor do Jogo 21 está no ar e aguardando conexões...");

        } catch (Exception e) {
            System.err.println("[SERVIDOR] Exceção no servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
