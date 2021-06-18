package com.example.nasaapp

import android.widget.ViewSwitcher
import androidx.lifecycle.ViewModelProvider
import com.example.nasaapp.repo.NasaInfoRepository
import com.example.nasaapp.service.NasaInfoService

object Injection {
    fun provideNasaInfoRepository() :NasaInfoRepository {
            return NasaInfoRepository(NasaInfoService.create())
    }
    fun viewModelFactory() : ViewModelProvider.Factory {
        return ViewModelFactory(provideNasaInfoRepository())
    }
}