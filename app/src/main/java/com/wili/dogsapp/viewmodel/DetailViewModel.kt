package com.wili.dogsapp.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wili.dogsapp.model.DogBreed
import com.wili.dogsapp.model.DogDatabase
import kotlinx.coroutines.launch

class DetailViewModel(application: Application): BaseViewModel(application){
    val dog = MutableLiveData<DogBreed>()

    fun fetch(dogUuid:Int){
        launch {
            val result = DogDatabase(getApplication()).getDao().getDog(dogUuid)
            dog.value = result
        }
    }
}