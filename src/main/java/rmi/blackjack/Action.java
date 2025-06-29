package rmi.blackjack;

import java.io.Serializable;

public enum Action implements Serializable {
    HIT("Pedir Carta (Hit)"),
    STAND("Parar (Stand)"),
    DOUBLE_DOWN("Dobrar aposta (Double down)"),
    SPLIT("Separar pares (Split)"),
    INSURANCE("Apostar dealer tem blackjack (Insurance)");

    private String description;

    Action(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
