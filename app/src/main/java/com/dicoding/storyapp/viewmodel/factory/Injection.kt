package com.dicoding.storyapp.viewmodel.factory

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dicoding.storyapp.data.user.UserPreferences

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("token")

object Injection {

    fun provideRepository(context: Context): UserPreferences {
        return UserPreferences.getInstance(context.dataStore)
    }
}