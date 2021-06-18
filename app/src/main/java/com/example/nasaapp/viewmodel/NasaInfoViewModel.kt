package com.example.nasaapp.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.ImageView
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.*
import com.example.nasaapp.model.NasaInfoResponse
import com.example.nasaapp.repo.NasaInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class NasaInfoViewModel(val nasaInfoRepository: NasaInfoRepository) : ViewModel(), LifecycleObserver {

    var isConnected = ObservableBoolean(false)

    val title: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val description : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val image : MutableLiveData<Bitmap> by lazy {
        MutableLiveData<Bitmap>()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        if(isConnected.get()) {
            var nasaInfoResponse: NasaInfoResponse? = null
            viewModelScope.launch(Dispatchers.IO) {
                nasaInfoResponse = nasaInfoRepository.getDetails()
                updateValue(nasaInfoResponse)
                updateImage(nasaInfoResponse)
            }
        }
    }

    suspend fun updateImage(nasaInfoResponse: NasaInfoResponse?) {
        val bitmapFromURL = getBitmapFromURL(nasaInfoResponse?.url)
        image.postValue(bitmapFromURL)
    }


    private fun updateValue(nasaInfoResponse: NasaInfoResponse?) {
        nasaInfoResponse?.let {
            title.postValue(it.title)
            description.postValue(it.explanation)
        }
    }


    suspend fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
