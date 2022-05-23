package com.example.tutuproject.useCases

import com.example.tutuproject.data.models.Character
import com.example.tutuproject.domain.CharactersRepository
import javax.inject.Inject

class SaveToLocaleUseCase @Inject constructor(
    private val repository: CharactersRepository
)
{
    fun saveToLocale(list: List<Character>) {
        repository.saveCharacter(*list.toTypedArray())
    }
}