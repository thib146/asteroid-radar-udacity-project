package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.NASAApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

enum class NASAApiStatus { LOADING, ERROR, DONE }

class MainViewModel : ViewModel() {

    // The internal MutableLiveData String that stores the status of the most recent network request
    private val _status = MutableLiveData<NASAApiStatus>()

    // The external immutable LiveData for the request status String
    val status: LiveData<NASAApiStatus>
        get() = _status

    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid?>()
    val navigateToSelectedAsteroid: LiveData<Asteroid?>
        get() = _navigateToSelectedAsteroid

    init {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val todayDate = dateFormat.format(currentTime)
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val timeInOneWeek = calendar.time
        val oneWeekDate = dateFormat.format(timeInOneWeek)

        getAsteroids(todayDate, oneWeekDate)
    }

    private fun getAsteroids(startDate: String, endDate: String) {
        viewModelScope.launch {
            _status.value = NASAApiStatus.LOADING
            try {
                val resultString = NASAApi.retrofitService.getAsteroids(startDate, endDate)
                val resultJson = JSONObject(resultString.body() ?: "")
                val listResult = parseAsteroidsJsonResult(resultJson)
                if (listResult.isNotEmpty()) {
                    _asteroids.value = listResult
                }
                _status.value = NASAApiStatus.DONE
            } catch (e: Exception) {
                _status.value = NASAApiStatus.ERROR
                Log.e("MainViewModel", "Error:" + e.message)
                _asteroids.value = ArrayList()
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