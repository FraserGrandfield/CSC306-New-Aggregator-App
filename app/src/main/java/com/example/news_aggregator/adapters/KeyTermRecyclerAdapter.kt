package com.example.news_aggregator.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.news_aggregator.R
import kotlinx.android.synthetic.main.key_term_list_item.view.*

class KeyTermRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.key_term_list_item, parent, false)
        return KeyTermViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is KeyTermViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(keyTermList: List<String>) {
        items = keyTermList
    }

    class KeyTermViewHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val keyTermTextView: TextView = itemView.key_term_text_view

        fun bind(keyTerm: String) {
            keyTermTextView.text = keyTerm
        }
    }
}