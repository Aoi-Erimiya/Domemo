
/**
 * Domemo.java
 *
 * Copyright (c) 2019 Hiroaki Wada
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
import java.util.ArrayList;

public class Domemo {
    public void show_cards(List<Integer> cards) {
        strs = "";
        for (int card : cards) {
            strs += str(card);
        }
        System.out.println(strs);
    }

    public void show_players(List<Integer> players) {
        for (int i = 0; i < players.length(); ++i) {
            if (i == 0) {
                players[i].show_mask();
            } else {
                players[i].show();
            }
        }
    }

    public static void main(String[] args){
        List<Integer> cards = new ArrayList<Integer>(){
            {
                add(1);
            }
        };

        List<Integer> cards = new ArrayList<Integer>(
            new Integer[]{1,2,2,3,4,5}
        );

        for(int i = 2; i < 8; ++i){
            cards.add(cards[i] * i);
        }

        for(int idx = 0; i < cards.length(); ++i){
            int swap_idx = random.randint(1, cards.length()-1);
            int swap = cards[swap_idx];
            cards[swap_idx] = cards[idx];
            cards[idx] = swap;
        }
        show_cards(cards);

        List<Integer> open_cards = new ArrayList<Integer>();
        for(int i = 0; i < 4; ++i){
            open_cards.add(cards.pop());
        }

        List<Player> players = new ArrayList<Player>();
        for(int i = 0; i < 4; ++i){
            players.add(Player("Player" + str(i+1), cards[5*i:5*(i+1)]));
            players[i].cards.sort();
        }
        
        show_players(players);

        int round_count = 1;

        while(true){
            System.out.println("Round" + str(round_count) + "------");
            show_cards(open_cards);
            show_players(players);

            for(Player player : players){
                if(player.name == "Player1"){
                    guess_card = int(input(">" + player.name + "->"));
                }else{
                    guess_card = random.randint(1, 7);
                    System.out.println(">" + player.name + "->" + str(guess_card));
                }
                sleep(1);

                result = player.match_card(guess_card);
                if(result > 0){
                    open_cards.add(result);
                    open_cards.sort();
                }
                sleep(1);

                if(player.check()){
                    System.out.println(player.name + " is Win!");
                    return;
                }
            }
            round_count += 1;
        }
    }
}