package com.example.tutuproject.view.characterListScreen


//for ui control
sealed class CharacterListScreenStates {
    object NoDataToShow : CharacterListScreenStates()
    object Loading : CharacterListScreenStates()
    object HasDataToShow : CharacterListScreenStates()
    object ErrorOccurredAndNothingToShow : CharacterListScreenStates()
}