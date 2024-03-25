package com.yuch.githubuserapp.ui

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yuch.githubuserapp.R

class SectionPagerAdapter(
    private val fragmentActivity: FragmentActivity,
    private val followersCount: Int,
    private val followingCount: Int,
    data: Bundle
) : FragmentStateAdapter(fragmentActivity) {

    private var fragmentBundle: Bundle
    init {
        fragmentBundle = data
    }

    @StringRes
    private val TAB_TITLES = intArrayOf(R.string.tab_1, R.string.tab_2)

    override fun getItemCount(): Int = TAB_TITLES.size

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment = when (position) {
            0 -> {
                val followersFragment = FollowersFragment()
                followersFragment.arguments = fragmentBundle
                followersFragment
            }
            1 -> {
                val followingFragment = FollowingFragment()
                followingFragment.arguments = fragmentBundle
                followingFragment
            }
            else -> throw IllegalStateException("Unexpected position $position")
        }
        return fragment
    }

    fun getPageTitle(position: Int): CharSequence {
        return fragmentActivity.getString(TAB_TITLES[position], if (position == 0) followersCount else followingCount)
    }
}