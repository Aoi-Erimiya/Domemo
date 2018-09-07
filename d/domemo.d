/*
  domemo.d

  Copyright (c) 2018 Hiroaki Wada

  This software is released under the MIT License.
  http://opensource.org/licenses/mit-license.php
*/
import std.stdio : write, writeln;
import std.random;
import std.algorithm;
import std.conv;
import std.format;
import std.array;

class Player
{
    protected string name;
    protected int[] cards;

    this(string name, int[] cards)
    {
        this.name = name;
        this.cards = cards;
    }

    bool check()
    {
        return this.cards.length == 0;
    }

    final string getName()
    {
        return name;
    }

    int match(int guessNumber)
    {
        auto hitCard = 0;
        foreach (idx; 0 .. this.cards.length)
        {
            if (this.cards[idx] == guessNumber)
            {
                hitCard = guessNumber;
                writeln("jackpot");
                remove(this.cards, idx);
                this.cards.length = this.cards.length - 1;
                break;
            }
        }
        if (hitCard == 0)
        {
            writeln("miss");
        }
        return hitCard;
    }

    void show()
    {
        write(this.name, "->");
        foreach (int card; this.cards)
        {
            write(card);
        }
        writeln();
    }
}

void show(string label, int[] cards)
{
    write(label, "->");
    foreach (int card; cards)
    {
        write(card);
    }
    writeln();
}

void main()
{
    auto rnd = Random(unpredictableSeed);

    int[] cards = [1, 2, 2, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 5, 6, 6, 6, 6,
        6, 6, 7, 7, 7, 7, 7, 7, 7];
    randomShuffle(cards, rnd);
    show("cards", cards);

    auto openCards = cards[cards.length - 4 .. cards.length];
    writeln(openCards);

    Player[] players = [];
    players.length = 4;

    foreach (idx; 0 .. 4)
    {
        auto writer = appender!string();
        formattedWrite(writer, "%s%d", "Player", idx + 1);
        players[idx] = new Player(writer.data, cards[idx * 5 .. (idx + 1) * 5]);
        players[idx].show();
    }

    mainLoop: while (true)
    {
        show("openCards", cards);
        foreach (Player player; players)
        {
            player.show();
        }

        foreach (Player player; players)
        {
            int guessNumber = uniform(1, 8, rnd);
            writeln(player.getName(), " is Called->", guessNumber);
            auto result = player.match(guessNumber);
            if (result > 0)
            {
                openCards.length = openCards.length + 1;
                openCards[openCards.length-1] = result;
                if (player.check())
                {
                    writeln(player.getName(), " is Win!");
                    break mainLoop;
                }
            }
        }
    }
}