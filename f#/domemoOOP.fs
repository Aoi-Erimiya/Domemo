(*
  domemoOOP.fs
  ver OOP.

  Copyright (c) 2018 Hiroaki Wada

  This software is released under the MIT License.
  http://opensource.org/licenses/mit-license.php
*)
open System
open System.Threading

type Player(name:string, cards:List<int>) = 
    let name:string = name
    let mutable cards:List<int> = cards

    member x.Name with get() = name

    member x.cardsToString(isMask:bool) = 
        let mutable cardsStr = ""
        if isMask then
            cards |> List.iter (fun x -> cardsStr <- cardsStr + "X")
        else
            cards |> List.iter (fun card -> cardsStr <- cardsStr + card.ToString())
        cardsStr

    member x.checkCard() = cards.Length = 0

    member x.guessCard(rand: Random) =
        rand.Next(1, 8)

    member x.matchCard(guessCard:int) = 
        if List.contains guessCard cards
        then
            let idx = List.findIndex (fun card -> card = guessCard) cards
            cards <- List.concat [ cards.[0..idx-1]; cards.[idx+1..cards.Length-1] ]
            true
        else
            false
    
type GameMaster() =
    let mutable cards:List<int> = []
    let mutable openCards:List<int> = []

    member x.Cards with get() = cards
    member x.OpenCards with get() = openCards

    member x.addOpenCards(addCard:int) =
        openCards <- List.sort <| List.append [addCard] openCards

    member x.createShuffledCards(rand:Random) = 
        let mutable cards = []
        for i in 1..7 do
            for _ in 1..i do
                cards <- List.append [i] cards

        let swap (array: _[]) x y =
            let tmp = array.[x]
            array.[x] <- array.[y]
            array.[y] <- tmp

        // shuffle an array (in-place)
        let shuffle array =
            Array.iteri (fun i _ -> swap array i (rand.Next(i, Array.length array))) array

        let cardArray = Array.ofList cards
        shuffle cardArray
    
        Array.toList cardArray
    
    // To run before playing
    member x.preparationGame(rand:Random) = 
        cards <- x.createShuffledCards(rand)
        x.takeInitialOpenCards(cards)

    member x.takeInitialOpenCards(cards:List<int>) =
        openCards <- List.sort cards.[cards.Length-4..cards.Length-1]


let showCards(label:string, cards:List<int>) =
    printf "%s -> " label
    cards |> List.iter (fun card -> printf "%d" card)
    printfn ""

let showPlayers(players:List<Player>) = 
    players |> List.iter (fun player -> (Console.WriteLine("{0} -> {1}", player.Name, player.cardsToString(List.last players = player))))

let createPlayers(cards:List<int>, playerNames:List<string>) = 
    let mutable players = []
    for i in 0..3 do
        let player = Player(playerNames.[i], cards.[5*i..5*(i+1)-1])
        players <- List.append [player] players
    players

[<EntryPoint>]
let main argv = 
    let rand = Random()

    let gm = GameMaster()
    gm.preparationGame(rand)

    let players = createPlayers(gm.Cards, ["Mizuki";"Izumi";"Ryoko";"Aoi"])
    
    let mutable isGameOver = false
    
    let rec nextGameRound() =
        printfn "----------------------"
        printfn "*Field*"
        showCards("Open cards", gm.OpenCards)
        printfn ""
        printfn "*Players*"
        showPlayers(players)
        printfn "----------------------"
        
        Thread.Sleep(500)
        
        for player in players do

            if not isGameOver then
                Thread.Sleep(500)
            
                let guessCard =
                    if List.last players = player then
                        printf "Please input guess number! -> "
                        Console.ReadLine() |> int
                    else
                        player.guessCard(rand)

                printfn "%s called -> %d" player.Name guessCard

                if player.matchCard(guessCard) then            
                    printfn "jackpot!"
                    gm.addOpenCards(guessCard)
                else
                    printfn "miss!"

                if player.checkCard() then
                    printfn "----------------------"
                    printfn "%s is Win!" player.Name
                    isGameOver <- true
                else
                    printfn ""
        
        if isGameOver then
            ()
        else
            printfn "Next round...."
            nextGameRound()
    
    nextGameRound()

    System.Console.ReadLine() |> ignore
    0
