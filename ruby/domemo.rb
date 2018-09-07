#
# domemo.rb
#
# Copyright (c) 2018 Hiroaki Wada
#
# This software is released under the MIT License.
# http://opensource.org/licenses/mit-license.php
#
class Player
    attr_reader :name, :cards

    def initialize(name, cards)
        @name = name
        @cards = cards
    end

    def show
        strs = ""
        for card in cards
            strs += card.to_s
        end
        puts strs
    end

    def show_mask
        strs = "X" * @cards.count
        puts strs
    end

    def see_other_players_cards(open_cards, players)
        cards = open_cards.clone
        for player in players
            if player.name != @name
                cards.concat(player.cards)
            end
        end

        return cards.sort!
    end

    def guess_card(open_cards, players)
        cards = see_other_players_cards(open_cards, players)
        candidate_cards = []

        for candidate_card in 1..7
            if candidate_card > cards.count(candidate_card)
                candidate_cards.push(candidate_card)
            end
        end

        rng = Random.new
        percent = rng.rand(1..100)
        if percent < 80
            return candidate_cards.sample
        else
            return rng.rand(1..7)
        end
    end

    def match_card(guess_card)
        idx = @cards.find_index(guess_card)
        if idx.nil?
            puts "miss."
            return 0
        else
            puts "jackpot."
            return @cards.delete_at(idx)
        end
    end

    def check()
        return @cards.count == 0
    end
end

def show_cards(cards)
    strs = ""
    for card in cards
        strs += card.to_s
    end
    puts strs
end

def show_players(players)
    for i in 0..players.count-1
        print players[i].name + " -> "
        if i == 0
            players[i].show_mask
        else
            players[i].show
        end
    end
end

def main
    cards = [1] + [2]*2 + [3]*3 + [4]*4 + [5]*5 + [6]*6 + [7]*7
    cards.shuffle!

    open_cards = cards.slice!(cards.count - 4, 4).sort
    
    players = []
    for i in 0..3
        players.push(Player.new("Player"+(i+1).to_s, cards.slice(i*5, 5).sort))
    end

    round_count = 1
    loop do
        puts "Round" + round_count.to_s + "--------"
        print "open -> "
        show_cards(open_cards)
        show_players(players)

        for player in players
            print ">" + player.name + " call -> "
            sleep(1)
            if player.name == "Player1"
                guess_card = gets.chomp!.to_i
            else
                guess_card = player.guess_card(open_cards, players)
                puts guess_card
            end

            result = player.match_card(guess_card)

            if result > 0
                open_cards.push(result)
                open_cards.sort!
            end

            if player.check
                puts player.name + " is Win!"

                if player.name != "Player1"
                    print "The remaining cards -> "
                    players[0].show
                end
                
                return
            end
        end

        round_count += 1
    end
end

main