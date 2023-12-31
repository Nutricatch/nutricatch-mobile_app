package com.nutricatch.dev.data.repository

import androidx.lifecycle.liveData
import com.nutricatch.dev.data.ResultState
import com.nutricatch.dev.data.api.ApiConfig
import com.nutricatch.dev.data.prefs.Preferences
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.net.UnknownHostException

class FoodsRepository(private val preferences: Preferences) {
    fun getFoodByName(name: String) = liveData {
        emit(ResultState.Loading)

        try {
            val token = preferences.getToken().first()

            val apiService = ApiConfig.getApiService(token)
            /// TODO ambil dari api jika sudah ada
            val foodNutrient = apiService.getFoodNutrient(name)

            emit(ResultState.Success(foodNutrient))
        } catch (e: HttpException) {
            val errorBody = e.response()?.message()
            when (val code = e.code()) {
                in 300..399 -> {
                    emit(ResultState.Error("Need To Reconfigure. Please Contact Administrator"))
                }

                in 400..499 -> {
                    emit(ResultState.Error("Request Error. code $code $errorBody"))
                }

                in 500..599 -> {
                    emit(ResultState.Error("Server Error"))
                }
            }
        } catch (e: UnknownHostException) {
            emit(ResultState.Error("Error... Please check your connection"))
        } catch (e: Exception) {
            emit(ResultState.Error("Unknown Error"))
        }
    }

    fun addNewEating(
        foodName: String,
        calories: Double,
        carbohydrates: Double,
        fat: Double,
        protein: Double,
        salt: Double,
        sugar: Double,
        fiber: Double
    ) = liveData {
        emit(ResultState.Loading)

        try {
            val token = preferences.getToken().first()

            val apiService = ApiConfig.getApiService(token)
            /// TODO ambil dari api jika sudah ada
            val responseItem =
                apiService.addNewEating(
                    foodName,
                    calories,
                    carbohydrates,
                    fat,
                    protein,
                    salt,
                    sugar,
                    fiber
                )

            emit(ResultState.Success(responseItem))
        } catch (e: HttpException) {
            val errorBody = e.response()?.message()
            when (val code = e.code()) {
                in 300..399 -> {
                    emit(ResultState.Error("Need To Reconfigure. Please Contact Administrator"))
                }

                in 400..499 -> {
                    emit(ResultState.Error("Request Error. code $code $errorBody"))
                }

                in 500..599 -> {
                    emit(ResultState.Error("Server Error"))
                }
            }
        } catch (e: UnknownHostException) {
            emit(ResultState.Error("Error... Please check your connection"))
        } catch (e: Exception) {
            emit(ResultState.Error("Unknown Error"))
        }
    }
}