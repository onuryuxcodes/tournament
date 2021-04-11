package com.test.tournament.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.test.tournament.R
import com.test.tournament.databinding.ActivityMainBinding
import com.test.tournament.databinding.GameContainerViewBinding
import com.test.tournament.databinding.GameViewBinding
import com.test.tournament.databinding.PlayerNameEditViewBinding
import com.test.tournament.model.Player
import com.test.tournament.util.CommonFunctions.Companion.isPowerOfTwo
import com.test.tournament.viewmodel.TournamentViewModel
import com.test.tournament.viewmodel.VMFactory

class TournamentActivity : AppCompatActivity() {

    private val viewModel: TournamentViewModel by lazy {
        ViewModelProvider(this, VMFactory()).get(TournamentViewModel::class.java)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var inflater: LayoutInflater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        //Observe the changes to current round to update the ui
        viewModel.currentRound.observe(this, Observer {
            when (it) {
                0 -> {
                    binding.roundContainer.removeAllViews()
                    binding.configureLayout.visibility = View.VISIBLE
                }
                else -> {
                    val gameContainerView = GameContainerViewBinding.inflate(inflater, null, false)
                    gameContainerView.roundName.text = getString(R.string.round) + it.toString()
                    viewModel.tournament.value?.games?.forEach { game ->
                        when (game.round) {
                            viewModel.currentRound.value -> {
                                val gameView = GameViewBinding.inflate(inflater, null, false)
                                gameView.player1.text = game.player1.name
                                gameView.player2.text = game.player2.name
                                gameContainerView.container.addView(gameView.root)
                            }
                        }
                    }
                    when (viewModel.winner.value) {
                        null -> {
                            binding.roundContainer.addView(gameContainerView.root)
                        }
                    }
                }
            }
        })
        binding.playRoundButton.setOnClickListener {
            viewModel.playTheGamesOfTheRound()
        }
        addEditTextBasedOnPlayerCountInput()
        binding.configureButton.setOnClickListener {
            var playerList = arrayListOf<Player>()
            binding.playerNameEditContainer.children.forEachIndexed { index, child ->
                when (child) {
                    is EditText -> {
                        var name = child.text.toString()
                        when {
                            name.isNullOrEmpty() -> {
                                name = getString(R.string.player) + (index + 1).toString()
                            }
                        }
                        playerList.add(Player(name))
                    }
                }
            }
            viewModel.createTournamentRound(playerList)
            binding.configureLayout.visibility = View.GONE
        }
        viewModel.winner.observe(this, Observer {
            binding.playRoundButton.isEnabled = false
            val gameView = GameViewBinding.inflate(inflater, null, false)
            gameView.player1.text = getString(R.string.winner) + it.name
            binding.roundContainer.addView(gameView.root)
        })
    }

    private fun addEditTextBasedOnPlayerCountInput() {
        binding.playerCountEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.playerNameEditContainer.removeAllViews()
                try {
                    val playerCount = s.toString().toInt()
                    when {
                        playerCount.isPowerOfTwo() -> {
                            binding.configureButton.isEnabled = true
                            for (i in 1..playerCount) {
                                val playerNameView =
                                    PlayerNameEditViewBinding.inflate(inflater, null, false)
                                binding.playerNameEditContainer.addView(playerNameView.root)
                            }
                        }
                        else -> {
                            binding.configureButton.isEnabled = false
                            Toast.makeText(
                                this@TournamentActivity,
                                getString(R.string.integer_error),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } catch (ex: Exception) {
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

}