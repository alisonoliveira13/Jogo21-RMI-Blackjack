package rmi.models;

import java.io.Serializable;


public class Carta implements Serializable {

    /**
     * Controle de versão para a serialização. Ajuda a garantir que o cliente
     * e o servidor estão usando a mesma versão da classe.
     */
    private static final long serialVersionUID = 1L;

    private final String face; // Ex: "A", "2", "10", "K"
    private final String naipe;  // Ex: "Copas", "Ouros"
    private final int valor;     // O valor da carta no jogo 21 (ex: K vale 10)

    public Carta(String face, String naipe, int valor) {
        this.face = face;
        this.naipe = naipe;
        this.valor = valor;
    }

    public String getFace() {
        return face;
    }

    public String getNaipe() {
        return naipe;
    }

    public int getValor() {
        return valor;
    }


    @Override
    public String toString() {
        return face + " de " + naipe;
    }
}