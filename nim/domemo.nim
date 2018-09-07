#
# domemo.nim
#
# Copyright (c) 2018 Hiroaki Wada
#
# This software is released under the MIT License.
# http://opensource.org/licenses/mit-license.php
#
import random
import sequtils
import strutils
import algorithm

type
    Player = ref object of RootObj
        name*: string
        cards: seq[int]

proc check(player:Player): bool = return player.cards.len == 0

proc match(player:Player, guessNumber:int): int = 
    if player.cards.contains(guessNumber):
        player.cards.del(player.cards.find(guessNumber))
        player.cards.sort(system.cmp[int])
        echo "jackpot."
        return guessNumber
    else:
        echo "miss."
        return 0

proc show(player:Player): void =
    var cardStr = ""
    for card in player.cards:
        if player.name == "Player1":
            cardStr = cardStr & "X"
        else:
            cardStr = cardStr & $card
    echo player.name, " -> ", cardStr

proc show(cards:openarray[int], label:string): void =
    var cardStr = ""
    for card in cards:
        cardStr = cardStr & $card
    echo label, " -> ", cardStr

proc showCard(players:openarray[Player]): void =
    for player in players:
        player.show()

# Main Logic ---------------------------------------
randomize()

var cards:seq[int] = @[1,2,2,3,3,3,4,4,4,4,5,5,5,5,5,6,6,6,6,6,6,7,7,7,7,7,7,7]
cards.shuffle()

var openCards:seq[int] = cards[cards.len-4..cards.len-1]
openCards.sort(system.cmp[int])

var players:seq[Player] = @[]
for i in countup(0, 3):
    var playerCards:seq[int] = cards[i*5..(i+1)*5-1]
    playerCards.sort(system.cmp[int])
    players.add(Player(name: "Player" & $(i+1), cards: playerCards))

block mainLoop:
    while true:
        echo "---------"
        openCards.show("openCards")

        showCard(players)

        for player in players:
            var guessNumber:int = 0
            if player.name == "Player1":
                guessNumber = readLine(stdin).parseInt
            else:
                guessNumber = rand(7) + 1
        
            echo player.name, " is called -> ", guessNumber

            let result = player.match(guessNumber)
            if result > 0:
                openCards.add(result)
                openCards.sort(system.cmp[int])
            
            if player.check:
                echo player.name, " is Win!"
                break mainLoop
