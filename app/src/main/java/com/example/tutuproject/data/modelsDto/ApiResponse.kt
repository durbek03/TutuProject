package com.example.tutuproject.data.modelsDto


import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("info")
    val info: Info,
    @SerializedName("results")
    val characterDtos: List<CharacterDto>
)