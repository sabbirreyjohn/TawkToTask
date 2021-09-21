package com.example.tawkto.datasource

import com.example.tawkto.model.User
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL =
    "https://api.github.com"

val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit =
    Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(BASE_URL)
        .build()

interface UserInterface {
    @GET("users")
    suspend fun getusers(@Query("since") lastUserId: Int): List<User>

    @GET("users/{USER_NAME}")
    suspend fun getProfile(@Path("USER_NAME") username: String): User.Profile
}

object UserApi {
    val userInterface: UserInterface by lazy {
        retrofit.create(UserInterface::class.java)
    }
}