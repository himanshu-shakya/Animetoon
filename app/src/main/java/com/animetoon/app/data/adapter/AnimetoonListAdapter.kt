package com.animetoon.app.data.adapter

import com.animetoon.app.data.model.Webtoon
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.animetoon.app.R

class AnimetoonListAdapter(
    private val webtoonList: List<Webtoon>,
    private val onItemClick: (Webtoon) -> Unit // A lambda function to handle item clicks
) : RecyclerView.Adapter<AnimetoonListAdapter.WebtoonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebtoonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_webtoon, parent, false) // Your item layout
        return WebtoonViewHolder(view)
    }

    override fun onBindViewHolder(holder: WebtoonViewHolder, position: Int) {
        val webtoon = webtoonList[position]
        holder.bind(webtoon)
        holder.itemView.setOnClickListener { onItemClick(webtoon) } // Handle item clicks
    }

    override fun getItemCount(): Int {
        return webtoonList.size
    }

    inner class WebtoonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(webtoon: Webtoon) {
        }
    }
}
