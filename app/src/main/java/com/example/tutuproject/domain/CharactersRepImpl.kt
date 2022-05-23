package com.example.tutuproject.domain

import android.util.Log
import com.example.tutuproject.data.local.AppDatabase
import com.example.tutuproject.data.models.Character
import com.example.tutuproject.data.modelsDto.toCharacter
import com.example.tutuproject.data.remote.ApiService
import com.example.tutuproject.others.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class CharactersRepImpl @Inject constructor(
    val apiService: ApiService,
    val appDatabase: AppDatabase,
) : CharactersRepository {
    private val TAG = "CharactersRepImpl"
    override suspend fun getCharacters(page: Int): List<Character> =
        apiService.getCharacters(page).characterDtos.map { it.toCharacter() }


    override suspend fun getSavedCharacters(): List<Character> =
        appDatabase.characterDao().getSavedCharacters()


    override fun saveCharacter(vararg character: Character) {
        appDatabase.characterDao().clearCharacterTable()
        appDatabase.characterDao().saveCharacter(*character)
    }
}