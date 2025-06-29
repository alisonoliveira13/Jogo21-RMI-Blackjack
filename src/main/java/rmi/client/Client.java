package rmi.client;

import rmi.blackjack.Round;
import rmi.interfaces.Session;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {

    private String username;
    private Integer balance;
    private final Scanner scanner = new Scanner(System.in);
    private Session session;

    public Client() throws NotBoundException, RemoteException {
        this.welcomeMessage();
        this.startClient();
        this.connectServer();
    }
    public void welcomeMessage(){
        System.out.println("Bem-vindo ao cliente do Jogo 21!");
        System.out.print("Digite seu nome para se conectar: ");
        this.username = scanner.nextLine();
        System.out.print("Digite quantos reais deseja depositar no jogo: ");
        this.balance  = scanner.nextInt();
    }

    public void startClient() throws NotBoundException, RemoteException {
        // 1. Localiza o RMI Registry no host do servidor (aqui, localhost) na porta 1099.
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);

        // 2. Busca (lookup) pelo serviço remoto no registry usando o nome "Jogo21".
        // O resultado é um "stub", um proxy que representa o objeto remoto.
        this.session = (Session) registry.lookup("Jogo21");
    }

    public void connectServer() throws RemoteException {
        String connectionResponse = this.session.connect(username, balance);
        System.out.println("[SERVIDOR] " + connectionResponse);
    }

    public void getUserMenuOption() throws RemoteException {
        while (true){
            System.out.println("[SERVIDOR] Selecione uma opção: ");
            System.out.println("[SERVIDOR] 1. Iniciar nova rodada");
            System.out.println("[SERVIDOR] 2. Ver saldo");
            System.out.println("[SERVIDOR] 3. Ver histórico");
            System.out.println("[SERVIDOR] 4. Sair");
            int option = scanner.nextInt();
            switch (option){
                case 1 -> {
                    this.startRound();
                }
                case 2 -> {
                    System.out.println("[SERVIDOR] Seu saldo: " + this.session.getBettorBalance());
                }
                case 3 -> {
                    System.out.print("[SERVIDOR] Histórico:\n" + this.session.getHistory());
                }
                case 4 -> {
                    return;
                }
                default -> {
                    System.out.println("[SERVIDOR] Opção inválida.");
                }
            }
        }

    }

    public void startRound() throws RemoteException {
        System.out.println("[SERVIDOR] Quantos R$ deseja apostar nessa rodada? ");
        int betAmount = scanner.nextInt();
        this.session.startNewRound(betAmount);
        this.getBettorAction();
    }

    public void getBettorAction() throws RemoteException {
        while (this.session.getCurrentRound() != null){
            System.out.print(this.session.getCurrentRoundGameState());
            System.out.print(this.session.getCurrentRoundPossibleActions());
            int nextAction = scanner.nextInt();
            boolean isValid = this.session.isValidAction(nextAction);
            if (!isValid){
                System.out.println("Ação inválida!");
            } else {
                this.session.executeAction(nextAction);
            }
        }
        System.out.println(this.session.getResultLastRound());
    }

    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.getUserMenuOption();

        } catch (Exception e) {
            System.err.println("[CLIENTE] Exceção no cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
}