package com.udacity.asteroidradar.database

import com.udacity.asteroidradar.Asteroid

fun List<Asteroid>.asDatabaseModel(): Array<DatabaseAsteroids> {
    return map {
        DatabaseAsteroids (
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}