package com.ambientaddons.features.misc

import AmbientAddons.Companion.mc
import AmbientAddons.Companion.config
import com.ambientaddons.utils.Extensions.stripControlCodes
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.*

object TicTacToe {

    private var board = arrayOf(arrayOf("1", "2", "3"), arrayOf("4", "5", "6"), arrayOf("7", "8", "9"))
    private var currentPlayer = ""
    private var currentName = ""
    private var opponent = ""
    private var gameActive = false
    private var printActive = false
    private var lineNumber = 0
    private val username = mc.session.username
    private val startGame = Regex("^Party > (?:\\[[A-Z]{3}\\+?\\+?] )?(?<username>[\\w]+): tictactoe (?<opponent>[\\w]+)$")
    private val playerMove = Regex("^Party > (?:\\[[A-Z]{3}\\+?\\+?] )?(?<player>[\\w]+): (?<position>[1-9])$")
    private val printedLine = Regex("^Party > (?:\\[[A-Z]{3}\\+?\\+?] )?(?<player>[\\w]+): ")


    @SubscribeEvent
    fun onChatReceived(event: ClientChatReceivedEvent) {

        val startMatch = event.message.unformattedText.stripControlCodes().let { startGame.find(it) }
        val isStartValid = (startMatch?.groups as? MatchNamedGroupCollection)?.get("username")?.value == username
        if (startMatch != null && !printActive && isStartValid)
        {
            opponent = (startMatch.groups as? MatchNamedGroupCollection)?.get("opponent")?.value ?: return
            board = arrayOf(arrayOf("1", "2", "3"), arrayOf("4", "5", "6"), arrayOf("7", "8", "9"))
            currentName = if (currentName == opponent) username else opponent
            currentPlayer = if (currentName == opponent) "X" else "O"
            gameActive = true
            printActive = true
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    mc.thePlayer.sendChatMessage("/pc It's $currentName's turn. Please enter a position (1-9) to place an $currentPlayer:")
                }
            }, config.tictactoeDelay.toLong())
            return
        }
        else if (gameActive && !printActive) {
            val playerMatch = event.message.unformattedText.stripControlCodes().let { playerMove.find(it) }
            if (playerMatch != null) {
                val playerTemp =
                    (playerMatch.groups as? MatchNamedGroupCollection)?.get("player")?.value ?: return
                if (playerTemp == currentName) {
                    val position =
                        (playerMatch.groups as? MatchNamedGroupCollection)?.get("position")?.value ?: return
                    Timer().schedule(object : TimerTask() {
                        override fun run() {
                            TicTacToe(position.toInt())
                        }
                    }, config.tictactoeDelay.toLong())
                    return
                }
            }
        }

        var printLines = listOf(
            "/pc ${board[0][0]}│ ${board[0][1]}│ ${board[0][2]}",
            "/pc -┤ -┤ -",
            "/pc ${board[1][0]}│ ${board[1][1]} | ${board[1][2]}",
            "/pc - | - | -",
            "/pc ${board[2][0]} | ${board[2][1]} | ${board[2][2]}"
        )

        if (printActive) {
            val printMatch = event.message.unformattedText.stripControlCodes().let { printedLine.find(it) }
            if (printMatch != null) {
                val printedStart =
                    (printMatch.groups as? MatchNamedGroupCollection)?.get("player")?.value ?: return
                if (printedStart == username) {
                    Timer().schedule(object : TimerTask() {
                        override fun run() {
                            mc.thePlayer.sendChatMessage(printLines[lineNumber])
                            if (lineNumber == 4) {
                                lineNumber = 0
                                printActive = false
                                return
                            }
                            lineNumber++
                            return
                        }
                    }, config.tictactoeDelay.toLong())
                }
            }
        }
    }

    private fun TicTacToe(position: Int) {

        val row = (position - 1).floorDiv(3)
        val col = (position - 1) % 3


        // Check if the move is valid
        if (position < 1 || position > 9 || board[row][col] == "X" || board[row][col] == "O") {
            mc.thePlayer.sendChatMessage("/pc Invalid move. Please try again.")
            return
        }

        // Update the board with the player's move
        board[row][col] = currentPlayer

        printActive = true

        // Check if the game is over
        if (checkWin(board, currentPlayer)) {
            gameActive = false
            mc.thePlayer.sendChatMessage("/pc Congratulations, $currentName! You win!")
            return
        } else if (checkTie(board)) {
            gameActive = false
            mc.thePlayer.sendChatMessage("/pc It's a tie!")
            return
        } else {
            // Switch to the other player
            currentName = if (currentPlayer == "X") username else opponent
            currentPlayer = if (currentPlayer == "X") "O" else "X"
            mc.thePlayer.sendChatMessage("/pc It's $currentName's turn. Please enter a position (1-9) to place an $currentPlayer:")
            return
        }
    }

    private fun checkWin(board: Array<Array<String>>, player: String): Boolean {
        // Check rows
        for (i in 0..2) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true
            }
        }

        // Check columns
        for (j in 0..2) {
            if (board[0][j] == player && board[1][j] == player && board[2][j] == player) {
                return true
            }
        }

        // Check diagonals
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true
        }
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true
        }

        return false
    }

    private fun checkTie(board: Array<Array<String>>): Boolean {
        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i][j].all {char -> char.isDigit()} ) {
                    return false
                }
            }
        }
        return true
    }
}