package com.yuch.githubuserapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yuch.githubuserapp.R
import com.yuch.githubuserapp.data.response.DataUser
import com.yuch.githubuserapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[ViewModel::class.java]

        if (savedInstanceState == null) {
            viewModel.setSearchUsers("farelyudapratama")
        }

        setupRecyclerView()
        setupSearchButton()

        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
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

        binding.user.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = this@MainActivity.adapter
        }

        viewModel.getSearchUsers().observe(this) { users ->
            if (users.isNotEmpty()) {
                adapter.setList(users)
                showLoading(false)
            } else {
                adapter.clearList()
                showLoading(false)
                binding.notFound.visibility = View.VISIBLE
            }
        }

    }

    private fun setupSearchButton() {
        with(binding) {
            searchBar.inflateMenu(R.menu.main_menu)
            searchBar.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId){
                    R.id.favorite_menu -> {
                        val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                        startActivity(intent)
                    }
                    R.id.setting_menu -> {
                        val intent = Intent(this@MainActivity, SettingActivity::class.java)
                        startActivity(intent)
                    }
                }
                true
            }
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchUser()
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    false
                }

        }
    }

    private fun searchUser() {
        val query = binding.searchView.text.toString()
        if (query.isNotEmpty()) {
            showLoading(true)
            viewModel.setSearchUsers(query)
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
        binding.notFound.visibility = View.GONE
    }

    private fun navigateToUserDetail(username: String, avatarUrl: String, id: Int) {
        val intent = Intent(this@MainActivity, DetailUserActivity::class.java).apply {
            putExtra(DetailUserActivity.EXTRA_USERNAME, username)
            putExtra(DetailUserActivity.EXTRA_AVATAR, avatarUrl)
            putExtra(DetailUserActivity.EXTRA_ID, id)
        }
        startActivity(intent)
    }
}
