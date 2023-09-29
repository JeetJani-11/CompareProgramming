package com.example.compareprogramming.adapters


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.compareprogramming.Contest
import com.example.compareprogramming.databinding.ContestItemBinding
import java.text.SimpleDateFormat
import java.util.Date


class ContestsAdapter : ListAdapter<Contest, ContestsAdapter.ContestViewHolder>(DiffCallback) {
    class ContestViewHolder(private var binding: ContestItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Contest) {
            binding.title.text = item.name
            binding.QuestionDetail.text = item.date
            binding.solvedWhen.text = getDateTime(item.time.toString())
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContestViewHolder {
        return ContestViewHolder(
            ContestItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ContestViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Contest>() {
        override fun areItemsTheSame(oldItem: Contest, newItem: Contest): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Contest, newItem: Contest): Boolean {
            return oldItem.name == newItem.name
        }
    }
}

@SuppressLint("SimpleDateFormat")
private fun getDateTime(s: String): String? {
    return try {
        val sdf = SimpleDateFormat("HH:MM:SS")
        val netDate = Date(s.toLong() * 1000)
        sdf.format(netDate)
    } catch (e: Exception) {
        e.toString()
    }
}