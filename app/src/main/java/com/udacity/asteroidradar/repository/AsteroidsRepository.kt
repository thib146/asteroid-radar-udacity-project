package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NASAApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepository(private val database: AsteroidsDatabase) {

    val asteroids: LiveData<List<Asteroid>> = database.asteroidsDao.getAsteroids().map { it.asDomainModel() }

    suspend fun refreshAsteroids(startDate: String, endDate: String) {
        withContext(Dispatchers.IO) {
            val resultString = NASAApi.retrofitService.getAsteroids(startDate, endDate)
            val resultJson = JSONObject(resultString.body() ?: "")
            val asteroids = parseAsteroidsJsonResult(resultJson)
            database.asteroidsDao.insertAll(*asteroids.asDatabaseModel())
        }
    }
}