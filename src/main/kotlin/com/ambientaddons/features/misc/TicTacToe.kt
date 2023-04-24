package com.ambientaddons.features.misc

import AmbientAddons.Companion.mc
import com.ambientaddons.events.MessageSentEvent
import com.ambientaddons.utils.Extensions.stripControlCodes
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.*

object TicTacToe {

    private var board = arrayOf(arrayOf("1", "2", "3"), arrayOf("4", "5", "6"), arrayOf("7", "8", "9"))
    private var currentPlayer = "X"
    private var currentName = ""
    private var opponent = ""
    private var gameActive = false
    private val username = mc.session.username
    private val startGame = Regex("^/pc tictactoe (?<opponent>[\\w]+)$")
    private val myMove = Regex("^/pc (?<position>[1-9])$")
    private val opponentMove = Regex("^Party > (?:\\[[A-Z]{3}\\+?\\+?] )?(?<opponent>[\\w]+): (?<position>[1-9])$")

    @SubscribeEvent
    fun onSendChat(event: MessageSentEvent) {

        val startMatch = event.message.let { startGame.find(it) }

        if (startMatch != null)
        {
            opponent = (startMatch.groups as? MatchNamedGroupCollection)?.get("opponent")?.value ?: return
            board = arrayOf(arrayOf("1", "2", "3"), arrayOf("4", "5", "6"), arrayOf("7", "8", "9"))
            currentPlayer = "X"
            currentName = opponent
            mc.thePlayer.sendChatMessage("/pc It's $currentName's turn. Please enter a position (1-9):")
            printBoard(board)
            gameActive = true
            return
        }

        val moveMatch = event.message.let { myMove.find(it) }

        if (moveMatch != null && gameActive && currentPlayer == "O")
        {
            val position = (moveMatch.groups as? MatchNamedGroupCollection)?.get("position")?.value ?: return
            TicTacToe(position.toInt())
            mc.thePlayer.sendChatMessage(event.message)
            return
        }
    }

    @SubscribeEvent
    fun onChatReceived(event: ClientChatReceivedEvent) {
        if (gameActive && currentPlayer == "X") {
            val opponentMatch = event.message.unformattedText.stripControlCodes().let { opponentMove.find(it) }
            if (opponentMatch != null) {
                val opponentTemp =
                    (opponentMatch.groups as? MatchNamedGroupCollection)?.get("opponent")?.value ?: return
                if (opponentTemp == opponent) {
                    val position =
                        (opponentMatch.groups as? MatchNamedGroupCollection)?.get("position")?.value ?: return
                    TicTacToe(position.toInt())
                    return
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

        // Check if the game is over
        if (checkWin(board, currentPlayer)) {
            mc.thePlayer.sendChatMessage("/pc Congratulations, $currentName! You win!")
            return
        } else if (checkTie(board)) {
            mc.thePlayer.sendChatMessage("/pc It's a tie!")
            return
        }

        // Print the current state of the board
        printBoard(board)

        // Switch to the other player
        currentPlayer = if (currentPlayer == "X") "O" else "X"
        currentName = if (currentPlayer == "X") opponent else username

        // Ask the current player to make a move
        mc.thePlayer.sendChatMessage("/pc It's $currentName's turn. Please enter a position (1-9):")
    }

    private fun printBoard(board: Array<Array<String>>) {
        val interval:Long = 250
        Timer().schedule(object : TimerTask() {
            override fun run() {
                mc.thePlayer.sendChatMessage("/pc ${board[0][0]}│ ${board[0][1]}│ ${board[0][2]}")
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        mc.thePlayer.sendChatMessage("/pc -┤ -┤ -")
                        Timer().schedule(object : TimerTask() {
                            override fun run() {
                                mc.thePlayer.sendChatMessage("/pc ${board[1][0]}│ ${board[1][1]} | ${board[1][2]}")
                                Timer().schedule(object : TimerTask() {
                                    override fun run() {
                                        mc.thePlayer.sendChatMessage("/pc - | - | -")
                                        Timer().schedule(object : TimerTask() {
                                            override fun run() {
                                                mc.thePlayer.sendChatMessage("/pc ${board[2][0]} | ${board[2][1]} | ${board[2][2]}")
                                            }
                                        }, interval)
                                    }
                                }, interval)
                            }
                        }, interval)
                    }
                }, interval)
            }
        }, interval)
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