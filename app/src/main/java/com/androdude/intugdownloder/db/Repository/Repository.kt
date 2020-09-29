package com.androdude.intugdownloder.db.Repository

import androidx.lifecycle.LiveData
import com.androdude.intugdownloder.api.PostApiService
import com.androdude.intugdownloder.db.model.PostRespose
import retrofit2.Response

class Repository {

    suspend fun getPosts(url : String) : Response<PostRespose>
    {
      return  PostApiService().getPostDetails(url)
    }
}