#
# domemo.py
#
# Copyright (c) 2018 Hiroaki Wada
#
# This software is released under the MIT License.
# http://opensource.org/licenses/mit-license.php
#
import random
from time import sleep

class Player:
    def __init__(self, name, cards):
        self.name = name
        self.cards = cards
    
    def show(self):
        cards = ""
        for card in self.cards: 
            cards += str(card)
        print (self.name + "->" + cards)
    
    def show_mask(self):
        cards = ""
        for card in self.cards:
            cards += "X"
        print (self.name + "->" + cards)
    
    def match_card(self, guess_card):
        try:
            self.cards.remove(guess_card)
            print("jackpot.")
            return guess_card
        except ValueError:
            print("miss.")
            return 0
    
    def check(self):
        return len(self.cards) == 0

def show_cards(cards):
    strs = ""
    for card in cards:
        strs += str(card)
    print strs

def show_players(players):
    for i in range(0, len(players)):
        if i == 0:
            players[i].show_mask()
        else:
            players[i].show()

def main():
    cards = [1]
    open_cards = []
    for i in range(2,8):
        cards += [i] * i
    
    for idx in range(0, len(cards)):
        swap_idx = random.randint(1, len(cards)-1)
        swap = cards[swap_idx]
        cards[swap_idx] = cards[idx]
        cards[idx] = swap
    
    show_cards(cards)
    
    for i in range(0,4):
        open_cards.append(cards.pop())
    
    players = []
    for i in range(0, 4):
        players.append(Player("Player" + str(i+1), cards[5*i:5*(i+1)]))
        players[i].cards.sort()

    show_players(players)

    round_count = 1
    while True:
        print("Round" + str(round_count) + "------")
        show_cards(open_cards)
        show_players(players)

        for player in players:
            if player.name == "Player1":
                guess_card = int(input(">" + player.name + "->"))
            else:
                guess_card = random.randint(1, 7)
                print(">" + player.name + "->" + str(guess_card))
            
            sleep(1)

            result = player.match_card(guess_card)
            if result > 0:
                open_cards.append(result)
                open_cards.sort()
            
            sleep(1)

            if player.check():
                print(player.name + " is Win!")
                return
        
        round_count += 1

main()
