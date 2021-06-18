package com.example.nasaapp.repo

import com.example.nasaapp.model.NasaInfoResponse
import com.example.nasaapp.service.NasaInfoService

class NasaInfoRepository(private val nasaInfoService: NasaInfoService) {
    suspend fun getDetails(): NasaInfoResponse {
        return nasaInfoService.getNasaInfo("ienJZ3XS8Uon5rj5GA09ooV9MnwV7x1JuSjV0alv")
    }
}