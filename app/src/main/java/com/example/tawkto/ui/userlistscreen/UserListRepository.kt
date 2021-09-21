package com.example.tawkto.ui.userlistscreen

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.tawkto.datasource.*
import com.example.tawkto.model.User
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalPagingApi::class)
class UserListRepository(private val database: TheDatabase) {

    suspend fun getUsersFromServer() = UserApi.userInterface.getusers(0)

    suspend fun insertUsersToDB(users: List<User>) = database.userDao.insertUsers(users)

    fun getUsersFromDB() = database.userDao.getUsers()

    fun getPagingUsersFromServer(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<User>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { UserPagingSource() }
        ).flow
    }

    fun getPagingUsersFromDb(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<User>> {
        if (database == null) throw IllegalStateException("Database is not initialized")
        Log.i("repository", "called")
        val pagingSourceFactory = { database.userDao.getUsers() }
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = UserRemoteMediator(database)
        ).flow
    }

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = 10, enablePlaceholders = false)
    }

}