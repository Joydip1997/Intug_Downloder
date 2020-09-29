package com.androdude.intugdownloder.api

import com.androdude.intugdownloder.db.model.PostRespose
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url


interface PostApiService {

    @GET
    suspend fun getPostDetails(@Url url : String) : Response<PostRespose>


    companion object
    {
        operator fun invoke() : PostApiService
        {


            return Retrofit.Builder()
                .baseUrl("https://www.instagram.com/") // Add any base url here
                .addConverterFactory(GsonConverterFactory.create()).build().create(PostApiService::class.java)
        }
    }
}
