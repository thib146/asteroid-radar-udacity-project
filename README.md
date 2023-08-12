# AsteroidRadar Project with Udacity

This app is my submission for an exercise project app part of the "Android Nanodegree" online class with Udacity.com.

It showcases simple usage of network handling with Retrofit, Room, Background worker, RecyclerViews, localization.

# Features

* The app uses Retrofit to get data from NASA's API
* The app uses a database setup with Room. All the data is stored so that the app is entirely usable offline
* Tha app uses a background worker that will get data from the API in the background once a day, if the device is plugged in + on WiFi
* The app has improved screen layouts when rotating the device in landscape mode
* The app is accessible: every view/image can be read by Android TalkBack for people with visual disabilities
* The app is translated in French

# Project Instructions

NOTE: add your NASA API Key in the `Constants.kt` file in the `API_KEY` variable

The app contains 2 screens in this order:

* Asteroids List screen: shows a list of asteroids passing by close to the earth, taken from NASA's API - https://api.nasa.gov/
* Asteroid Detail screen: shows the details of a specific asteroid when tapped on

