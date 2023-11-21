package com.dicoding.storyapp.api

import com.dicoding.storyapp.data.LoginResponse
import com.dicoding.storyapp.data.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("stories")
    fun getListStories(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Call<StoryResponse>

    @Multipart
    @POST("stories")
    fun postStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<StoryResponse>
}