class Board(var rows: Int = 6,
             var columns: Int = 7,) {
    var firstPlayer: String
    var secondPlayer: String
    var totalGame: Int

    init {
        println("Connect Four")
        println("First player's name:")
        firstPlayer = readln()
        println("Second player's name:")
        secondPlayer = readln()

        val regex = Regex("[0-9]?. ?x ?.[0-9]?")

        do {
            println("Set the board dimensions (Rows x Columns)")
            println("Press Enter for default (6 x 7)")

            var bord = readln().lowercase().replace("\t", "").replace(" ".toRegex(), "")

            if (bord == "") {
                break
            } else if (!regex.matches(bord)) {
                println("Invalid input")
            } else {
                var (rowsTemp, columnsTemp) = bord.split(("x")).map { it.toInt() }
                if (rowsTemp < 5 || rowsTemp > 9) {
                    println("Board rows should be from 5 to 9")
                } else if ( columnsTemp < 5 || columnsTemp > 9) {
                    println("Board columns should be from 5 to 9")
                } else {
                    rows = rowsTemp
                    columns = columnsTemp
                    break
                }
            }
        }
        while (true)
        totalGame = countGame()
    }

    var boardList: MutableList<MutableList<String>> = MutableList(rows) {MutableList(columns) {" "} }
    var myTurn = "o"
    var field = rows * columns
    var score = mutableListOf<Int>(0, 0)

    fun printInformation() {
        println("${firstPlayer} VS ${secondPlayer}")
        println("${rows} X ${columns} board")
        if (totalGame == 1) {
            println("Single game")
        } else {
            println("Total $totalGame games")
        }
    }

    fun printBoard() {
        for (i in 1..columns) {
            print(" " + i)
        }
        println(" ")

        for (j in boardList) {
            print("║")
            print(j.joinToString("║"))
            println("║")
        }
        print("╚")
        for (e in 1..(columns - 1)) {
            print("═╩")
        }
        println("═╝")
    }

    fun move(myMoveInt: Int) {
        if (boardList[0][myMoveInt] == " ") {
            for (n in boardList.size - 1 downTo 0) {
                if (boardList[n][myMoveInt] == " ") {
                    boardList[n][myMoveInt] = myTurn
                    this.printBoard()
                    this.myTurn = if (this.myTurn == "o") "*" else "o"
                    field--
                    break
                }
            }
        } else {
            println("Column ${myMoveInt + 1} is full")
        }
    }


    fun correctInput(value: String): Boolean {
        try {
            val valueInt = value.toInt()
            if (valueInt > 0 && valueInt <= columns) {
                return true
            } else {
                println("The column number is out of range (1 - $columns)")
                return false
            }
        }
        catch (e: NumberFormatException){
            println("Incorrect column number")
            return false
        }
    }

    fun whoseNext() {
        if (myTurn == "*") {
            println("${this.secondPlayer}'s turn:")
        } else {
            println("${this.firstPlayer}'s turn:")
        }
    }

    fun whoWon(): Boolean {
        val sample1 = "o o o o"
        val sample2 = "* * * *"
        var tempNum1: Int
        var tempNum2: Int
        var text: String
        val winnerText = if (myTurn == "*") "Player $firstPlayer won" else "Player $secondPlayer won"

        // horizontal check
        for (list in boardList) {
            if (sample1 in list.joinToString(" ") || sample2 in list.joinToString(" ")) {
                println(winnerText)
                if (myTurn == "*") {
                    score[0] = score[0] + 2
                } else {
                    score[1] = score[1] + 2
                }
                return true
            }
        }

        // vertical check
        for (i in 0 until columns){
            text = ""
            for (n in 0 until rows) {
                text = text + boardList[n][i] + " "
            }
            if (sample1 in text || sample2 in text) {
                println(winnerText)
                if (myTurn == "*") {
                    score[0] = score[0] + 2
                } else {
                    score[1] = score[1] + 2
                }
                return true
            }
        }

        // diagonal check
        for (n in 3 until rows) {
            tempNum1 = n
            tempNum2 = 0
            text = ""
            loop@while (tempNum1 >= 0) {
                if (tempNum2 == columns) break@loop
                text = text + boardList[tempNum1--][tempNum2++] + " "
            }
            if (sample1 in text || sample2 in text) {
                println(winnerText)
                if (myTurn == "*") {
                    score[0] = score[0] + 2
                } else {
                    score[1] = score[1] + 2
                }
                return true
            }
        }

        for (n in 0 until columns) {
            tempNum1 = rows - 1
            tempNum2 = n
            text = ""
            loop@while (tempNum2 <= columns - 1) {
                if (tempNum1 < 0) break@loop
                text = text + boardList[tempNum1--][tempNum2++] + " "
            }
            if (sample1 in text || sample2 in text) {
                println(winnerText)
                if (myTurn == "*") {
                    score[0] = score[0] + 2
                } else {
                    score[1] = score[1] + 2
                }
                return true
            }
        }

        for (n in 3 until rows) {
            tempNum1 = n
            tempNum2 = columns - 1
            text = ""
            loop@while (tempNum1 >= 0) {
                if (tempNum2 > 0) break@loop
                text = text + boardList[tempNum1--][tempNum2--] + " "
            }
            if (sample1 in text || sample2 in text) {
                println(winnerText)
                if (myTurn == "*") {
                    score[0] = score[0] + 2
                } else {
                    score[1] = score[1] + 2
                }
                return true
            }
        }

        for (n in (columns - 1) downTo 3) {
            tempNum1 = rows - 1
            tempNum2 = n
            text = ""
            loop@while (tempNum2 >= 0) {
                if (tempNum1 < 0) break@loop
                text = text + boardList[tempNum1--][tempNum2--] + " "
            }
            if (sample1 in text || sample2 in text) {
                println(winnerText)
                if (myTurn == "*") {
                    score[0] = score[0] + 2
                } else {
                    score[1] = score[1] + 2
                }
                return true
            }
        }
        return false
    }

    fun fieldIsFull(): Boolean = if (field == 0) {
        println("It is a draw")
        score[0] = score[0] + 1
        score[1] = score[1] + 1
        true
    } else false

    fun countGame(countGame: Int = 1): Int {
        while (true) {
            println("""Do you want to play single or multiple games?
For a single game, input 1 or press Enter
Input a number of games:""")
            var input = readln()
            if (input == "") {
                return countGame
            } else {
                try {
                    val num = input.toInt()
                    if (num > 0) {
                        return num
                    }
                    println("Invalid input")
                }
                catch (e: NumberFormatException) {
                    println("Invalid input")
                }
            }
        }
    }

    fun multipleGames() {
        var stopGame: Boolean
        if (totalGame == 1) {
            printBoard()
            stopGame = oneGame()
            if (stopGame != true) {
                printScore()
            }
        } else {
            loop@for (i in 1..totalGame) {
                println("Game #$i")
                printBoard()
                stopGame = oneGame()
                if (stopGame) break@loop
                printScore()
                boardList = MutableList(rows) {MutableList(columns) {" "} }
                field = rows * columns
                myTurn = if (i % 2 == 0) "o" else "*"
            }
        }
        println("Game over!")
    }

    fun printScore() = println("Score\n$firstPlayer: ${score[0]} $secondPlayer: ${score[1]}")

    fun oneGame(): Boolean {
        whoseNext()
        var myMove = readln()
        var myMoveInt: Int
        loop@while (myMove != "end") {
            if (correctInput(myMove)) {
                myMoveInt = myMove.toInt() - 1
                this.move(myMoveInt)
                if (this.whoWon() == true) break@loop
                if (this.fieldIsFull() == true) break@loop
                whoseNext()
                myMove = readln()
            } else {
                whoseNext()
                myMove = readln()
            }
        }
        if (myMove == "end") {
            return true
        } else {
            return false
        }
    }

}


fun main() {
    val game = Board()
    game.printInformation()
    game.multipleGames()

}