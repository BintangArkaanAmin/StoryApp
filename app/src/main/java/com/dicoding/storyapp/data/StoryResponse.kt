package com.dicoding.storyapp.data
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class StoryResponse(
    @field:SerializedName("listStory")
    val listStory: List<StoryItem>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,
)

@Parcelize
data class StoryItem(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("photoUrl")
    val photo: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("lon")
    val lon: Double,

    @field:SerializedName("lat")
    val lat: Double

): Parcelable