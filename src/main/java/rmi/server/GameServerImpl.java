package rmi.server;

import rmi.interfaces.rmiGame;
import rmi.models.Card;
import rmi.models.GameState;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementação concreta da interface remota JogoRMI.
 * Esta classe contém toda a lógica de negócio do jogo de 21.
 * Estende UnicastRemoteObject para se tornar um objeto de serviço RMI.
 */

public class GameServerImpl extends UnicastRemoteObject implements rmiGame {

    private final Map<String, GameState> activeGames = new ConcurrentHashMap<>();
    private List<Card> deck;


    public GameServerImpl() throws RemoteException {
        super();
        System.out.println("[SERVIDOR] Objeto de serviço do jogo criado.");
    }

    @Override
    public String connect(String playerName) throws RemoteException {
        // Esta linha executou com sucesso
        System.out.println("[SERVIDOR] " + playerName + " se conectou.");
        return "Bem-vindo ao Jogo 21, " + playerName + "! Clique em 'Iniciar Rodada' para começar.";
    }

    @Override
    public GameState startRound(String playerName) throws RemoteException {
        System.out.println("[SERVIDOR] Iniciando nova rodada para " + playerName);
        this.deck = createAndShuffleDeck();

        // Distribui cartas
        List<Card> playerHand = new ArrayList<>();
        playerHand.add(deck.remove(0));
        playerHand.add(deck.remove(0));

        List<Card> dealerHand = new ArrayList<>();
        dealerHand.add(deck.remove(0));
        dealerHand.add(deck.remove(0));

        // Cria o estado inicial do jogo
        int gameScore = calculateScore(playerHand);
        List<Card> dealerVisibleHand = List.of(dealerHand.get(0)); // Apenas a primeira carta é visível

        GameState newState = new GameState(
                playerHand,
                gameScore,
                dealerVisibleHand,
                "Sua vez de jogar. Peça uma carta ou pare.",
                false
        );

        // Armazena o estado completo (incluindo a mão oculta do dealer) no servidor

        activeGames.put(playerName, newState);

        return newState;
    }

    @Override
    public GameState hit(String playerName) throws RemoteException {
        GameState currentState = activeGames.get(playerName);
        if (currentState == null || currentState.isGameEnded()) {
            return currentState; // Não faz nada se o jogo acabou
        }

        // Pega uma nova carta e adiciona à mão do jogador
        List<Card> newHand = new ArrayList<>(currentState.getPlayerHand());
        newHand.add(deck.remove(0));

        int newScore = calculateScore(newHand);
        boolean gameEnded = false;
        String message = "Você pediu uma carta. Continue ou pare.";

        if (newScore > 21) {
            gameEnded = true;
            message = "Você estourou com " + newScore + " pontos! O dealer venceu.";
        }

        GameState nextState = new GameState(
                newHand,
                newScore,
                currentState.getDealerHand(),
                message,
                gameEnded
        );
        activeGames.put(playerName, nextState);
        return nextState;
    }

    @Override
    public GameState stand(String playerName) throws RemoteException {
        GameState currentState = activeGames.get(playerName);
        if (currentState == null || currentState.isGameEnded()) {
            return currentState;
        }

        // --- Início da lógica do Dealer ---

        List<Card> completeDealerHand = new ArrayList<>(currentState.getDealerHand());
        completeDealerHand.add(deck.remove(0)); // A carta que estava virada para baixo.

        int dealerScore = calculateScore(completeDealerHand);

        // Dealer deve pedir cartas até atingir 17 ou mais.
        while (dealerScore < 17) {
            completeDealerHand.add(deck.remove(0));
            dealerScore = calculateScore(completeDealerHand);
        }

        // --- Fim da lógica do Dealer ---

        // --- Determinação do Vencedor ---
        String finalMessage;
        int playerScore = currentState.getPlayerScore();

        if (dealerScore > 21) {
            finalMessage = "O dealer estourou com " + dealerScore + " pontos! Você venceu!";
        } else if (playerScore > dealerScore) {
            finalMessage = "Você venceu com " + playerScore + " contra " + dealerScore + " do dealer!";
        } else { // Dealer vence em caso de empate ou pontuação maior
            finalMessage = "O dealer venceu com " + dealerScore + " contra " + playerScore + ".";
        }

        GameState finalState = new GameState(
                currentState.getPlayerHand(),
                playerScore,
                completeDealerHand, // Agora enviamos a mão completa do dealer
                finalMessage,
                true // O jogo está encerrado
        );
        activeGames.put(playerName, finalState);
        return finalState;
    }



    private int calculateScore(List<Card> hand) {
        int score = 0;
        int aceCount = 0;
        for (Card card : hand) {
            score += card.getValue();
            if (card.getRank().equals("A")) {
                aceCount++;
            }
        }
        // Lógica do Ás: se a pontuação estourar, o Ás vale 1 em vez de 11.
        while (score > 21 && aceCount > 0) {
            score -= 10;
            aceCount--;
        }
        return score;
    }

    private List<Card> createAndShuffleDeck() {
        List<Card> newDeck = new ArrayList<>();
        String[] suits = {"Copas", "Ouros", "Paus", "Espadas"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        int[] values = {2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11}; // Ás inicialmente vale 11

        for (String suit : suits) {
            for (int i = 0; i < ranks.length; i++) {
                newDeck.add(new Card(ranks[i], suit, values[i]));
            }
        }
        Collections.shuffle(newDeck);
        return newDeck;
    }
}