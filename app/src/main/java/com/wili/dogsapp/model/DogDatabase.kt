package com.wili.dogsapp.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(DogBreed::class), version = 1)
abstract class DogDatabase : RoomDatabase() {
    abstract fun getDao(): DogDao

    companion object{
        @Volatile private var instance:DogDatabase? = null
        private val LOCK = Any()
        private const val DATABASE_NAME = "dogdatabase"

        operator fun invoke(context: Context) = instance?: synchronized(LOCK) {
            instance?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            DogDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}