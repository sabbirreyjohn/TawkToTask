package com.example.tawkto.ui.userlistscreen

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tawkto.MainCoroutineRule
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(manifest= Config.NONE)
class UserListViewModelTest{

    private lateinit var listViewModel : UserListViewModel
    private lateinit var mRepository: UserListRepository

    @Before
    fun setUp(){
        listViewModel = UserListViewModel(ApplicationProvider.getApplicationContext())
        mRepository = mockk()

    }
}

