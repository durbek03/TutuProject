package com.example.tutuproject.view.characterListScreen

import com.example.tutuproject.data.models.Character

sealed class CharacterListScreenIntent {
    object ShowLoadingNextPage : CharacterListScreenIntent()
    object HideLoadingNextPage : CharacterListScreenIntent()
    object ScrollRvTop : CharacterListScreenIntent()
}