package com.udacity.asteroidradar.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.api.getTodayAndOneWeekFormattedDates
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import retrofit2.HttpException

class RefreshAsteroidsDataWorker(appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {
    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AsteroidsRepository(database)

        return try {
            val formattedDates = getTodayAndOneWeekFormattedDates()
            repository.refreshAsteroids(formattedDates[0], formattedDates[1])
            repository.deleteOldAsteroids(formattedDates[0])
            return Result.success()
        } catch (exception: HttpException) {
            Result.retry()
        }
    }
}