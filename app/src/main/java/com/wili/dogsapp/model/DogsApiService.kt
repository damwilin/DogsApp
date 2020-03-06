package com.wili.dogsapp.model

import com.wili.dogsapp.di.DaggerApiComponent
import io.reactivex.Single
import javax.inject.Inject

class DogsApiService {

    @Inject
    lateinit var api:DogsApi

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun getDogs(): Single<List<DogBreed>>{
        return api.getDogs()
    }
}