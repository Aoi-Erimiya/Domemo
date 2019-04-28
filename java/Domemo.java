
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
    public static void showCards(List<Integer> cards) {
        String s = "";
        for (int card : cards) {
            s += String.valueOf(card);
        }
        System.out.println(s);
    }

    public static void showPlayers(List<Player> players) {
        for (int i = 0; i < players.size(); ++i) {
            if (i == 0) {
                players.get(i).showMask();
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
            int swapIdx = random.nextInt(cards.size());
            int swap = cards.get(swapIdx);
            cards.set(swapIdx, cards.get(idx));
            cards.set(idx, swap);
        }
        showCards(cards);

        List<Integer> openCards = new ArrayList<Integer>();
        for (int i = 0; i < 4; ++i) {
            openCards.add(cards.remove(cards.size() - 1));
        }

        List<Player> players = new ArrayList<Player>();
        for (int i = 0; i < 4; ++i) {
            players.add(new Player("Player" + String.valueOf(i + 1),
                    cards.stream().skip(5 * i).limit(5).collect(Collectors.toList())));
            players.get(i).getCards().sort(Comparator.naturalOrder());
        }

        showPlayers(players);

        int roundCount = 1;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Round" + String.valueOf(roundCount) + "------");
            showCards(openCards);
            showPlayers(players);

            for (Player player : players) {
                int guessCard = 0;
                if (player.getName().equals("Player1")) {
                    System.out.print(">" + player.getName() + "->");
                    String inputCard = scanner.next();
                    guessCard = Integer.valueOf(inputCard);
                } else {
                    guessCard = random.nextInt(7) + 1;
                    System.out.println(">" + player.getName() + "->" + String.valueOf(guessCard));
                }

                int result = player.matchCard(guessCard);
                if (result > 0) {
                    openCards.add(result);
                    openCards.sort(Comparator.naturalOrder());
                }

                if (player.check()) {
                    System.out.println(player.getName() + " is Win!");
                    return;
                }
            }
            roundCount++;
        }
    }
}