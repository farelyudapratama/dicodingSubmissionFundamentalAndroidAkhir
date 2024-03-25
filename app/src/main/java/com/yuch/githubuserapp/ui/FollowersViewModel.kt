package com.yuch.githubuserapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yuch.githubuserapp.data.api.Api
import com.yuch.githubuserapp.data.response.DataUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel: ViewModel() {
    val listFollowers = MutableLiveData<ArrayList<DataUser>>()
    fun setListFollowers(username: String?){
        username?.let { it ->
            Api.apiInstance
                .getFollowers(it)
                .enqueue(object : Callback<ArrayList<DataUser>> {
                    override fun onResponse(
                        call: Call<ArrayList<DataUser>>,
                        response: Response<ArrayList<DataUser>>,
                    ) {
                        if (response.isSuccessful){
                            listFollowers.postValue(response.body())
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<DataUser>>, t: Throwable) {
                        Log.d("Failure", t.message ?: "Unknown failure")
                    }
                })
        }
    }
    fun getListFollowers(): LiveData<ArrayList<DataUser>> {
        return listFollowers
    }
}