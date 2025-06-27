package rmi.blackjack;

import java.util.ArrayList;
import java.util.Scanner;

public class Round {
    private final Dealer dealer;
    private final Bettor bettor;
    private final Deck deck;
    private final Scanner scanner = new Scanner(System.in);
    private final ArrayList<Action> actionList = new ArrayList<>();

    public Round(Dealer dealer, Bettor bettor, Deck deck){
        this.dealer = dealer;
        this.bettor = bettor;
        this.deck = deck;
    }

    public void start(){
        this.bettor.dropCards(this.deck);
        this.dealer.dropCards(this.deck);
        this.deck.shuffle();

        this.bettor.drawCards(this.deck, 2);
        this.dealer.drawCards(this.deck, 2);

        this.getNextAction();
    }
    //TODO implementar algo pra ver quem ganhou ou perdeu, que possa ser retornado para a classe session
    //TODO implementar ações
    //TODO implementar lógica cliente
    //TODO implementar lógica apostas
    public boolean bettorLost(){
        return this.bettor.getScore() > 21;
    }

    public void getNextAction(){
        if (this.bettorLost()){
            return;
        }
        getAvailableActions();
        printAvailableActions();
        int nextActionPosition;
        while (true){
            nextActionPosition = this.scanner.nextInt();
            if (1 <= nextActionPosition && nextActionPosition <= actionList.size()){
                break;
            }
            System.out.println("Ação inválida. \n");
            printAvailableActions();
        }
        executeNextAction(actionList.get(nextActionPosition));
    }

    public void executeNextAction(Action action){
        switch (action){
            case HIT -> this.hit();
            case STAND -> this.stand();
            case DOUBLE_DOWN -> this.doubleDown();
            case SPLIT -> this.split();
            case INSURANCE -> this.insurance();
        }
    }

    public void printAvailableActions(){
        System.out.println("Escolha a próxima ação: \n");

        int index = 1;
        for (Action action : actionList){
            System.out.println(index + ". " + action.getDescription() + "\n");
            index++;
        }
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
                    if (this.bettor.getHand().size() != 2){
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

    }

    public void stand(){

    }

    public void doubleDown(){

    }

    public void split(){

    }

    public void insurance(){

    }
}
