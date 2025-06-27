package rmi.blackjack;

import java.util.ArrayList;
import java.util.List;

public class Bettor extends Player{
    private String username;
    private int balance;

    public Bettor(String username, int balance) {
        super();
        this.username = username;
        this.balance = balance;
    }

}
