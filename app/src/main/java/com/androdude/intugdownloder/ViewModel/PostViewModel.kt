package com.androdude.intugdownloder.ViewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.androdude.intugdownloder.db.Repository.Repository
import com.androdude.intugdownloder.db.model.PostRespose
import retrofit2.Response

class PostViewModel : ViewModel() {
    private var repository: Repository = Repository()

    fun getPosts(url : String) : LiveData<Response<PostRespose>>  = liveData {
        val response = repository.getPosts(url)
        emit(response)
    }
}