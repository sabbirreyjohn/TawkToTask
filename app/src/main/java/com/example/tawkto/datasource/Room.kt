package com.example.tawkto.datasource

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.tawkto.model.RemoteKeys
import com.example.tawkto.model.User

@Dao
interface UserDao {
    @Query("select * from User")
    fun getUsers(): PagingSource<Int, User>

    @Query("select * from User")
    fun getUsersList(): LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<User>)

    @Query("update user set profile=:profile where userId = :id")
    suspend fun updateUserProfile(id: String, profile: User.Profile)

    @Query("update user set note=:note where userId = :id")
    suspend fun updateUserNote(id: String, note: String)

    @Query("select profile from User where userId=:id")
    fun getProfile(id: String): LiveData<User.Profile>

    @Query("DELETE FROM user")
    suspend fun clearAllUsers()
}

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRemoteKey(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remotekeys WHERE repoId = :id")
    suspend fun getRemoteKeysUserId(id: String): RemoteKeys?

    @Query("DELETE FROM remotekeys")
    suspend fun clearRemoteKeys()
}

@Database(entities = [User::class, RemoteKeys::class], version = 1)
@TypeConverters(User.ProfileTypeConverter::class)
abstract class TheDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val remoteKeysDao: RemoteKeysDao
}

private lateinit var INSTANCE: TheDatabase

fun getDatabase(context: Context): TheDatabase {
    synchronized(TheDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                TheDatabase::class.java,
                "users.db"
            ).addTypeConverter(User.ProfileTypeConverter()).build()
        }
    }
    return INSTANCE
}