
/**
 * Domemo.java
 *
 * Copyright (c) 2019 Hiroaki Wada
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.Random;
import java.util.Comparator;

@SuppressWarnings("serial")
public class Domemo {
    public static void show_cards(List<Integer> cards) {
        String s = "";
        for (int card : cards) {
            s += String.valueOf(card);
        }
        System.out.println(s);
    }

    public static void show_players(List<Player> players) {
        for (int i = 0; i < players.size(); ++i) {
            if (i == 0) {
                // players.get(i).show_mask();
                players.get(i).show();
            } else {
                players.get(i).show();
            }
        }
    }

    public static void main(String[] args) {
        List<Integer> cards = new ArrayList<Integer>() {
            {
                add(1);
            }
        };

        for (int i = 2; i < 8; ++i) {
            for (int j = 0; j < i; ++j) {
                cards.add(i);
            }
        }

        Random random = new Random();

        for (int idx = 0; idx < cards.size(); ++idx) {
            int swap_idx = random.nextInt(cards.size());
            int swap = cards.get(swap_idx);
            cards.set(swap_idx, cards.get(idx));
            cards.set(idx, swap);
        }
        show_cards(cards);

        List<Integer> open_cards = new ArrayList<Integer>();
        for (int i = 0; i < 4; ++i) {
            open_cards.add(cards.remove(cards.size() - 1));
        }

        List<Player> players = new ArrayList<Player>();
        for (int i = 0; i < 4; ++i) {
            players.add(new Player("Player" + String.valueOf(i + 1),
                    cards.stream().skip(5 * i).limit(5).collect(Collectors.toList())));
            // cards.subList(5 * i, 5 * (i + 1)).stream().collect(Collectors.toList())));
            players.get(i).getCards().sort(Comparator.naturalOrder());
        }

        show_players(players);

        int round_count = 1;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Round" + String.valueOf(round_count) + "------");
            show_cards(open_cards);
            show_players(players);

            for (Player player : players) {
                int guess_card = 0;
                if (player.getName().equals("Player1")) {
                    System.out.print(">" + player.getName() + "->");
                    String inputCard = scanner.next();
                    guess_card = Integer.valueOf(inputCard);
                } else {
                    guess_card = random.nextInt(7) + 1;
                    System.out.println(">" + player.getName() + "->" + String.valueOf(guess_card));
                }
                // Thread.sleep(1000);

                int result = player.match_card(guess_card);
                if (result > 0) {
                    open_cards.add(result);
                    open_cards.sort(Comparator.naturalOrder());
                }
                // Thread.sleep(1000);

                if (player.check()) {
                    System.out.println(player.getName() + " is Win!");
                    return;
                }
            }
            round_count++;
        }
    }
}