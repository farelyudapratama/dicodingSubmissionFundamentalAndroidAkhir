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

class FollowingViewModel: ViewModel() {
    val listFollowing = MutableLiveData<ArrayList<DataUser>>()
    fun setListFollowing(username: String?){
        username?.let { it ->
            Api.apiInstance
                .getFollowing(it)
                .enqueue(object : Callback<ArrayList<DataUser>> {
                    override fun onResponse(
                        call: Call<ArrayList<DataUser>>,
                        response: Response<ArrayList<DataUser>>,
                    ) {
                        if (response.isSuccessful){
                            listFollowing.postValue(response.body())
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<DataUser>>, t: Throwable) {
                        t.message?.let { Log.d("Failure", it) }
                    }

                })
        }
    }
    fun getListFollowing(): LiveData<ArrayList<DataUser>> {
        return listFollowing
    }
}