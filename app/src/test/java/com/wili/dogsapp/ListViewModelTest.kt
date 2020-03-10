package com.wili.dogsapp

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.wili.dogsapp.di.AppModule
import com.wili.dogsapp.di.DaggerViewModelComponent
import com.wili.dogsapp.model.DogBreed
import com.wili.dogsapp.model.DogDao
import com.wili.dogsapp.model.DogsApiService
import com.wili.dogsapp.util.SharedPreferencesHelper
import com.wili.dogsapp.viewmodel.ListViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.Callable
import java.util.concurrent.Executor

class ListViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    @Mock
    lateinit var dogsService: DogsApiService

    @Mock
    lateinit var prefs: SharedPreferencesHelper
    @Mock
    lateinit var dogDao:DogDao


    val application = Mockito.mock(Application::class.java)
    lateinit var listViewModel: ListViewModel
    val testDog = DogBreed("test",null,null,null,null,null,null).apply { uuid =1 }
    val testDogUUID = 1

    @Before
    fun setup(){
        listViewModel = ListViewModel(application, true)
        MockitoAnnotations.initMocks(this)
        DaggerViewModelComponent.builder()
            .appModule(AppModule(application))
            .apiModule(ApiModuleTest(dogsService))
            .prefsModule(PrefsModuleTest(prefs))
            .dogDatabaseModule(DogDatabaseModuleTest(dogDao))
            .build()
            .inject(listViewModel)
    }

    @Before
    fun setupRxSchedulers(){
        val immediate = object: Scheduler(){
            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() }, true)
            }
        }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { t: Callable<Scheduler> ->  immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { t: Callable<Scheduler> -> immediate }
    }

    @Test
    fun fetchFromRemoteSuccess(){
        val testSingle = Single.just(listOf(testDog))
        Mockito.`when`(dogsService.getDogs()).thenReturn(testSingle)
        testCoroutineRule.runBlockingTest {
            Mockito.`when`(dogDao.insertAll(*listOf(testDog).toTypedArray())).thenReturn(listOf(testDogUUID.toLong()))
            listViewModel.fetchFromRemote()
            Assert.assertEquals(1,listViewModel.dogs.value?.size)
            Assert.assertEquals(false, listViewModel.dogsLoadError.value)
            Assert.assertEquals(false,listViewModel.loading.value)
        }
    }

    @Test
    fun fetchFromRemoteSourceFailure(){
        val testSingle = Single.error<List<DogBreed>>(Throwable())
        Mockito.`when`(dogsService.getDogs()).thenReturn(testSingle)
        listViewModel.fetchFromRemote()
        Assert.assertEquals(null,listViewModel.dogs.value)
        Assert.assertEquals(false, listViewModel.loading.value)
        Assert.assertEquals(true,listViewModel.dogsLoadError.value)
    }

    @Test
    fun fetchFromDatabase(){
        testCoroutineRule.runBlockingTest {
            Mockito.`when`(dogDao.getAllDogs()).thenReturn(listOf(testDog))
            listViewModel.fetchFromDatabase()
            Assert.assertEquals(1,listViewModel.dogs.value?.size)
            Assert.assertEquals(false, listViewModel.loading.value)
            Assert.assertEquals(false,listViewModel.dogsLoadError.value)
        }
    }
}