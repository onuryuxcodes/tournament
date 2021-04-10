package com.test.tournament.model

data class Game(var player1: Player, var player2: Player, var winner: Player?)

fun Game.isPlayed(): Boolean {
    return this.winner != null
}