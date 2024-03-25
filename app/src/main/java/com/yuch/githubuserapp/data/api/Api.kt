package com.yuch.githubuserapp.data.api

import com.yuch.githubuserapp.BuildConfig
import com.yuch.githubuserapp.BuildConfig.*
import com.yuch.githubuserapp.data.response.DataUser
import com.yuch.githubuserapp.data.response.UserResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    companion object {
        private const val BASE_URL = BuildConfig.BASE_URL
        private const val API_KEY = BuildConfig.API_KEY

        private val httpClient = OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader("Authorization", API_KEY)
                .build()
            chain.proceed(request)
        }.build()

        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiInstance: Api = retrofit.create(Api::class.java)
    }

    @GET("search/users")
    fun getSearchUsers(
        @Query("q") query: String
    ):Call<UserResponse>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DataUser>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<ArrayList<DataUser>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<DataUser>>
}
