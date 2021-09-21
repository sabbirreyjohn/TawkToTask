package com.example.tawkto.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tawkto.datasource.TheDatabase
import com.example.tawkto.datasource.UserPagingSource
import com.example.tawkto.model.User
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class DaoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: TheDatabase
    private lateinit var users: List<User>

    @Before
    fun initDb() {
        // Using an in-memory database so that the information stored here disappears when the
        // process is killed.

        users = listOf(
            User("1", "Title 1", "Node 1", "http://thumbnail", null, null),
            User("2", "Title 2", "Node 2", "http://thumbnail", null, null),
            User("3", "Title 3", "Node 3", "http://thumbnail", null, null)
        )



        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            TheDatabase::class.java
        ).allowMainThreadQueries().addTypeConverter(User.ProfileTypeConverter()).build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun `insert users succeessfully`() {

        runBlockingTest {
            database.userDao.insertUsers(users)
            val tempUsers = database.userDao.getUsersList()
            assertThat(tempUsers.getOrAwaitValue().size).isEqualTo(3)
        }
    }

    @Test
    fun `replace users if already exists`() {
        runBlockingTest {
            database.userDao.insertUsers(users)
            database.userDao.insertUsers(users)

            val tempUsers = database.userDao.getUsersList().getOrAwaitValue()
            assertThat(tempUsers.size).isEqualTo(3)
        }
    }

    @Test
    fun `insert profile to user`() {
        runBlockingTest {
            database.userDao.insertUsers(users)
            val profile = User.Profile(
                "", "", "", "",
                "", "", "", 0, "",
                1, "", "", "", true,
                "", 1, "", "", "Sabbir", "",
                "", 1, 1, "", "",
                false, "", "", "", "",
                "", ""
            )
            database.userDao.updateUserProfile("1", profile)
            val tempUsers = database.userDao.getUsersList().getOrAwaitValue()

            assertThat(tempUsers.get(0).profile?.name).isEqualTo("Sabbir")
        }
    }
}