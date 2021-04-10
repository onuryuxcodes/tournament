package com.test.tournament.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.test.tournament.R
import com.test.tournament.viewmodel.TournamentViewModel
import com.test.tournament.viewmodel.VMFactory

class MainActivity : AppCompatActivity() {

    private val viewModel: TournamentViewModel by lazy {
        ViewModelProvider(this, VMFactory()).get(TournamentViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.tournament.observe(this, Observer {

        })

    }
}