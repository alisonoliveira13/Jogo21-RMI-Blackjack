package rmi.client;

import rmi.interfaces.rmiGame;
import rmi.models.GameState;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        try {
            // 1. Localiza o RMI Registry no host do servidor (aqui, localhost) na porta 1099.
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            // 2. Busca (lookup) pelo serviço remoto no registry usando o nome "Jogo21".
            // O resultado é um "stub", um proxy que representa o objeto remoto.
            rmiGame gameService = (rmiGame) registry.lookup("Jogo21");

            // --- Interface de Usuário no Console ---
            Scanner scanner = new Scanner(System.in);
            String playerName = "";
            GameState currentState = null;

            System.out.println("Bem-vindo ao cliente do Jogo 21!");
            System.out.print("Digite seu nome para se conectar: ");
            playerName = scanner.nextLine();
            String connectionResponse = gameService.connect(playerName);
            System.out.println("[SERVIDOR] " + connectionResponse);


            while (true) {
                System.out.println("\n--- MENU ---");
                System.out.println("1. Iniciar Nova Rodada");
                System.out.println("2. Pedir Carta (Hit)");
                System.out.println("3. Parar (Stand)");
                System.out.println("4. Sair");
                System.out.print("Escolha uma opção: ");
                String option = scanner.nextLine();

                switch (option) {
                    case "1":
                        // Invoca o método remoto no servidor.
                        currentState = gameService.startRound(playerName);
                        printState(currentState);
                        break;
                    case "2":
                        if (currentState == null || currentState.isGameEnded()) {
                            System.out.println("Você precisa iniciar uma rodada primeiro ou a rodada atual já acabou.");
                            continue;
                        }
                        currentState = gameService.hit(playerName);
                        printState(currentState);
                        break;
                    case "3":
                        if (currentState == null || currentState.isGameEnded()) {
                            System.out.println("Você precisa iniciar uma rodada primeiro ou a rodada atual já acabou.");
                            continue;
                        }
                        currentState = gameService.stand(playerName);
                        printState(currentState);
                        break;
                    case "4":
                        System.out.println("Obrigado por jogar! Saindo...");
                        scanner.close();
                        return; // Encerra o programa
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                        break;
                }
            }

        } catch (Exception e) {
            System.err.println("[CLIENTE] Exceção no cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private static void printState(GameState state) {
        if (state == null) {
            System.out.println("Estado do jogo é nulo.");
            return;
        }

        System.out.println("\n---------------------------------");
        System.out.println(">> Mão do Jogador: " + state.getPlayerHand());
        System.out.println(">> Pontuação do Jogador: " + state.getPlayerScore());
        System.out.println("---------------------------------");
        System.out.println(">> Mão Visível do Dealer: " + state.getDealerHand());
        System.out.println("---------------------------------");
        System.out.println("## MENSAGEM: " + state.getMessage() + " ##");
        System.out.println("---------------------------------");
    }
}