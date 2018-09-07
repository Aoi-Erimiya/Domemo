/*
  domemo.kt

  Copyright (c) 2018 Hiroaki Wada

  This software is released under the MIT License.
  http://opensource.org/licenses/mit-license.php
*/
import java.util.Random

class Player{
    val name: String
    val cards: MutableList<Int>

    constructor(name: String, cards: MutableList<Int>){
        this.name = name
        this.cards = cards
    }

    fun show(){
        println(this.name + " -> " + this.cards)
    }

    fun match(guessCard: Int): Boolean {
        val ret = cards.remove(guessCard)
        if(ret){
            println("jackpot!")
        }else{
            println("miss!")
        }
        return ret
    }

    fun check(): Boolean{
        return this.cards.count() == 0 
    }
}

fun main(args: Array<String>) {
    val cards = mutableListOf(1,2,2,3,3,3,4,4,4,4,5,5,5,5,5,6,6,6,6,6,6,7,7,7,7,7,7,7)
    cards.shuffle()

    val sliceCards = cards.slice(cards.count()-4 .. cards.count()-1).sorted()
    var openCards: MutableList<Int> = sliceCards.toMutableList()

    val players: MutableList<Player> = mutableListOf()
    for(i in 0 until 4){
        var playerCards = cards.slice(i*5 .. (i+1)*5-1).sorted()
        players.add(Player("Player"+(i+1), playerCards.toMutableList()))
    }

    var round = 1
    while(round < 30){
        println("*round" + round)
        println(openCards)
        for(player in players){
            player.show()
        }

        for(player in players){
            val random: Random = Random()
            val guessCard = random.nextInt(7)+1
            println(player.name + " is Called -> " + guessCard)

            if(player.match(guessCard)){
                openCards.add(guessCard)
                val sortedOpenCards = openCards.sorted()
                openCards = sortedOpenCards.toMutableList()

                if(player.check()){
                    println(player.name + " is Win!")
                    return
                }
            }
        }
        round++
    }
}