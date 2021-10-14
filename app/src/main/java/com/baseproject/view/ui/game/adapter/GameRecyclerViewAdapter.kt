package com.baseproject.view.ui.game.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.baseproject.R
import com.baseproject.databinding.ItemGameWordBinding
import com.baseproject.utils.extentions.*

class GameRecyclerViewAdapter :
    ListAdapter<String, GameRecyclerViewAdapter.ItemViewHolder>(DiffUtilCallback) {

    var listener: ((word: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemPersonBinding = ItemGameWordBinding.inflate(layoutInflater, parent, false)
        return ItemViewHolder(itemPersonBinding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    class ItemViewHolder(private val binding: ItemGameWordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(wordString: String, listener: ((word: String) -> Unit)?) {
            binding.tvWord.apply {
                text = wordString
                setOnClickListener {
                    text = ""
                    background = ResourcesCompat.getDrawable(resources, R.drawable.bg_game_item_selected, null)
                    disable()
                    listener?.invoke(wordString)
                }
                background = ResourcesCompat.getDrawable(resources, R.drawable.bg_gray_rounded, null)
                enable()
            }
        }
    }

    object DiffUtilCallback : DiffUtil.ItemCallback<String>() {
        override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
        override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    }

}