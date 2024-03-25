package com.yuch.githubuserapp.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yuch.githubuserapp.data.api.Api
import com.yuch.githubuserapp.data.local.FavoriteUser
import com.yuch.githubuserapp.data.local.FavoriteUserDao
import com.yuch.githubuserapp.data.local.UserDatabase
import com.yuch.githubuserapp.data.response.DataUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : AndroidViewModel(application) {
    val user = MutableLiveData<DataUser>()
    private var userDao: FavoriteUserDao?
    private var userDb: UserDatabase?

    init {
        userDb = UserDatabase.getDatabase(application)
        userDao = userDb?.favoriteUserDao()
    }
    fun setUserDetail(username: String?) {
        if (username != null) {
            Api.apiInstance
                .getUserDetail(username)
                .enqueue(object : Callback<DataUser> {
                    override fun onResponse(
                        call: Call<DataUser>,
                        response: Response<DataUser>,
                    ) {
                        if (response.isSuccessful) {
                            user.postValue(response.body())
                        }
                    }

                    override fun onFailure(call: Call<DataUser>, t: Throwable) {
                        t.message?.let { Log.d("Failure", it) }
                    }

                })
        }
    }

    fun getUserDetail(): LiveData<DataUser> {
        return user
    }
    fun addToFavorite(username: String, avatarUrl: String, id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoriteUser(
                username,
                avatarUrl,
                id
            )
            userDao?.addToFavorite(user)
        }
    }
    suspend fun checkUser(id: Int) = userDao?.checkUser(id)

    fun deleteFavoriteUser(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.deleteFavoriteUser(id)
        }
    }
}