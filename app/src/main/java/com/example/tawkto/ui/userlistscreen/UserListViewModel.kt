package com.example.tawkto.ui.userlistscreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.tawkto.datasource.getDatabase
import com.example.tawkto.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.IOException

enum class ApiStatus { LOADING, DONE, ERROR }
@OptIn(ExperimentalPagingApi::class)
class UserListViewModel(application: Application) : AndroidViewModel(application) {

   val repo = UserListRepository(getDatabase(application))


    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus> get() = _status

    init {
       loadData()
    }


    fun loadUsersFromDB(): Flow<PagingData<User>> {
        return repo.getPagingUsersFromDb().cachedIn(viewModelScope)
    }

    fun loadData() {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                loadUsersFromDB()
                _status.value = ApiStatus.DONE
            } catch (networkError: IOException) {
                networkError.printStackTrace()
                _status.value = ApiStatus.ERROR
            }
        }
    }

}