package rmi.client;

import rmi.interfaces.Session;

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
            System.out.println("[SERVIDOR] 4. Depositar");
            System.out.println("[SERVIDOR] 5. Sacar");
            System.out.println("[SERVIDOR] 6. Sair");
            int option = scanner.nextInt();
            switch (option){
                case 1 -> this.startRound();
                case 2 -> System.out.println("[SERVIDOR] Seu saldo: R$" + this.session.getBettorBalance());
                case 3 -> System.out.print("[SERVIDOR] Histórico:\n" + this.session.getHistory());

                case 4 -> this.deposit();

                case 5 -> this.withdraw();

                case 6 -> {
                    return;
                }
                default -> System.out.println("[SERVIDOR] Opção inválida.");

            }
        }

    }

    public boolean isCPFValid(String cpf) {
        cpf = cpf.replaceAll("\\D", "");
        if (cpf.length() != 11) {
            return false;
        }

        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 9; i++) {
            int digit = Character.getNumericValue(cpf.charAt(i));
            sum += digit * (10 - i);
        }
        int firstCheckDigit = 11 - (sum % 11);
        if (firstCheckDigit >= 10) {
            firstCheckDigit = 0;
        }

        if (firstCheckDigit != Character.getNumericValue(cpf.charAt(9))) {
            return false;
        }

        sum = 0;
        for (int i = 0; i < 10; i++) {
            int digit = Character.getNumericValue(cpf.charAt(i));
            sum += digit * (11 - i);
        }
        int secondCheckDigit = 11 - (sum % 11);
        if (secondCheckDigit >= 10) {
            secondCheckDigit = 0;
        }

        return secondCheckDigit == Character.getNumericValue(cpf.charAt(10));
    }

    public void deposit() throws RemoteException {
        System.out.println("[SERVIDOR] Digite o valor a ser depositado: ");
        int value = scanner.nextInt();
        if (value < 0){
            System.out.println("[SERVIDOR] Valor inválido! O mínimo é R$1,00. Cancelando operação...");
            return;
        }
        this.session.deposit(value);
        System.out.println("[SERVIDOR] R$" + value + " depositados com sucesso");
    }

    public void withdraw() throws RemoteException {
        System.out.println("[SERVIDOR] Digite o valor a ser sacado: ");
        int value = scanner.nextInt();
        if (value < 0){
            System.out.println("[SERVIDOR] Valor inválido! O mínimo é R$1,00. Cancelando operação...");
            return;
        }
        System.out.println("[SERVIDOR] Digite sua chave pix (CPF), apenas números: ");
        String cpf = scanner.nextLine();
        if (!isCPFValid(cpf)){
            System.out.println("[SERVIDOR] CPF inválido! Cancelando operação...");
        }
        this.session.withdraw(value);
        System.out.println("[SERVIDOR] R$" + value + " sacados com sucesso");
    }

    public void startRound() throws RemoteException {
        System.out.println("[SERVIDOR] Quantos R$ deseja apostar nessa rodada? ");
        int betAmount = scanner.nextInt();
        if (betAmount < 1) {
            System.out.println("[SERVIDOR] Valor de aposta inválido. O mínimo é R$1,00.");
            return;
        }
        if (betAmount > this.session.getBettorBalance()) {
            System.out.println("[SERVIDOR] Você não possui saldo suficiente para realizar essa aposta. Seu saldo é de: " + this.session.getBettorBalance());
            return;
        }
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