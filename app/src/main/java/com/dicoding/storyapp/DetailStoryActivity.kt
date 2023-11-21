package com.dicoding.storyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.dicoding.storyapp.data.StoryItem
import com.dicoding.storyapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.title = "Detail Story App"

        val data = intent.getParcelableExtra<StoryItem>(EXTRA_DATA) as StoryItem
        binding.apply {
            tvName.text = data.name
            tvStory.text = data.description
            Glide.with(this@DetailStoryActivity)
                .load(data.photo)
                .fitCenter()
                .into(ivImg)
        }
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}