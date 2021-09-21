package com.example.tawkto.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.tawkto.model.RemoteKeys
import com.example.tawkto.model.User
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator(private val db: TheDatabase) : RemoteMediator<Int, User>() {


    private val DEFAULT_SINCE_ID: Int = 0

    override suspend fun load(loadType: LoadType, state: PagingState<Int, User>): MediatorResult {

        val pageKeyData = getKeyPageData(loadType, state)
        val page = when (pageKeyData) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
               pageKeyData as Int
            }
        }

        try {
            val response = UserApi.userInterface.getusers(page)
            val isEndOfList = response.isEmpty()
            db.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {

                    db.remoteKeysDao.clearRemoteKeys()
                    db.userDao.clearAllUsers()
                }
                val prevKey = if (page == DEFAULT_SINCE_ID) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response.map {
                    RemoteKeys(repoId = it.userId, prevKey = prevKey, nextKey = nextKey)
                }
                db.remoteKeysDao.insertAllRemoteKey(keys)
                db.userDao.insertUsers(response)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }


    }

    suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, User>): Any? {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_SINCE_ID
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                    ?: return MediatorResult.Success(endOfPaginationReached = false)
                remoteKeys.nextKey
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                    ?: return MediatorResult.Success(endOfPaginationReached = false)
                //end of list condition reached
                remoteKeys.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                remoteKeys.prevKey

            }
        }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, User>): RemoteKeys? {
        return state.pages
            .firstOrNull() { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { user -> db.remoteKeysDao.getRemoteKeysUserId(user.userId) }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, User>): RemoteKeys? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { user -> db.remoteKeysDao.getRemoteKeysUserId(user.userId) }
    }

    private suspend fun getClosestRemoteKey(state: PagingState<Int, User>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.userId?.let { repoId ->
                db.remoteKeysDao.getRemoteKeysUserId(repoId)
            }
        }
    }
}