package com.test.tournament.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.tournament.model.Tournament

class TournamentViewModel : ViewModel() {
    val tournament = MutableLiveData<Tournament>()

}