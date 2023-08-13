package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Dao
interface AsteroidsDao {

    @Query("SELECT * FROM databaseasteroids ORDER BY closeApproachDate ASC")
    fun getAsteroids(): List<DatabaseAsteroids>?

    @Query("SELECT * FROM databaseasteroids WHERE closeApproachDate = :todayDate")
    fun getAsteroidsToday(todayDate: String): List<DatabaseAsteroids>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroids)

    @Query("DELETE FROM databaseasteroids WHERE closeApproachDate < :todayDate")
    fun deleteAsteroidsBeforeToday(todayDate: String)
}

enum class AsteroidFilter(val value: String) { ALL("all"), WEEK("week"), DAY("day")}

@Database(entities = [DatabaseAsteroids::class], version = 1)
abstract class AsteroidsDatabase: RoomDatabase() {
    abstract val asteroidsDao: AsteroidsDao
}

private lateinit var INSTANCE: AsteroidsDatabase

fun getDatabase(context: Context): AsteroidsDatabase {
    synchronized(AsteroidsDatabase::class.java) {
        if(!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidsDatabase::class.java,
                "asteroids"
            ).build()
        }
    }
    return INSTANCE
}