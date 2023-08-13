package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NASAApi
import com.udacity.asteroidradar.api.getTodayFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidFilter
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepository(private val database: AsteroidsDatabase) {

    suspend fun refreshAsteroids(startDate: String, endDate: String) {
        withContext(Dispatchers.IO) {
            val resultString = NASAApi.retrofitService.getAsteroids(startDate, endDate)
            val resultJson = JSONObject(resultString.body() ?: "")
            val asteroids = parseAsteroidsJsonResult(resultJson)
            database.asteroidsDao.insertAll(*asteroids.asDatabaseModel())
            deleteOldAsteroids(startDate)
        }
    }

    suspend fun getAllAsteroids(): List<Asteroid>? {
        return withContext(Dispatchers.IO) {
            database.asteroidsDao.getAsteroids()?.asDomainModel()
        }
    }

    suspend fun getFilteredAsteroids(filter: AsteroidFilter): List<Asteroid>? {
        return withContext(Dispatchers.IO) {
            when (filter) {
                AsteroidFilter.ALL, AsteroidFilter.WEEK -> {
                    database.asteroidsDao.getAsteroids()?.asDomainModel()
                }

                AsteroidFilter.DAY -> {
                    database.asteroidsDao.getAsteroidsToday(getTodayFormattedDates()).asDomainModel()
                }
            }
        }
    }

    suspend fun deleteOldAsteroids(startDate: String) {
        withContext(Dispatchers.IO) {
            database.asteroidsDao.deleteAsteroidsBeforeToday(startDate)
        }
    }

}