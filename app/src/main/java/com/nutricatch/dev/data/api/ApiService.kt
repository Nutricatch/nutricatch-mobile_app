package com.nutricatch.dev.data.api

import com.nutricatch.dev.data.api.response.ConsumeResponse
import com.nutricatch.dev.data.api.response.DailyIntakeResponse
import com.nutricatch.dev.data.api.response.DiamondResponse
import com.nutricatch.dev.data.api.response.FoodsResponseItem
import com.nutricatch.dev.data.api.response.HealthResponse
import com.nutricatch.dev.data.api.response.RecommendedNutritionResponse
import com.nutricatch.dev.data.api.response.RestaurantResponseItem
import com.nutricatch.dev.data.api.response.UserResponse
import com.nutricatch.dev.data.response.AuthResponse
import com.nutricatch.dev.model.LatestPostResponse
import com.nutricatch.dev.model.Recipe
import com.nutricatch.dev.model.RecipeListResponse
import okhttp3.MultipartBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("latest_post")
    suspend fun getLatestPosts(): LatestPostResponse

    @GET("recipes")
    suspend fun getRecipes(): RecipeListResponse

    @GET("recipe/{id}")
    suspend fun getRecipe(@Path("id") id: Int): Recipe


    /*
    *   Auth Section
    * */
    @FormUrlEncoded
    @POST("auth/login")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): AuthResponse

    @FormUrlEncoded
    @POST("auth/register")
    suspend fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): AuthResponse

    /*
    *   User Profile and health
    * */
    @GET("user-health/profile")
    suspend fun getProfile(): UserResponse

    @GET("user-health/health")
    suspend fun getHealthData(): HealthResponse

    @FormUrlEncoded
    @POST("/user-health/update")
    suspend fun updateHealthData(
        @Field("weight") weight: Double,
        @Field("height") height: Double,
        @Field("age") age: Double,
        @Field("gender") gender: String,
        @Field("fitnessGoal") fitnessGoal: String,
        @Field("activityLevel") activityLevel: String,
    ): HealthResponse

    @GET("/restaurants/search")
    suspend fun getNearbyRestaurants(
        @Query("latitude") lat: Double,
        @Query("longitude") lng: Double
    ): MutableList<RestaurantResponseItem>

    @GET("/foods/{name}")
    suspend fun getFoodNutrient(
        @Path("name") name: String
    ): FoodsResponseItem

    /*
    *   User daily food intake
    * */

    @GET("/nutrition-recommender/daily-recomended-nutrition")
    suspend fun getRecommendedNutrition(): RecommendedNutritionResponse

    @GET("/daily-consumtion/daily-consumtion-by-date")
    suspend fun getConsumptionByDate(
        @Query("date") date: String
    ): List<ConsumeResponse>

    @FormUrlEncoded
    @POST("/daily-consumtion/create-daily-consumtion")
    suspend fun addNewEating(
        @Field("foodName") foodName: String,
        @Field("calories") calories: Double,
        @Field("carbohydrates") carb: Double,
        @Field("fat") fat: Double,
        @Field("protein") protein: Double,
        @Field("salt") salt: Double,
        @Field("sugar") sugar: Double,
        @Field("fiber") fiber: Double
    ): FoodsResponseItem

    @GET("/diamonds")
    suspend fun getDiamonds(): Int

    @GET("/diamonds/use-one")
    suspend fun useDiamond()

    @FormUrlEncoded
    @POST("/diamonds/add")
    suspend fun addDiamond(@Field("diamondCounts")diamondCounts: Int) :DiamondResponse
}