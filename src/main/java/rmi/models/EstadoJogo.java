package rmi.models;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


public class EstadoJogo implements Serializable {

    @Serial
    private static final long serialVersionUID = 2L; // Mudei o ID para 2L

    private final List<Carta> maoJogador;
    private final int pontuacaoJogador;
    private final List<Carta> maoVisivelDealer;
    private final String mensagem;
    private final boolean jogoEncerrado;

    public EstadoJogo(List<Carta> maoJogador, int pontuacaoJogador, List<Carta> maoVisivelDealer, String mensagem, boolean jogoEncerrado) {
        this.maoJogador = maoJogador;
        this.pontuacaoJogador = pontuacaoJogador;
        this.maoVisivelDealer = maoVisivelDealer;
        this.mensagem = mensagem;
        this.jogoEncerrado = jogoEncerrado;
    }

    public List<Carta> getMaoJogador() {
        return maoJogador;
    }

    public int getPontuacaoJogador() {
        return pontuacaoJogador;
    }

    public List<Carta> getMaoVisivelDealer() {
        return maoVisivelDealer;
    }

    public String getMensagem() {
        return mensagem;
    }

    public boolean isJogoEncerrado() {
        return jogoEncerrado;
    }

    @Override
    public String toString() {
        return "EstadoJogo {\n" +
                "  Mão do Jogador = " + maoJogador + ", Pontuação = " + pontuacaoJogador + ",\n" +
                "  Mão do Dealer = " + maoVisivelDealer + ",\n" +
                "  Mensagem = '" + mensagem + "',\n" +
                "  Jogo Encerrado = " + jogoEncerrado + "\n" +
                '}';
    }
}
