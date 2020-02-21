package com.wili.dogsapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wili.dogsapp.model.DogBreed

class ListViewModel: ViewModel(){
    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh(){
        val dog1 = DogBreed("1","CORGI1","15 years", "breedgroup","bredfor", "temperament", "")
        val dog2 = DogBreed("2","CORGI2","15 years", "breedgroup","bredfor", "temperament", "")
        val dog3 = DogBreed("3","CORGI3","15 years", "breedgroup","bredfor", "temperament", "")
        dogs.value = listOf(dog1,dog2,dog3)
        dogsLoadError.value = false
        loading.value = false
    }
}