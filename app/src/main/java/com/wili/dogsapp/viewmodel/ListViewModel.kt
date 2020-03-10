package com.wili.dogsapp.viewmodel

import android.app.Application
import android.app.NotificationManager
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wili.dogsapp.di.AppModule
import com.wili.dogsapp.di.CONTEXT_APP
import com.wili.dogsapp.di.DaggerViewModelComponent
import com.wili.dogsapp.di.TypeOfContext
import com.wili.dogsapp.model.DogBreed
import com.wili.dogsapp.model.DogDao
import com.wili.dogsapp.model.DogDatabase
import com.wili.dogsapp.model.DogsApiService
import com.wili.dogsapp.util.NotificationHelper
import com.wili.dogsapp.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.NumberFormatException
import javax.inject.Inject

class ListViewModel(application: Application) : BaseViewModel(application) {

    constructor(application: Application, test:Boolean = true):this(application){
        injected = test
    }

    @Inject
    @field:TypeOfContext(type = CONTEXT_APP)
    lateinit var prefHelper: SharedPreferencesHelper
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L //5 minutes in nanoseconds
    @Inject
    lateinit var dogsService:DogsApiService
    @Inject
    lateinit var dogDao: DogDao

    private val disposable = CompositeDisposable()
    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    private var injected = false

    fun inject() {
        if (!injected){
            DaggerViewModelComponent.builder()
                .appModule(AppModule(getApplication()))
                .build()
                .inject(this)
        }
    }

    fun refresh() {
        inject()
        checkCacheDuration()
        val updateTime = prefHelper.getUpdateTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime){
            fetchFromDatabase()
        } else {
            fetchFromRemote()
        }
    }

    private fun checkCacheDuration() {
        val cachePreference = prefHelper.getCacheDuration()
        try{
            val cachePreferenceInt = cachePreference?.toInt() ?: 5*60
            refreshTime = cachePreferenceInt.times(1000 * 1000 * 1000L)
        } catch (e: NumberFormatException){
            e.printStackTrace()
        }
    }

    fun refreshBypassCache(){
        inject()
        fetchFromRemote()
    }

    fun fetchFromDatabase() {
        inject()
        loading.value = true
        launch {
            val dogs = dogDao.getAllDogs()
            dogsRetrieved(dogs)
        }
    }

    fun fetchFromRemote() {
        inject()
        loading.value = true
        disposable.add(
            dogsService.getDogs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<DogBreed>>() {
                    override fun onSuccess(dogList: List<DogBreed>) {
                        storeDogsLocally(dogList)
                    }

                    override fun onError(e: Throwable) {
                        dogsLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }
                })
        )
    }

    private fun dogsRetrieved(dogList: List<DogBreed>) {
        dogs.value = dogList
        dogsLoadError.value = false
        loading.value = false
    }

    private fun storeDogsLocally(list: List<DogBreed>) {
        launch {
            dogDao.deleteAllDogs()
            val result = dogDao.insertAll(*list.toTypedArray())
                var i = 0
                while (i < list.size) {
                    list[i].uuid = result[i].toInt()
                    ++i
                }
            dogsRetrieved(list)
        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}