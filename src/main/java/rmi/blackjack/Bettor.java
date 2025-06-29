package rmi.blackjack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Bettor extends Player implements Serializable {
    private String username;
    private int balance;

    public Bettor(String username, int balance) {
        super();
        this.username = username;
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
