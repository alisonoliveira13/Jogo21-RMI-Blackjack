package rmi.cliente;

import rmi.interfaces.JogoRMI;
import rmi.models.Carta;
import rmi.models.EstadoJogo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) {
        try {
            // 1. Localiza o RMI Registry no host do servidor (aqui, localhost) na porta 1099.
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            // 2. Busca (lookup) pelo serviço remoto no registry usando o nome "Jogo21".
            // O resultado é um "stub", um proxy que representa o objeto remoto.
            JogoRMI jogoServico = (JogoRMI) registry.lookup("Jogo21");

            // --- Interface de Usuário no Console ---
            Scanner scanner = new Scanner(System.in);
            String nomeJogador = "";
            EstadoJogo estadoAtual = null;

            System.out.println("Bem-vindo ao cliente do Jogo 21!");
            System.out.print("Digite seu nome para se conectar: ");
            nomeJogador = scanner.nextLine();
            String respostaConexao = jogoServico.conectar(nomeJogador);
            System.out.println("[SERVIDOR] " + respostaConexao);


            while (true) {
                System.out.println("\n--- MENU ---");
                System.out.println("1. Iniciar Nova Rodada");
                System.out.println("2. Pedir Carta (Hit)");
                System.out.println("3. Parar (Stand)");
                System.out.println("4. Sair");
                System.out.print("Escolha uma opção: ");
                String opcao = scanner.nextLine();

                switch (opcao) {
                    case "1":
                        // Invoca o método remoto no servidor.
                        estadoAtual = jogoServico.iniciarRodada(nomeJogador);
                        imprimirEstado(estadoAtual);
                        break;
                    case "2":
                        if (estadoAtual == null || estadoAtual.isJogoEncerrado()) {
                            System.out.println("Você precisa iniciar uma rodada primeiro ou a rodada atual já acabou.");
                            continue;
                        }
                        estadoAtual = jogoServico.pedirCarta(nomeJogador);
                        imprimirEstado(estadoAtual);
                        break;
                    case "3":
                        if (estadoAtual == null || estadoAtual.isJogoEncerrado()) {
                            System.out.println("Você precisa iniciar uma rodada primeiro ou a rodada atual já acabou.");
                            continue;
                        }
                        estadoAtual = jogoServico.parar(nomeJogador);
                        imprimirEstado(estadoAtual);
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


    private static void imprimirEstado(EstadoJogo estado) {
        if (estado == null) {
            System.out.println("Estado do jogo é nulo.");
            return;
        }

        System.out.println("\n---------------------------------");
        System.out.println(">> Mão do Jogador: " + estado.getMaoJogador());
        System.out.println(">> Pontuação do Jogador: " + estado.getPontuacaoJogador());
        System.out.println("---------------------------------");
        System.out.println(">> Mão Visível do Dealer: " + estado.getMaoVisivelDealer());
        System.out.println("---------------------------------");
        System.out.println("## MENSAGEM: " + estado.getMensagem() + " ##");
        System.out.println("---------------------------------");
    }
}