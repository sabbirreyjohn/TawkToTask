package com.example.tawkto.ui.profilescreen

import com.example.tawkto.datasource.TheDatabase
import com.example.tawkto.datasource.UserApi
import com.example.tawkto.model.User

class ProfileRepository(private val database: TheDatabase) {

    suspend fun getProfileFromServer(username: String) = UserApi.userInterface.getProfile(username)

    suspend fun insertProfileToDB(id: String, profile: User.Profile) =
        database.userDao.updateUserProfile(id, profile)

    suspend fun updateNoteToDb(id: String, note: String) = database.userDao.updateUserNote(id, note)

    fun getProfileFromDB(id: String) = database.userDao.getProfile(id)
}