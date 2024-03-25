package com.yuch.githubuserapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yuch.githubuserapp.data.response.DataUser
import com.yuch.githubuserapp.databinding.ItemUserBinding

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private val list = mutableListOf<DataUser>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setList(users: List<DataUser>) {
        list.clear()
        list.addAll(users)
        notifyDataSetChanged()
    }

    inner class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: DataUser?) {
            user ?: return
            binding.apply {
                root.setOnClickListener {
                    onItemClickCallback?.onItemClick(user)
                }
                user.avatarUrl?.let { url ->
                    Glide.with(itemView)
                        .load(url)
                        .into(imgAvatar)
                }
                username.text = user.login
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun clearList() {
        list.clear()
        notifyDataSetChanged()
    }

    interface OnItemClickCallback {
        fun onItemClick(data: DataUser)
    }
}
