package rmi.blackjack;

import java.io.Serializable;
import java.util.ArrayList;

public class Round implements Serializable {
    private final Dealer dealer;
    private final Bettor bettor;
    private final Deck deck;
    private final ArrayList<Action> actionList = new ArrayList<>();
    private int betAmount;
    private Boolean result = null;

    public Round(Dealer dealer, Bettor bettor, Deck deck, int betAmount){
        this.dealer = dealer;
        this.bettor = bettor;
        this.deck = deck;
        this.betAmount = betAmount;
    }

    public void start(){
        this.bettor.dropCards(this.deck);
        this.dealer.dropCards(this.deck);
        this.deck.shuffle();

        this.bettor.drawCards(this.deck, 2);
        this.dealer.drawCards(this.deck, 2);
        this.dealer.getHand().getLast().setFlipped(false);
    }

    //TODO melhorar lógica apostas
    //TODO implementar interface gráfica
    //TODO implementar double down, stand, insurance
    public Boolean getResult() {
        return result;
    }

    public boolean isValidAction(int action){
        return 1 <= action && action <= actionList.size();
    }

    public void executeAction(int actionPostion){
        Action action = actionList.get(actionPostion);
        switch (action){
            case HIT -> this.hit();
            case STAND -> this.stand();
            case DOUBLE_DOWN -> this.doubleDown();
            case SPLIT -> this.split();
            case INSURANCE -> this.insurance();
        }
    }

    public String getAvailableActionsString() {
        this.getAvailableActions();
        StringBuilder availableActions = new StringBuilder();
        availableActions.append("\nEscolha a próxima ação: \n");

        int index = 1;
        for (Action action : actionList) {
            availableActions.append(index).append(". ").append(action.getDescription()).append("\n");
            index++;
        }
        return availableActions.toString();
    }

    public int getBetAmount(){
        return this.betAmount;
    }


    /* Implementação de funcionalidades extras não especificadas no trabalho.
    * No jogo de blackjack, além de Hit e Stand, também há Double Down, Split e Insurance.
    * Foi realizado as implementações a seguir.*/
    public void getAvailableActions(){
        actionList.clear();

        for (Action action : Action.values()){
            switch (action){
                case HIT -> actionList.add(action);
                case STAND -> actionList.add(action);
                case DOUBLE_DOWN -> {
                    if (this.bettor.getHand().size() != 2 || this.bettor.getBalance() < this.betAmount){
                        break;
                    }
                    actionList.add(action);
                }
                case SPLIT -> {
                    if (this.bettor.getHand().size() != 2){
                        break;
                    }

                    if (this.bettor.getHand().getFirst() != this.bettor.getHand().getLast()){
                        break;
                    }
                    actionList.add(action);
                }

                case INSURANCE -> {
                    if (this.bettor.getHand().size() != 2){
                        break;
                    }
                    if (this.dealer.getFlippedCard().getRank() != 1){
                        break;
                    }
                    actionList.add(action);
                }
            }
        }
    }

    public void hit(){
        this.bettor.drawCard(this.deck);
        if (this.bettor.lost()){
            this.result = false;
        }
    }

    public void stand(){
        this.dealer.play(this.deck);
        this.calculateWinner();
    }

    public void calculateWinner(){
        if (this.bettor.getScore() > this.dealer.getScore() || this.dealer.lost()){
            this.result = true;
            return;
        }

        this.result = false;
    }


    /* O jogador pode dobrar a aposta, mas recebe apenas mais uma carta*/
    public void doubleDown(){
        this.bettor.drawCard(this.deck);
        this.bettor.setBalance(this.bettor.getBalance() - this.betAmount);
        this.betAmount += this.betAmount;

        if (this.bettor.lost()){
            this.result = false;
            return;
        }
        this.stand();
    }

    /* Se o jogador tem um par, pode dividir cada carta para uma mão diferente */
    public void split(){

    }

    /* Se o dealer tem um Ás como carta visível, o jogador pode fazer um seguro.
    Se o dealer tiver blackjack, o seguro é pago em 2:1.
    Ou seja, jogador aposta seguro, se ganhar recebe o que apostou de volta e mais 2x o que apostou.
     */
    public void insurance(){
        System.out.println("Escolha o valor da aposta de seguro: ");
    }

    public String getGameStateString() {
        return "Dealer: " + this.dealer.getHand() + " Pontos: " + this.dealer.getScore()
                + "\nApostador: " + this.bettor.getHand() + " Pontos: " + this.bettor.getScore();
    }


    public String getResultBoolToString(){
        return this.result ? "ganhou" : "perdeu";
    }

    public String getResultString() {
        String stringResult = getResultBoolToString();

        String output = "Você " + stringResult + "!\n";
        output += getGameStateString();

        return output;
    }

    public String toString(){
        return "Resultado: " + getResultBoolToString() + " | Ganhos/Perdas: " + betAmount +
                " | " + "Dealer: " + this.dealer.getHand() + " Pontos: " + this.dealer.getScore()
                + " | Apostador: " + this.bettor.getHand() + " Pontos: " + this.bettor.getScore();
    }
}
