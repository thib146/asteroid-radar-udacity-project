package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NASAApi
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

enum class NASAApiStatus { LOADING, ERROR, DONE }

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(database)

    val asteroids = asteroidsRepository.asteroids

    private val _statusAsteroids = MutableLiveData<NASAApiStatus>()
    val statusAsteroids: LiveData<NASAApiStatus>
        get() = _statusAsteroids

    private val _statusPictureDay = MutableLiveData<NASAApiStatus>()
    val statusPictureDay: LiveData<NASAApiStatus>
        get() = _statusPictureDay

    private val _pictureDay = MutableLiveData<PictureOfDay>()
    val pictureDay: LiveData<PictureOfDay>
        get() = _pictureDay

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid?>()
    val navigateToSelectedAsteroid: LiveData<Asteroid?>
        get() = _navigateToSelectedAsteroid

    init {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val todayDate = dateFormat.format(currentTime)
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        val timeInOneWeek = calendar.time
        val oneWeekDate = dateFormat.format(timeInOneWeek)

        getAsteroids(todayDate, oneWeekDate)
        getPictureOfDay()
    }

    private fun getAsteroids(startDate: String, endDate: String) {
        viewModelScope.launch {
            _statusAsteroids.value = NASAApiStatus.LOADING
            try {
                asteroidsRepository.refreshAsteroids(startDate, endDate)
                _statusAsteroids.value = NASAApiStatus.DONE
            } catch (e: Exception) {
                _statusAsteroids.value = NASAApiStatus.ERROR
                Log.e("MainViewModel", "Error:" + e.message)
            }
        }
    }

    private fun getPictureOfDay() {
        viewModelScope.launch {
            _statusPictureDay.value = NASAApiStatus.LOADING
            try {
                val result = NASAApi.retrofitService.getPictureOfDay()
                _pictureDay.value = result
                _statusPictureDay.value = NASAApiStatus.DONE
            } catch (e: Exception) {
                _statusPictureDay.value = NASAApiStatus.ERROR
                _pictureDay.value = PictureOfDay("string", "", "")
                Log.e("MainViewModel", "Error:" + e.message)
            }
        }
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }

}