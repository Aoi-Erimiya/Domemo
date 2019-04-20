
/**
 * Player.java
 *
 * Copyright (c) 2019 Hiroaki Wada
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
public class Player {
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
        cards = "";
        for (int card : this.cards) {
            cards += str(card);
        }
        System.out.println(this.name + "->" + cards);
    }

    public void show_mask() {
        cards = "";
        for (int card : this.cards) {
            cards += "X";
        }
        System.out.println(this.name + "->" + cards);
    }

    public int match_card(int guess_card) {
        try {
            this.cards.remove(guess_card);
            System.out.println("jackpot.");
            return guess_card;
        } catch (Exception e) {
            System.out.println("miss.");
            return 0;
        }
    }

    public boolean check() {
        return len(this.cards) == 0;
    }

}