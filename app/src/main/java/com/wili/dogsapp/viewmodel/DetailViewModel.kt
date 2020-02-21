package com.wili.dogsapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wili.dogsapp.model.DogBreed

class DetailViewModel: ViewModel(){
    val dog = MutableLiveData<DogBreed>()

    fun fetch(){
        dog.value = DogBreed("1","CORGI1","15 years", "breedgroup","bredfor", "temperament", "")
    }
}