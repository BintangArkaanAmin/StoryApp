package com.dicoding.storyapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.api.ApiConfig
import com.dicoding.storyapp.data.LoginResponse
import com.dicoding.storyapp.data.user.UserData
import com.dicoding.storyapp.data.user.UserPreferences
import com.dicoding.storyapp.viewmodel.factory.Event
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    fun login(email: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService("").postLogin(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _message.value = Event(response.body()?.message.toString())
                    _loginResponse.value = response.body()
                } else {
                    _message.value = Event(response.message().toString())
                    Log.e(
                        TAG,
                        "Failure: ${response.message()}, ${response.body()?.message.toString()}"
                    )
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = Event(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun saveSession(userData: UserData) {
        viewModelScope.launch {
            userPreferences.saveSession(userData)
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}