package com.test.tournament.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.tournament.model.Game
import com.test.tournament.model.Player
import com.test.tournament.model.Tournament
import kotlin.random.Random

/**
 * TournamentViewModel contains the logic and the data operations
 * of the Tournament
 */

class TournamentViewModel : ViewModel() {
    val tournament = MutableLiveData<Tournament>()
    val currentRound = MutableLiveData(1)

    fun createTournamentRound(playerList: List<Player>) {
        var games = arrayListOf<Game>()
        var playerIndexToOperate = 0
        loop@ for (i in playerList.indices) {
            when {
                playerIndexToOperate != i -> continue@loop
            }
            games.add(Game(player1 = playerList[i], player2 = playerList[i + 1], winner = null, round = currentRound.value))
            playerIndexToOperate += 2
        }
        tournament.value?.games?.addAll(games)
    }

    fun selectPlayerAsFutureWinner(player: Player) {
        tournament.value?.games?.forEachIndexed { index, game ->
            when {
                game.player1 == player || game.player2 == player -> {
                    game.winner = player
                    updateGameInTournament(index, game)
                }
            }
        }
    }

    fun deselectPlayerAsFutureWinner(player: Player) {
        tournament.value?.games?.forEachIndexed { index, game ->
            when (game.winner) {
                player -> {
                    game.winner = null
                    updateGameInTournament(index, game)
                }
            }
        }
    }

    fun playTheGamesOfTheRound() {
        var winnerList = arrayListOf<Player>()
        tournament.value?.games?.filter { it.round == currentRound.value }?.forEachIndexed { index, game ->
            when (game.winner) {
                null -> {
                    when (val player1wins = Random.nextBoolean()) {
                        player1wins -> game.winner = game.player1
                        else -> game.winner = game.player2
                    }
                    game.winner?.let {
                        winnerList.add(it)
                    }
                    updateGameInTournament(index, game)
                }
                else -> {
                    game.winner?.let {
                        winnerList.add(it)
                    }
                }
            }

        }
        createTournamentRound(winnerList)
        currentRound.value = +1
    }

    private fun updateGameInTournament(index: Int, game: Game) {
        tournament.value?.games?.let {
            it[index] = game
        }
    }
}