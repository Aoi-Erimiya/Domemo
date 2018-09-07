/*
  domemo.swift

  Copyright (c) 2018 Hiroaki Wada

  This software is released under the MIT License.
  http://opensource.org/licenses/mit-license.php
*/
import Foundation
import Glibc

class Player{
    var name: String
    var cards: [Int]

    init(name: String, cards: [Int]){
        self.name = name
        self.cards = cards
    }

    func show(){
        print(self.name + " -> ")
        showCards(cards: self.cards)
    }

    func check() -> Bool {
        return self.cards.count == 0
    }

    func match(guessCard: Int) -> Bool{
        if let idx = self.cards.index(of: guessCard){
            self.cards.remove(at: idx)
            print("jackpot!")
            return true
        }else{
            print("miss!")
            return false
        }
    }
}

func showCards(cards:[Int]){
    var str = ""
    for card in cards{
        str += "\(cards)"
    }
    print(str)
}

let cards:[Int] = [1,2,2,3,3,3,4,4,4,4,5,5,5,5,5,6,6,6,6,6,6,7,7,7,7,7,7,7]
for idx in cards.indices{
    let swp = cards[idx]
    let targetIdx = Int.random(in: 0 ..< 28)
    cards[idx] = cards[targetIdx]
    cards[targetIdx] = swp
}
var openCards:[Int] = Array(cards[cards.count ..< cards.count])

var players: [Player] = []
for idx in 0 ..< 4{
    players.append(Player(name: "Player\(idx)", cards: Array(cards[idx*5 ..< (idx+1)*5])))
}

var round = 1
MainLoop:while round < 100 {
    print("*round(\round)")
    var str = "openCard -> "
    showCards(cards: openCards)

    for player in players{
        player.show()
    }

    for player in players{
        let guessCard: Int = arc4random_uniform(7) + 1
        print("\(player.name) is Called -> \(guessCard)")
        if player.match(guessCard: guessCard){
            openCards.append(guessCard)
            if player.check(){
                print("\(player.name) is Win!")
                break MainLoop
            }
        }
    }
    round += 1
}