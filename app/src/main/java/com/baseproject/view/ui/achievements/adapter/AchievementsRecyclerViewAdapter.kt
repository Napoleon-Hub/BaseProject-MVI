package com.baseproject.view.ui.achievements.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.baseproject.data.room.entity.AchievementsEntity
import com.baseproject.databinding.ItemAchievementBinding

class AchievementsRecyclerViewAdapter :
    ListAdapter<AchievementsEntity, AchievementsRecyclerViewAdapter.ItemViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemPersonBinding = ItemAchievementBinding.inflate(layoutInflater, parent, false)
        return ItemViewHolder(itemPersonBinding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewHolder(private val binding: ItemAchievementBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AchievementsEntity) = with(itemView) {
            if (item.achieveReceived) {
                binding.ivAchievement.setImageDrawable(ResourcesCompat.getDrawable(resources, item.drawableId, null))
                binding.viewNotReceive.visibility = View.GONE
            }
            binding.tvAchievementTitle.text = context.getString(item.achieveTitleId)
            binding.tvAchievementDescription.text = context.getString(item.achieveDescriptionId)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<AchievementsEntity>() {
        override fun areItemsTheSame(oldItem: AchievementsEntity, newItem: AchievementsEntity): Boolean =
            oldItem.drawableId == newItem.drawableId

        override fun areContentsTheSame(oldItem: AchievementsEntity, newItem: AchievementsEntity): Boolean =
            oldItem.drawableId == newItem.drawableId && oldItem.achieveTitleId == newItem.achieveTitleId
                    && oldItem.achieveDescriptionId == newItem.achieveDescriptionId
                    && oldItem.achieveReceived == newItem.achieveReceived
    }
}