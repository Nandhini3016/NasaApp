package com.example.nasaapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nasaapp.repo.NasaInfoRepository
import com.example.nasaapp.viewmodel.NasaInfoViewModel

class ViewModelFactory(private val nasaInfoRepository: NasaInfoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
       if(modelClass.isAssignableFrom(NasaInfoViewModel::class.java))
           return NasaInfoViewModel(nasaInfoRepository) as T
        else
            throw IllegalAccessException("unKnown class")
    }
}