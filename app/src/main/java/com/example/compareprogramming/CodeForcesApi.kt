package com.example.compareprogramming

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://codeforces.com/api/"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
    .baseUrl(BASE_URL)
    .build()

interface CodeForcesApiService {
    @GET("contest.list")
    suspend fun getContest(@Query("gym") gym: Boolean): Contest_Json

    @GET("user.info")
    suspend fun getUser(@Query("handles") handles: String): User_Json

    @GET("user.rating")
    suspend fun getUserRating(@Query("handle") handle: String): Ratingchange_Json

    @GET("user.status")
    suspend fun getUserStatus(
        @Query("handle") handle: String,
        @Query("from") from: Int = 1,
        @Query("count") count: Int = 100
    ): Submission

}

object CodeForcesApi {
    val retrofitService: CodeForcesApiService by lazy {
        retrofit.create(CodeForcesApiService::class.java)
    }
}


