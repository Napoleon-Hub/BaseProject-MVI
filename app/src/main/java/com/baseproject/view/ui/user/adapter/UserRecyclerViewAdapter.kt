package com.baseproject.view.ui.user.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.baseproject.R
import com.baseproject.data.room.entity.BaseEntity
import com.baseproject.databinding.ItemGameStatisticsBinding

class UserRecyclerViewAdapter :
    ListAdapter<BaseEntity, UserRecyclerViewAdapter.ItemViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemPersonBinding = ItemGameStatisticsBinding.inflate(layoutInflater, parent, false)
        return ItemViewHolder(itemPersonBinding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewHolder(private val binding: ItemGameStatisticsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BaseEntity) = with(itemView) {
            binding.tvScore.text = context.getString(R.string.user_score, item.score)
            binding.tvResult.text = if (item.isWin) context.getString(R.string.user_win)
            else context.getString(R.string.user_lose)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<BaseEntity>() {
        override fun areItemsTheSame(oldItem: BaseEntity, newItem: BaseEntity): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: BaseEntity, newItem: BaseEntity): Boolean =
            oldItem.score == newItem.score && oldItem.isWin == newItem.isWin
    }
}