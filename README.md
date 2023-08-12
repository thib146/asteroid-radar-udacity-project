# AsteroidRadar Project with Udacity

This app is my submission for an exercise project app part of the "Android Nanodegree" online class with Udacity.com.

It showcases simple usage of network handling with Retrofit, Room, Background worker, RecyclerViews.

# Features

* The app uses Retrofit to get data from NASA's API
* The app uses a database setup with Room. All the data is stored so that the app is entirely usable offline
* Tha app uses a background worker that will get data from the API in the background once a day, if the device is plugged in + on WiFi

# Project Instructions

The app contains 2 screens in this order:

* Asteroids List screen: shows a list of asteroids passing by close to the earth, taken from NASA's API - https://api.nasa.gov/
* Asteroid Detail screen: shows the details of a specific asteroid when tapped on

