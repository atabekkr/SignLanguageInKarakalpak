package com.shaxsanem.signlanguage.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shaxsanem.signlanguage.data.models.Word
import com.shaxsanem.signlanguage.databinding.ItemElementBinding

class WordAdapter : ListAdapter<Word, WordAdapter.WordViewHolder>(MyDiffUtil()) {

    inner class WordViewHolder(private val binding: ItemElementBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind() {
            val item = getItem(adapterPosition)

            binding.tvTitle.text = item.name

            binding.root.setOnClickListener {
                onClick.invoke(item.content)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        return WordViewHolder(
            ItemElementBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind()
    }

    private var onClick: (content: String) -> Unit = {}
    fun setOnItemClickListener(onClick: (content: String) -> Unit) {
        this.onClick = onClick
    }

    private class MyDiffUtil : DiffUtil.ItemCallback<Word>() {
        override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem == newItem
        }
    }
}