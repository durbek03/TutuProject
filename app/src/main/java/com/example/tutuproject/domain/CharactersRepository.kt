package com.example.tutuproject.domain

import com.example.tutuproject.data.models.Character
import com.example.tutuproject.others.Resource
import kotlinx.coroutines.flow.Flow

interface CharactersRepository {
    suspend fun getCharacters(page: Int) : List<Character>

    suspend fun getSavedCharacters() : List<Character>

    fun saveCharacter(vararg character: Character)
}