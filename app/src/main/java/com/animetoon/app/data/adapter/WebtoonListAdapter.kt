package com.animetoon.app.data.adapter

import com.animetoon.app.data.model.Webtoon
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.animetoon.app.R

class WebtoonListAdapter(
    private var webtoonList: List<Webtoon>,
    private val onItemClick: (Webtoon) -> Unit // A lambda function to handle item clicks
) : RecyclerView.Adapter<WebtoonListAdapter.WebtoonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebtoonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false) // Your item layout
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

    fun updateData(newWebtoonList: List<Webtoon>) {
        webtoonList = newWebtoonList
        notifyDataSetChanged() // Notify the adapter about data changes
    }
    inner class WebtoonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.tv_title) // Assuming you have a TextView with this ID
        private val imageView: ImageView = itemView.findViewById(R.id.image_view) // Assuming you have an ImageView with this ID
        private val descriptionTextView: TextView = itemView.findViewById(R.id.tv_description) // Assuming you have a TextView with this ID
        fun bind(webtoon: Webtoon) {
            titleTextView.text = webtoon.title // Set the title
            imageView.load(webtoon.image_url) {
                crossfade(true)
                transformations(RoundedCornersTransformation(14f))
            }
            descriptionTextView.text = webtoon.brief_description

        }
    }
}
