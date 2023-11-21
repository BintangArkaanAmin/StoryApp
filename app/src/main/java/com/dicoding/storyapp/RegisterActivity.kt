package com.dicoding.storyapp

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.dicoding.storyapp.databinding.ActivityRegisterBinding
import com.dicoding.storyapp.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {
    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel>()
    private lateinit var loading: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAnimation()
        createLoading()
        register()

        binding.btnRegister.setOnClickListener {
            if (binding.edRegisterName.length() == 0 || binding.edRegisterEmail.length() == 0 && binding.edRegisterPassword.length() < 8) {
                binding.edRegisterName.error = getString(R.string.error_field)
                binding.edRegisterEmail.error = getString(R.string.error_field)
                binding.edRegisterPassword.error = getString(R.string.error_field)
            } else {
                registerViewModel.register(
                    binding.edRegisterName.text.toString(),
                    binding.edRegisterEmail.text.toString(),
                    binding.edRegisterPassword.text.toString()
                )
            }
        }

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }

    private fun register() {
        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        registerViewModel.message.observe(this) {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        registerViewModel.registerResponse.observe(this) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun setAnimation() {
        val title = ObjectAnimator.ofFloat(binding.txtTitle, View.ALPHA, 1f).setDuration(400)
        val username = ObjectAnimator.ofFloat(binding.tilNama, View.ALPHA, 1f).setDuration(400)
        val email = ObjectAnimator.ofFloat(binding.tilEmail, View.ALPHA, 1f).setDuration(400)
        val password = ObjectAnimator.ofFloat(binding.tilPassword, View.ALPHA, 1f).setDuration(400)
        val button = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(400)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(400)

        AnimatorSet().apply {
            playSequentially(title, username, email, password, button, login)
            startDelay = 400
        }.start()
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
}