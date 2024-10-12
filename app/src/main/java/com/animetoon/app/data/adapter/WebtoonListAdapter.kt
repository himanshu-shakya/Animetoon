package com.animetoon.app.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.animetoon.app.R
import com.animetoon.app.data.model.Webtoon

class WebtoonListAdapter(
    private var webtoonList: List<Webtoon>,
    private val onItemClick: (Webtoon) -> Unit,
    private val isFavoriteScreen: Boolean=false,
    private val onRemoveFavoriteClick: (Webtoon) -> Unit = {}
) : RecyclerView.Adapter<WebtoonListAdapter.WebtoonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebtoonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return WebtoonViewHolder(view)
    }

    override fun onBindViewHolder(holder: WebtoonViewHolder, position: Int) {
        val webtoon = webtoonList[position]
        holder.bind(webtoon)
        holder.itemView.setOnClickListener { onItemClick(webtoon) }


        if (isFavoriteScreen) {
            holder.removeFavoriteButton.visibility = View.VISIBLE
            holder.removeFavoriteButton.setOnClickListener { onRemoveFavoriteClick(webtoon) }
        } else {
            holder.removeFavoriteButton.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return webtoonList.size
    }

    fun updateData(newWebtoonList: List<Webtoon>) {
        webtoonList = newWebtoonList
        notifyDataSetChanged()
    }

    inner class WebtoonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView =
            itemView.findViewById(R.id.tv_title)
        private val imageView: ImageView =
            itemView.findViewById(R.id.image_view)
        private val descriptionTextView: TextView =
            itemView.findViewById(R.id.tv_description)
        val removeFavoriteButton: ImageButton =
            itemView.findViewById(R.id.remove_favorite_button)

        fun bind(webtoon: Webtoon) {
            titleTextView.text = webtoon.title
            imageView.load(webtoon.imageUrl) {
                crossfade(true)
                transformations(RoundedCornersTransformation(14f))
            }
            descriptionTextView.text = webtoon.briefDescription
        }
    }
}
