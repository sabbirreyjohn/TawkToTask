package com.example.tawkto.ui.profilescreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tawkto.datasource.getDatabase
import com.example.tawkto.model.User
import kotlinx.coroutines.launch
import java.io.IOException

class ProfileViewModel(application: Application, user: User) : AndroidViewModel(application) {

    val repo = ProfileRepository(getDatabase(application))
    var profile = repo.getProfileFromDB(user.userId)

    init {

        loadProfile(user)
    }

     fun saveNote(user: User, note: String) {
        viewModelScope.launch {
            repo.updateNoteToDb(user.userId, note)
        }
    }

    private fun loadProfile(user: User) {
        viewModelScope.launch {
            try {
                val tempProfile = repo.getProfileFromServer(user.userName)
                repo.insertProfileToDB(user.userId, tempProfile)

            } catch (networkError: IOException) {
                networkError.printStackTrace()

            }
        }
    }
}


class ProfileViewModelFactory(private val mApplication: Application, private val user: User) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileViewModel(mApplication, user) as T
    }
}