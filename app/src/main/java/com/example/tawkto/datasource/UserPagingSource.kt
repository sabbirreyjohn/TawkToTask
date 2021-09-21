package com.example.tawkto.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tawkto.model.User
import java.lang.Exception

class UserPagingSource : PagingSource<Int, User>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        return try{
            val sinceUserId: Int = params.key ?: LAST_USER_ID
            val response = UserApi.userInterface.getusers(sinceUserId)
            LoadResult.Page( response, null, response.last().userId.toInt())
        }catch (e: Exception){
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val LAST_USER_ID = 0
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition
    }
}