package rmi.models;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


public class GameState implements Serializable {

    private final List<Card> playerHand;
    private final int playerScore;
    private final List<Card> dealerHand;
    private final String message;
    private final boolean gameEnded;

    public GameState(List<Card> playerHand, int playerScore, List<Card> dealerHand, String message, boolean gameEnded) {
        this.playerHand = playerHand;
        this.playerScore = playerScore;
        this.dealerHand = dealerHand;
        this.message = message;
        this.gameEnded = gameEnded;
    }

    public List<Card> getPlayerHand() {
        return playerHand;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public List<Card> getDealerHand() {
        return dealerHand;
    }

    public String getMessage() {
        return message;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    @Override
    public String toString() {
        return "EstadoJogo {\n" +
                "  Mão do Jogador = " + playerHand + ", Pontuação = " + playerScore + ",\n" +
                "  Mão do Dealer = " + dealerHand + ",\n" +
                "  Mensagem = '" + message + "',\n" +
                "  Jogo Encerrado = " + gameEnded + "\n" +
                '}';
    }
}
