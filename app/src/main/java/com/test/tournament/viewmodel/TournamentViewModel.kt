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
    val currentRound = MutableLiveData(0)
    val winner = MutableLiveData<Player>()


    fun createTournamentRound(playerList: List<Player>) {
        when (playerList.size) {
            1 -> {
                winner.value = playerList[0]
                return
            }
        }
        val nextRound = (currentRound.value ?: 0) + 1
        var games = arrayListOf<Game>()
        var playerIndexToOperate = 0
        loop@ for (i in playerList.indices) {
            when {
                playerIndexToOperate != i -> continue@loop
            }
            games.add(
                Game(
                    player1 = playerList[i],
                    player2 = playerList[i + 1],
                    winner = null,
                    round = nextRound
                )
            )
            playerIndexToOperate += 2
        }
        when (tournament.value) {
            null -> {
                tournament.value = Tournament(games)
            }
            else -> {
                tournament.value?.games?.addAll(games)
            }
        }
        when (currentRound.value) {
            0 -> {
                currentRound.value = 1
            }
        }
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
        tournament.value?.games?.filter { it.round == currentRound.value }
            ?.forEachIndexed { index, game ->
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
        currentRound.value = currentRound.value?.plus(1)
    }

    private fun updateGameInTournament(index: Int, game: Game) {
        tournament.value?.games?.let {
            it[index] = game
        }
    }
}