package com.yuch.githubuserapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yuch.githubuserapp.data.local.FavoriteUser
import com.yuch.githubuserapp.data.response.DataUser
import com.yuch.githubuserapp.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: UserAdapter
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]

        setupRecyclerView()

        viewModel.getFavoriteUser()?.observe(this) {
            if (it != null) {
                val list = mapList(it)
                adapter.setList(list)
            }
        }
    }

    private fun mapList(users: List<FavoriteUser>): ArrayList<DataUser> {
        val listUsers = ArrayList<DataUser>()

        for (user in users){
            val userMapped = DataUser(
                user.login,
                user.id,
                user.avatar_url
            )
            listUsers.add(userMapped)
        }
        return listUsers
    }

    private fun setupRecyclerView() {
        adapter = UserAdapter().apply {
            setItemClickCallback(object : UserAdapter.OnItemClickCallback{
                override fun onItemClick(data: DataUser) {
                    data.login?.let { data.id?.let { it1 -> data.avatarUrl?.let { it2 ->
                        navigateToUserDetail(it,
                            it2,it1)
                    } } }
                }
            })
        }
        binding.apply {
            favoriteUser.setHasFixedSize(true)
            favoriteUser.layoutManager =LinearLayoutManager(this@FavoriteActivity)
            favoriteUser.adapter = this@FavoriteActivity.adapter
        }
    }
    private fun navigateToUserDetail(username: String, avatarUrl: String, id: Int) {
        val intent = Intent(this@FavoriteActivity, DetailUserActivity::class.java).apply {
            putExtra(DetailUserActivity.EXTRA_USERNAME, username)
            putExtra(DetailUserActivity.EXTRA_AVATAR, avatarUrl)
            putExtra(DetailUserActivity.EXTRA_ID, id)
        }
        startActivity(intent)
    }
}