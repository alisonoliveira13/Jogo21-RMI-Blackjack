package rmi.servidor;

import rmi.interfaces.JogoRMI;
import rmi.models.Carta;
import rmi.models.EstadoJogo;

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

public class ServidorJogoImpl extends UnicastRemoteObject implements JogoRMI {

    private final Map<String, EstadoJogo> jogosAtivos = new ConcurrentHashMap<>();
    private List<Carta> baralho;


    public ServidorJogoImpl() throws RemoteException {
        super();
        System.out.println("[SERVIDOR] Objeto de serviço do jogo criado.");
    }

    @Override
    public String conectar(String nomeJogador) throws RemoteException {
        // Esta linha executou com sucesso
        System.out.println("[SERVIDOR] " + nomeJogador + " se conectou.");
        return "Bem-vindo ao Jogo 21, " + nomeJogador + "! Clique em 'Iniciar Rodada' para começar.";
    }

    @Override
    public EstadoJogo iniciarRodada(String nomeJogador) throws RemoteException {
        System.out.println("[SERVIDOR] Iniciando nova rodada para " + nomeJogador);
        this.baralho = criarEEmbaralharBaralho();

        // Distribui cartas
        List<Carta> maoJogador = new ArrayList<>();
        maoJogador.add(baralho.remove(0));
        maoJogador.add(baralho.remove(0));

        List<Carta> maoDealer = new ArrayList<>();
        maoDealer.add(baralho.remove(0));
        maoDealer.add(baralho.remove(0));

        // Cria o estado inicial do jogo
        int pontuacaoJogador = calcularPontuacao(maoJogador);
        List<Carta> maoVisivelDealer = List.of(maoDealer.get(0)); // Apenas a primeira carta é visível

        EstadoJogo novoEstado = new EstadoJogo(
                maoJogador,
                pontuacaoJogador,
                maoVisivelDealer,
                "Sua vez de jogar. Peça uma carta ou pare.",
                false
        );

        // Armazena o estado completo (incluindo a mão oculta do dealer) no servidor

        jogosAtivos.put(nomeJogador, novoEstado);

        return novoEstado;
    }

    @Override
    public EstadoJogo pedirCarta(String nomeJogador) throws RemoteException {
        EstadoJogo estadoAtual = jogosAtivos.get(nomeJogador);
        if (estadoAtual == null || estadoAtual.isJogoEncerrado()) {
            return estadoAtual; // Não faz nada se o jogo acabou
        }

        // Pega uma nova carta e adiciona à mão do jogador
        List<Carta> novaMao = new ArrayList<>(estadoAtual.getMaoJogador());
        novaMao.add(baralho.remove(0));

        int novaPontuacao = calcularPontuacao(novaMao);
        boolean jogoEncerrado = false;
        String mensagem = "Você pediu uma carta. Continue ou pare.";

        if (novaPontuacao > 21) {
            jogoEncerrado = true;
            mensagem = "Você estourou com " + novaPontuacao + " pontos! O dealer venceu.";
        }

        EstadoJogo proximoEstado = new EstadoJogo(
                novaMao,
                novaPontuacao,
                estadoAtual.getMaoVisivelDealer(),
                mensagem,
                jogoEncerrado
        );
        jogosAtivos.put(nomeJogador, proximoEstado);
        return proximoEstado;
    }

    @Override
    public EstadoJogo parar(String nomeJogador) throws RemoteException {
        EstadoJogo estadoAtual = jogosAtivos.get(nomeJogador);
        if (estadoAtual == null || estadoAtual.isJogoEncerrado()) {
            return estadoAtual;
        }

        // --- Início da lógica do Dealer ---

        List<Carta> maoCompletaDealer = new ArrayList<>(estadoAtual.getMaoVisivelDealer());
        maoCompletaDealer.add(baralho.remove(0)); // A carta que estava virada para baixo.

        int pontuacaoDealer = calcularPontuacao(maoCompletaDealer);

        // Dealer deve pedir cartas até atingir 17 ou mais.
        while (pontuacaoDealer < 17) {
            maoCompletaDealer.add(baralho.remove(0));
            pontuacaoDealer = calcularPontuacao(maoCompletaDealer);
        }

        // --- Fim da lógica do Dealer ---

        // --- Determinação do Vencedor ---
        String mensagemFinal;
        int pontuacaoJogador = estadoAtual.getPontuacaoJogador();

        if (pontuacaoDealer > 21) {
            mensagemFinal = "O dealer estourou com " + pontuacaoDealer + " pontos! Você venceu!";
        } else if (pontuacaoJogador > pontuacaoDealer) {
            mensagemFinal = "Você venceu com " + pontuacaoJogador + " contra " + pontuacaoDealer + " do dealer!";
        } else { // Dealer vence em caso de empate ou pontuação maior
            mensagemFinal = "O dealer venceu com " + pontuacaoDealer + " contra " + pontuacaoJogador + ".";
        }

        EstadoJogo estadoFinal = new EstadoJogo(
                estadoAtual.getMaoJogador(),
                pontuacaoJogador,
                maoCompletaDealer, // Agora enviamos a mão completa do dealer
                mensagemFinal,
                true // O jogo está encerrado
        );
        jogosAtivos.put(nomeJogador, estadoFinal);
        return estadoFinal;
    }



    private int calcularPontuacao(List<Carta> mao) {
        int pontuacao = 0;
        int numAzes = 0;
        for (Carta carta : mao) {
            pontuacao += carta.getValor();
            if (carta.getFace().equals("A")) {
                numAzes++;
            }
        }
        // Lógica do Ás: se a pontuação estourar, o Ás vale 1 em vez de 11.
        while (pontuacao > 21 && numAzes > 0) {
            pontuacao -= 10;
            numAzes--;
        }
        return pontuacao;
    }

    private List<Carta> criarEEmbaralharBaralho() {
        List<Carta> novoBaralho = new ArrayList<>();
        String[] naipes = {"Copas", "Ouros", "Paus", "Espadas"};
        String[] faces = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        int[] valores = {2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11}; // Ás inicialmente vale 11

        for (String naipe : naipes) {
            for (int i = 0; i < faces.length; i++) {
                novoBaralho.add(new Carta(faces[i], naipe, valores[i]));
            }
        }
        Collections.shuffle(novoBaralho);
        return novoBaralho;
    }
}