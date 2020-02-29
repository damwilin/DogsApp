package com.wili.dogsapp.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DogDao{
    @Insert
    //varargs -> to insert multiple objects of type DogBreed
    //suspend function -> because have to work on background thread
    suspend fun insertAll(vararg dogs: DogBreed):List<Long>

    @Query("SELECT * FROM DogBreed")
    suspend fun getAllDogs():List<DogBreed>

    @Query("SELECT * FROM DogBreed WHERE uuid= :dogId")
    suspend fun getDog(dogId:Int): DogBreed

    @Query("DELETE FROM DogBreed")
    suspend fun deleteAllDogs()
}