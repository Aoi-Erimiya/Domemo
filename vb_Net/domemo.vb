Module Module1

    ' Player2～4の手札を表示する
    Sub show(cards As List(Of Integer))
        For Each card In cards
            Console.Write(card)
        Next
        Console.WriteLine("")
    End Sub

    ' Player1の手札を伏せて表示する
    Sub showMask(cards As List(Of Integer))
        For Each card In cards
            Console.Write("X")
        Next
        Console.WriteLine("")
    End Sub

    ' 7: 各プレイヤーは自分が持っているカードを予想して、宣言する
    Sub declarationCard(declaration As Integer,
                        playerCards As List(Of Integer),
                        playerName As String,
                        openCards As List(Of Integer))
        Console.WriteLine(playerName & " -> " & declaration)

        ' 8: 予想があっていたら、手札からカードを場に移動して表にする
        If playerCards.Remove(declaration) Then
            Console.WriteLine("jackPot!")
            openCards.Add(declaration)

            ' 9: 最初に手札がなくなったプレイヤーが勝ち
            If playerCards.Count = 0 Then
                Console.WriteLine(playerName & " win!")

                ' 判定がループの外に移動したので何か入力されるまで待つ
                Console.ReadKey()

                ' ゲームを終了させる
                Environment.Exit(0)
            End If
        End If
    End Sub

    Sub Main()
        ' 1: 1～7までのカードを用意する
        Dim cards As List(Of Integer) = New List(Of Integer)

        For i As Integer = 1 To 7
            For j As Integer = 1 To i
                cards.Add(i)
            Next
        Next

        ' 2: カードをシャッフルする
        Dim shuffledCards As List(Of Integer) =
            cards.OrderBy(Function(x) Guid.NewGuid()).ToList

        show(shuffledCards)

        ' 3: 場にカードを4枚表にする
        Dim openCards As List(Of Integer) = shuffledCards.GetRange(0, 4)

        ' 4: 4枚のカードを、内容を伏せたままゲームから除外する
        Dim closeCards As List(Of Integer) = shuffledCards.GetRange(4, 4)

        ' 5: カードを5枚ずつ4人のプレイヤーに配る
        Dim player1 As List(Of Integer) = shuffledCards.GetRange(8, 5)
        Dim player2 As List(Of Integer) = shuffledCards.GetRange(13, 5)
        Dim player3 As List(Of Integer) = shuffledCards.GetRange(18, 5)
        Dim player4 As List(Of Integer) = shuffledCards.GetRange(23, 5)

        Dim random As New Random()

        ' 6: 各プレイヤーは自分には見えないようにカードを表にする
        For i As Integer = 1 To 30
            Console.WriteLine("---------------------------------")
            ' 場のカードとプレイヤーの手札を表示する
            Console.Write("open->")
            show(openCards)
            Console.Write("player1->")
            showMask(player1)
            Console.Write("player2->")
            show(player2)
            Console.Write("player3->")
            show(player3)
            Console.Write("player4->")
            show(player4)

            ' 7: 各プレイヤーは他のプレイヤーのカードを見ながら、自分が持っているカードを予想して、宣言する
            ' Player1
            System.Console.Write("input num ->")
            Dim inputKey As ConsoleKeyInfo = Console.ReadKey()
            System.Console.WriteLine("")

            Dim declaration As Integer = Integer.Parse(inputKey.KeyChar)
            declarationCard(declaration, player1, "player1", openCards)

            ' Player2
            declaration = random.Next(7) + 1
            declarationCard(declaration, player2, "player2", openCards)

            ' Player3
            declaration = random.Next(7) + 1
            declarationCard(declaration, player3, "player3", openCards)

            ' Player4
            declaration = random.Next(7) + 1
            declarationCard(declaration, player4, "player4", openCards)

            ' 何か入力したら次のターンにいくようにする
            'Console.ReadKey()
        Next

    End Sub

End Module