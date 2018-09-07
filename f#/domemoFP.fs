(*
  domemoFP.fs
  ver FP.

  Copyright (c) 2018 Hiroaki Wada

  This software is released under the MIT License.
  http://opensource.org/licenses/mit-license.php
*)
open System
open System.Threading

[<AbstractClass; Sealed>]
type Math private() =
    static let rand = Random();
    static member Next(arg : int) : int =
        rand.Next(arg)

// Hold the number of cards.
type CardPair(cardNumber:int, cardCount:int) =
    let cardNumber:int = cardNumber
    let cardCount:int = cardCount

    member x.CardNumber with get() = cardNumber
    member x.CardCount with get() = cardCount

    override x.ToString() = x.CardNumber.ToString() + ":" + x.CardCount.ToString()

// Hold the card authority.
type CardAuth(owner:string, cardNumber:int) = 
    let mutable owner:string = owner
    let cardNumber:int = cardNumber

    member x.Owner with get() = owner
    member x.Owner with set(value) = owner <- value
    member x.CardNumber with get() = cardNumber

    override x.ToString() = x.CardNumber.ToString()

// Assign ownership of the card.
let rec assignCards(cards:List<CardAuth>, assignName, assignCount) = 
    if assignCount = 0 then
        ()
    else
        let target = Math.Next(28)
        if cards.[target].Owner = "none" then
            cards.[target].Owner <- assignName
            assignCards(cards, assignName, assignCount - 1)
        else      
            assignCards(cards, assignName, assignCount)

// Functional super spaghetti monster!
let domemo() =
    let waitMilliseconds = 700;
    let cardNumbers = [1;2;3;4;5;6;7]
    let playerNames = ["MZK";"IZM";"HRT";"AKR"]

    let mutable cards = []
    for i in 1..7 do
        for _ in 1..i do
            cards <- List.append [CardAuth("none", i)] cards

    // Assign ownership of the card.
    assignCards(cards, "open", 4)
    assignCards(cards, "reject", 4)
    playerNames |> List.iter(fun x -> assignCards(cards, x, 5))

    // Cards not owned.
    let mutable missCards:List<CardAuth> = []

    let mutable isGameOver = false
    let rec nextGameRound() = 
        printfn "----------------------"
        printfn "*Field*"
        let mutable candidateCards = []
        cardNumbers
        |> List.iter(fun cardNumber ->
            let classifiedCards = cards |> List.filter (fun ca -> ca.Owner = "open") |> List.filter(fun ca -> ca.CardNumber = cardNumber)
            candidateCards <- List.append candidateCards [CardPair(cardNumber, classifiedCards.Length)]
        )
        printfn "%A" candidateCards
        printfn ""
        printfn "*Players*"
        playerNames |> List.iter(fun x -> 
            printf "%s " x
            cards |> List.filter (fun ca -> ca.Owner = x) |> printfn "%A") 
        printfn "----------------------"

        Thread.Sleep(waitMilliseconds)

        playerNames
        |> List.iter(fun playerName ->
            if not isGameOver then
                printf "*%s " playerName
                // Extract cards visible from player's point of view.
                let showCards = cards |> List.filter (fun ca -> ca.Owner <> playerName && ca.Owner <> "reject")

                // Classify by card number and calculate the estimated remaining number.
                let mutable candidateCards = []
                cardNumbers
                |> List.iter(fun cardNumber ->
                    let classifiedCards = showCards |> List.filter(fun ca -> ca.CardNumber = cardNumber)
                    candidateCards <- List.append candidateCards [CardPair(cardNumber, cardNumber - classifiedCards.Length)]
                )
                printfn "%A" candidateCards

                // Estimate the card with the highest likelihood of hit (excluding items declared but not hit).
                let guessCard:CardPair =
                    candidateCards
                    |> List.filter(fun cp -> 
                        missCards |> List.tryFind(fun cam -> cam.Owner = playerName && cam.CardNumber = cp.CardNumber) = None)
                    |> List.maxBy(fun cp -> cp.CardCount)

                guessCard.CardNumber |> printfn "%A"
                Thread.Sleep(waitMilliseconds)

                if cards |> List.tryFind(fun ca -> ca.Owner = playerName && ca.CardNumber = guessCard.CardNumber) = None then
                    printfn "Miss!"
                    missCards <- List.append [CardAuth(playerName, guessCard.CardNumber)] missCards
                else
                    printfn "Jackpot!"
                    let hitCards = cards |> List.filter(fun ca -> ca.Owner = playerName && ca.CardNumber = guessCard.CardNumber)
                    let otherCards = cards |> List.filter(fun ca -> ca.Owner = playerName && ca.CardNumber <> guessCard.CardNumber || ca.Owner <> playerName)
                    cards <- List.append (List.append [CardAuth("open", hitCards.Head.CardNumber)] hitCards.Tail) otherCards
                    
                    // If there is no ownership card, the game ends.
                    if cards |> List.tryFind(fun ca -> ca.Owner = playerName) = None then
                        isGameOver <- true
                        printfn "%s is Win!" playerName

            Thread.Sleep(waitMilliseconds)
        )
        if isGameOver then
            ()
        else
            nextGameRound()

    // mainloop
    nextGameRound()

[<EntryPoint>]
let main argv = 
    domemo()
    Console.ReadLine() |> ignore
    0
