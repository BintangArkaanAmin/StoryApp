package com.dicoding.storyapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.storyapp.DetailStoryActivity
import com.dicoding.storyapp.DetailStoryActivity.Companion.EXTRA_DATA
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.StoryItem
import com.dicoding.storyapp.databinding.ItemStoryBinding

class StoryAdapter(var storyItem: List<StoryItem>) : RecyclerView.Adapter<StoryAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryAdapter.ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryAdapter.ViewHolder, position: Int) {
        with(holder){
            val data = storyItem.get(position)
            binding.tvItemName.text = data.name
            binding.tvItemDesc.text = data.description
            Glide.with(itemView.context)
                .load(data.photo)
                .fitCenter()
                .apply(
                    RequestOptions
                        .placeholderOf(R.drawable.ic_loading)
                ).into(binding.ivItemPhoto)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                intent.putExtra(EXTRA_DATA, data)
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.tvItemName, "name"),
                        Pair(binding.ivItemPhoto, "story"),
                        Pair(binding.tvItemDesc, "desc")
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    override fun getItemCount(): Int {
        return storyItem.size
    }

}