package rmi.blackjack;

public enum Action {
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
