//
// domemo.go
//
// Copyright (c) 2018 Hiroaki Wada
//
// This software is released under the MIT License.
// http://opensource.org/licenses/mit-license.php
//
package main

import(
	"fmt"
	"math/rand"
	"sort"
)

type Player struct {
	name string
	cards []int
}

func (p Player)check() bool {
	return len(p.cards) == 0
}

func (p Player) show() {
	fmt.Println(p.name + " -> ", p.cards)
}

func match(p *Player, guessCard int) bool {
	ret := false
	cardLen := len(p.cards)
	for idx := 0; idx < cardLen; idx++ {
		if p.cards[idx] == guessCard {
			fmt.Println("jackpot!")
			ret = true
			if idx == cardLen{
				p.cards = p.cards[0:idx-1]
			}else if idx == 0 {
				p.cards = p.cards[idx+1:cardLen]
			}else{
				p.cards = append(p.cards[0:idx], p.cards[idx+1:cardLen]...)
			}
			break;
		}
	}

	if !ret {
		fmt.Println("miss!")
	}

	return ret
}

func main(){
	cards := [28]int{1,2,2,3,3,3,4,4,4,4,5,5,5,5,5,6,6,6,6,6,6,7,7,7,7,7,7,7}
	cardLen := len(cards)
	for idx := 0; idx < cardLen; idx++ {
		target := rand.Intn(cardLen)
		swp := cards[target]
		cards[target] = cards[idx]
		cards[idx] = swp
	}

	openCards := cards[cardLen-4:cardLen]
	players := []Player{}

	for idx := 0; idx < 4; idx++ {
		name := "Player" + fmt.Sprintf("%d", idx+1)
		playerCards := cards[5*idx:5*(idx+1)]
		sort.Sort(sort.IntSlice(playerCards))
		players = append(players, Player{name, playerCards})
	}

	round := 1
GAME_LOOP:
	for round < 30 {
		fmt.Println("*round", round)
		fmt.Println("opencards -> ", openCards)
		for idx := 0; idx < 4; idx++ {
			players[idx].show()
		}

		for idx := 0; idx < 4; idx++ {
			guessCard := rand.Intn(7) + 1
			fmt.Println(players[idx].name, " is Called -> ", guessCard)
			if match(&players[idx], guessCard){
				openCards = append(openCards, guessCard)
				sort.Sort(sort.IntSlice(openCards))
			}
			if players[idx].check(){
				fmt.Println(players[idx].name, " is Win!")
				break GAME_LOOP
			}
		}
		round++
	}
}