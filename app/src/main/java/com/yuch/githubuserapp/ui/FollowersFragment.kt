package com.yuch.githubuserapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yuch.githubuserapp.R
import com.yuch.githubuserapp.data.response.DataUser
import com.yuch.githubuserapp.databinding.FragmentLayoutFollowBinding
import com.yuch.githubuserapp.ui.DetailUserActivity.Companion.EXTRA_USERNAME

class FollowersFragment: Fragment(R.layout.fragment_layout_follow) {
    private var _binding : FragmentLayoutFollowBinding?= null
    private lateinit var viewModel: FollowersViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var username: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        username = args?.getString(EXTRA_USERNAME).toString()
        _binding = FragmentLayoutFollowBinding.bind(view)
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        _binding?.apply {
            user.setHasFixedSize(true)
            user.layoutManager= LinearLayoutManager(activity)
            user.adapter = adapter
        }

        if (savedInstanceState == null || !savedInstanceState.containsKey(EXTRA_USERNAME)) {
            showLoading(true)
            viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[FollowersViewModel::class.java]
            viewModel.setListFollowers(username)
        } else {
            username = savedInstanceState.getString(EXTRA_USERNAME).toString()
            viewModel = ViewModelProvider(this)[FollowersViewModel::class.java]
        }

        viewModel.getListFollowers().observe(viewLifecycleOwner) { followers ->
            if (followers.isNullOrEmpty()) {
                adapter.clearList()
                showLoading(false)
                _binding?.notFound?.visibility = View.VISIBLE
                _binding?.notFound?.text = getString(R.string.user_not_follow, "following")
            } else {
                adapter.setList(followers)
                showLoading(false)
                _binding?.notFound?.visibility = View.GONE
            }
        }

        adapter.setItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClick(data: DataUser) {
                val intent = Intent(requireContext(), DetailUserActivity::class.java)
                intent.putExtra(EXTRA_USERNAME, data.login)
                intent.putExtra(DetailUserActivity.EXTRA_AVATAR, data.avatarUrl)
                intent.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                startActivity(intent)
            }
        })

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_USERNAME, username)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(state: Boolean){
        if(state) {
            _binding?.progressBar?.visibility = View.VISIBLE
            _binding?.user?.visibility = View.GONE
        } else {
            _binding?.progressBar?.visibility = View.GONE
            _binding?.user?.visibility = View.VISIBLE
        }
    }

}
