package com.dicoding.storyapp.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.dicoding.storyapp.api.ApiConfig
import com.dicoding.storyapp.data.StoryItem
import com.dicoding.storyapp.data.StoryResponse
import com.dicoding.storyapp.data.user.UserData
import com.dicoding.storyapp.data.user.UserPreferences
import com.dicoding.storyapp.viewmodel.factory.Event
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val userPreferences: UserPreferences) : ViewModel()  {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _storyResponse = MutableLiveData<List<StoryItem>>()
    val storyResponse: LiveData<List<StoryItem>> = _storyResponse

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    fun getStory(){
        viewModelScope.launch {
            _isLoading.value = true
            val client = ApiConfig.getApiService(getToken()).getListStories(1, 10)
            client.enqueue(object : Callback<StoryResponse> {
                override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body() != null) {
                        _message.value = Event(response.body()?.message.toString())
                        _storyResponse.value = response.body()!!.listStory!!
                    } else {
                        _message.value = Event(response.message().toString())
                        Log.e(
                            TAG,
                            "Failure: ${response.message()}, ${response.body()?.message.toString()}"
                        )
                    }
                }

                override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                    _isLoading.value = false
                    _message.value = Event(t.message.toString())
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
        }
    }

    suspend fun getToken(): String {
        return userPreferences.getSession().first().token
    }

    fun getSession(): LiveData<UserData> {
        return userPreferences.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            userPreferences.logout()
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}