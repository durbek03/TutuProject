package com.example.tutuproject.useCases

import com.example.tutuproject.data.models.Character
import com.example.tutuproject.domain.CharactersRepository
import com.example.tutuproject.others.ConnectionMode
import com.example.tutuproject.others.Resource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject


class GetCharacterListUseCase @Inject constructor(val repository: CharactersRepository) {
    suspend fun getFromNetwork(
        page: Int = 1,
    ): List<Character> =
            repository.getCharacters(page ?: 1)

    suspend fun getFromLocale(): List<Character> = repository.getSavedCharacters()
}