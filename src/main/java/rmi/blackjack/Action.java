package rmi.blackjack;

import java.io.Serializable;

public enum Action implements Serializable {
    HIT("Pedir Carta (Hit)"),
    STAND("Parar (Stand)"),
    DOUBLE_DOWN("Dobrar aposta (Double down) | Obrigatoriamente receberá mais uma carta e apenas esta.");

    private String description;

    Action(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
