package com.example.compareprogramming.adapters


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.compareprogramming.Question
import com.example.compareprogramming.databinding.QuestionitemBinding
import java.text.SimpleDateFormat
import java.util.Date


class QuestionAdapter(context: Context) : ListAdapter<Question, QuestionAdapter.QuestionViewHolder>(
    DiffCallback
) {
    private var cont = context

    class QuestionViewHolder(private var binding: QuestionitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Question) {
            binding.title.text = item.name
            binding.QuestionDetail.text = item.handle
            binding.solvedWhen.text = getDateTime(item.time.toString())
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuestionViewHolder {
        return QuestionViewHolder(
            QuestionitemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            val queryUrl: Uri = Uri.parse(getItem(position).url)
            val intent = Intent(Intent.ACTION_VIEW, queryUrl)
            cont.startActivity(intent)
        }
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Question>() {
        override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean {
            return oldItem.url == newItem.url
        }
    }


}

@SuppressLint("SimpleDateFormat")
private fun getDateTime(s: String): String? {
    return try {
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:MM:SS")
        val netDate = Date(s.toLong() * 1000)
        sdf.format(netDate)
    } catch (e: Exception) {
        e.toString()
    }
}