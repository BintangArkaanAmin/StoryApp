package com.dicoding.storyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.adapter.StoryAdapter
import com.dicoding.storyapp.databinding.ActivityMainBinding
import com.dicoding.storyapp.viewmodel.MainViewModel
import com.dicoding.storyapp.viewmodel.factory.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ViewModelFactory
    private val mainViewModel: MainViewModel by viewModels { viewModel }
    private lateinit var loading: AlertDialog
    private var addClicked = false
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelFactory.getInstance(this)

        createLoading()
        loadData()
        listStory()

        binding.btnAdd.setOnClickListener {
            addClicked = true
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }

    private fun loadData() {
        mainViewModel.getSession().observe(this) {
            if (!it.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                mainViewModel.getStory()
            }
        }
    }

    private fun listStory(){
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.message.observe(this) {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        mainViewModel.storyResponse.observe(this) {
            storyAdapter = StoryAdapter(it)
            binding.rvStory.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = storyAdapter
            }
        }
    }

    private fun createLoading() {
        loading = AlertDialog.Builder(this)
            .setCancelable(false)
            .setView(R.layout.loading)
            .create()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) loading.show() else loading.dismiss()
    }

    override fun onRestart() {
        super.onRestart()
        if (addClicked) {
            loadData()
            addClicked = false
            binding.rvStory.scrollToPosition(0)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_logout -> {
                mainViewModel.logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}