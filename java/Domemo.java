/**
 * Domemo.java
 *
 * Copyright (c) 2019 Hiroaki Wada
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
from time import sleep;

public class Player(){
    public Player(String name, List<int> cards){
        this.name = name;
        this.cards = cards;
    }

		public List<int> getCards(){
			return this.cards;
		}

		public String getName(){
			return this.name;
		}

    public void show(){
        cards = "";
        for(int card : this.cards){
            cards += str(card);
        }
        System.out.println(this.name + "->" + cards);
    }

    public void show_mask(){
        cards = "";
        for(int card : this.cards){
            cards += "X";
        }
        System.out.println(this.name + "->" + cards);
    }
    public int match_card(int guess_card){
        try{
            this.cards.remove(guess_card);
            System.out.println("jackpot.");
            return guess_card;
        }catch(Exception e){
            System.out.println("miss.");
            return 0;
        }
    }

    public boolean check(){
        return len(this.cards) == 0;
    }
}

public void show_cards(List<int> cards){
    strs = "";
    for(int card : cards){
        strs += str(card);
    }
    System.out.println(strs);
}

public void show_players(List<int> players){
    for(int i = 0; i < players.length(); ++i){
        if(i == 0){
            players[i].show_mask();
        }else{
            players[i].show();
        }
		}
}

public static void main(String[] args){
    cards = [1];
    open_cards = [];
    for (int i = 2; i < 8; ++i){
        cards += [i] * i;
    }

    for(int idx = 0; i < cards.length(); ++i){
        swap_idx = random.randint(1, cards.length()-1);
        swap = cards[swap_idx];
        cards[swap_idx] = cards[idx];
        cards[idx] = swap;
    }
    show_cards(cards);
    
    for(int i = 0; i < 4; ++i){
        open_cards.append(cards.pop());
    }

    players = [];
    for(int i = 0; i < 4; ++i){
        players.append(Player("Player" + str(i+1), cards[5*i:5*(i+1)]));
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
                open_cards.append(result);
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
