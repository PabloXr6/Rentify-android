package com.example.rentify.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [VehicleEntity::class], version = 1, exportSchema = false)
abstract class RentifyDatabase : RoomDatabase() {

    abstract fun favoriteVehicleDao(): FavoriteVehicleDao

    companion object {
        @Volatile
        private var INSTANCE: RentifyDatabase? = null

        fun getDatabase(context: Context): RentifyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RentifyDatabase::class.java,
                    "rentify_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}