/*
  domemo.rs

  Copyright (c) 2018 Hiroaki Wada

  This software is released under the MIT License.
  http://opensource.org/licenses/mit-license.php
*/
extern crate rand;
use rand::Rng;
use std::io;
use std::thread;
use std::time::Duration;

struct Player {
    name: String,
    cards: Vec<i32>,
}

impl Player {
    fn new(name: &str, cards: Vec<i32>) -> Player {
        Player {
            name: name.to_string(),
            cards: cards,
        }
    }

    fn show(&self) {
        print!("{}->", self.name);
        for x in 0..self.cards.len() {
            print!("{},", self.cards[x]);
        }
    }

    fn show_mask(&self) {
        print!("{}->", self.name);
        for _x in 0 .. self.cards.len() {
            print!("X,")
        }
    }

    fn match_card(&mut self, guess_card:i32) -> i32 {
        let mut ret = 0;
        if self.cards.contains(&guess_card) == true {
            println!("jackpot!");
            let result = self.cards.binary_search(&guess_card);
            let card_index: usize = result.unwrap();
            ret = self.cards.remove(card_index);
        } else {
            println!("miss!");
        }
        ret
    }
}

fn show_cards(cards: &Vec<i32>) {
    for x in 0..cards.len() {
        print!("{},", cards[x]);
    }
    println!("");
}

fn show_players(players: &Vec<Player>) {
    for x in 0..players.len() {
        players[x].show();
        println!("");
    }
}

fn show_players_game(players: &Vec<Player>){
    for x in 0 .. players.len() {
        if x == 0 {
            players[x].show_mask();
        } else {
            players[x].show();
        }
        println!("");
    }
}

fn check_card(player: &Player) -> bool {
    let mut res = false;
    if player.cards.len() == 0 {
        println!("{} is Win!", player.name);
        res = true;
    }
    res
}

fn main() {
    let mut cards:Vec<i32> = vec![
        1,2,2,3,3,3,4,4,4,4,5,5,5,5,5,6,6,6,6,6,6,7,7,7,7,7,7,7
    ];

    let mut reject_cards: Vec<i32> = Vec::new();

    let cards_len = cards.len();

    for idx in 0 .. cards_len {
        cards.swap(idx, rand::thread_rng().gen_range(0, cards_len));
    }
    for _x in 0 .. 4 {
        reject_cards.push(cards.pop().unwrap());
    }
    let mut open_cards: Vec<i32> = Vec::new();
    for _x in 0 .. 4 {
        open_cards.push(cards.pop().unwrap());
    }

    let mut players: Vec<Player> = Vec::new();
    let mut player_cnt = 0;
    for chunk in cards.chunks(5) {
        player_cnt += 1;
        let mut player_cards = chunk.to_vec();
        player_cards.sort();
        let mut player = Player::new(&format!("Player{}", player_cnt.to_string()), player_cards);
        players.push(player);
    }

    show_players(&players);

    let mut round = 1;
    loop {
        println!("*round{}", round);
        print!("open cards -> ");
        show_cards(&open_cards);
        show_players_game(&players);

        println!(">player1 turn");
        let mut guess = String::new();

        io::stdin()
            .read_line(&mut guess)
            .expect("Failed to read line");
        
        println!("You guessed:{}", guess);

        let guess:i32 = match guess.trim().parse() {
            Ok(num) => num,
            Err(_) => continue,
        };

        thread::sleep(Duration::from_millis(1000));

        let res:i32 = players[0].match_card(guess);
        if res > 0 {
            open_cards.push(res);
            open_cards.sort();
        }

        if check_card(&players[0]) {
            break;
        }

        for x in 1 .. players.len() {
            thread::sleep(Duration::from_millis(1000));

            let other_guess:i32 = rand::thread_rng().gen_range(1, 6);
            println!("{} called {}", players[x].name, other_guess);

            let other_res: i32 = players[x].match_card(other_guess);
            if other_res > 0 {
                open_cards.push(other_res);
                open_cards.sort();
            }
            if check_card(&players[x]){
                break;
            }
        }
        thread::sleep(Duration::from_millis(1000));
        round += 1;
    }
}