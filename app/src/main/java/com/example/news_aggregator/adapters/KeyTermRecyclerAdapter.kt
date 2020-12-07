package com.example.news_aggregator.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.news_aggregator.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.key_term_list_item.view.*

class KeyTermRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: List<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.key_term_list_item, parent, false)

        return KeyTermViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
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

    class KeyTermViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val keyTermTextView: TextView = itemView.key_term_text_view
        private val deleteButton = itemView.delete_button
        var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        private val database: FirebaseFirestore = FirebaseFirestore.getInstance()
        private var ref = database.collection(itemView.context.getString(R.string.firestore_users))
            .document(mAuth.uid.toString())

        fun bind(keyTerm: String) {
            keyTermTextView.text = keyTerm
            deleteButton.setOnClickListener {
                ref.update(
                    itemView.context.getString(R.string.firestore_key_terms),
                    FieldValue.arrayRemove(keyTerm)
                )
            }
        }
    }
}