
/**
 * Player.java
 *
 * Copyright (c) 2019 Hiroaki Wada
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
import java.util.List;

public class Player {
    private String name = "";
    private List<Integer> cards = null;

    Player(String name, List<Integer> cards) {
        this.name = name;
        this.cards = cards;
    }

    public List<Integer> getCards() {
        return this.cards;
    }

    public String getName() {
        return this.name;
    }

    public void show() {
        String cards = "";
        for (int card : this.cards) {
            cards += String.valueOf(card);
        }
        System.out.println(this.name + "->" + cards);
    }

    public void showMask() {
        String cards = "";
        for (int card : this.cards) {
            cards += "X";
        }
        System.out.println(this.name + "->" + cards);
    }

    public int matchCard(int guess_card) {
        try {
            this.cards.remove(this.cards.indexOf(guess_card));
            System.out.println("jackpot.");
            return guess_card;
        } catch (Exception e) {
            System.out.println("miss.");
            return 0;
        }
    }

    public boolean check() {
        return this.cards.size() == 0;
    }

}