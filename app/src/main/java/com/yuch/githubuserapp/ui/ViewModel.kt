package com.yuch.githubuserapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yuch.githubuserapp.data.api.Api
import com.yuch.githubuserapp.data.response.DataUser
import com.yuch.githubuserapp.data.response.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class ViewModel : ViewModel() {
    val listUsers = MutableLiveData<ArrayList<DataUser>>()

    fun setSearchUsers(query:String){
        Api.apiInstance
            .getSearchUsers(query)
            .enqueue(object : Callback<UserResponse>{
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>,
                ) {
                    if (response.isSuccessful){
                        listUsers.postValue(response.body()?.items)
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    t.message?.let { Log.d("Failure", it) }
                }

            })
    }
    fun getSearchUsers():LiveData<ArrayList<DataUser>>{
        return listUsers
    }
}