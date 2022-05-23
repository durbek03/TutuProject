package com.example.tutuproject.view.characterListScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tutuproject.data.models.Character
import com.example.tutuproject.others.ConnectionMode
import com.example.tutuproject.others.Constants
import com.example.tutuproject.useCases.GetCharacterListUseCase
import com.example.tutuproject.useCases.SaveToLocaleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(val getCharacterListUseCase: GetCharacterListUseCase, val saveToLocaleUseCase: SaveToLocaleUseCase) :
    ViewModel() {
    private val TAG = "CharacterListViewModel"
    val screenStates = MutableStateFlow<CharacterListScreenStates?>(null)
    val channel = Channel<CharacterListScreenIntent>()
    val action = channel.receiveAsFlow()
    var characterList = MutableStateFlow<List<Character>>(emptyList())
    val page = MutableSharedFlow<Int>()
    val lastVisibleItemPosition = MutableStateFlow<Int>(0)
    val connectionMode = MutableStateFlow<ConnectionMode>(ConnectionMode.Offline)
    var afterLocaleShown = false
    var currentPage = 0

    init {
        handlePageChange()
        handleConnectivityChange()
    }

    fun saveToDb(list: List<Character>) {
        viewModelScope.launch(Dispatchers.IO) {
            saveToLocaleUseCase.saveToLocale(list)
        }
    }

    fun handleConnectivityChange() {
        viewModelScope.launch {
            connectionMode.collectLatest {
                when (it) {
                    ConnectionMode.Offline -> {
                        Log.d(TAG, "handleConnectivityChange: offline")
                        channel.send(CharacterListScreenIntent.ShowLoadingNextPage)
                        if (characterList.value.isEmpty()) {
                            val fromLocale: List<Character>
                            withContext(Dispatchers.IO) {
                                fromLocale = getCharacterListUseCase.getFromLocale()
                            }
                            if (fromLocale.isNotEmpty()) {
                                characterList.emit(fromLocale)
                                screenStates.emit(CharacterListScreenStates.HasDataToShow)
                                afterLocaleShown = true
                            } else {
                                screenStates.emit(CharacterListScreenStates.NoDataToShow)
                            }
                        }
                    }
                    ConnectionMode.Online -> {
                        Log.d(TAG, "handleConnectivityChange: online")
                        lastVisibleItemPosition.collectLatest {
                            if (afterLocaleShown) {
                                currentPage++
                                afterLocaleShown = false
                            }
                            if (it == (currentPage * 20 - 1) || characterList.value.isEmpty()) {
                                page.emit(currentPage + 1)
                            }
                        }
                    }
                }
            }
        }
    }

    fun handlePageChange() {
        viewModelScope.launch {
            page.collect {
                currentPage = it
                if (characterList.value.isNotEmpty()) {
                    channel.send(CharacterListScreenIntent.ShowLoadingNextPage)
                } else {
                    screenStates.emit(CharacterListScreenStates.Loading)
                }
                try {
                    val fromNetwork = getCharacterListUseCase.getFromNetwork(it)
                    val newList = mutableListOf<Character>()
                    newList.addAll(characterList.value)
                    newList.addAll(fromNetwork)
                    characterList.emit(newList)
                    screenStates.emit(CharacterListScreenStates.HasDataToShow)
                } catch (e: Exception) {
                    if (characterList.value.isEmpty()) {
                        screenStates.emit(CharacterListScreenStates.ErrorOccurredAndNothingToShow)
                    } else {
                        screenStates.emit(CharacterListScreenStates.HasDataToShow)
                    }
                }
                channel.send(CharacterListScreenIntent.HideLoadingNextPage)
                Log.d(TAG, "handlePageChange: $it")
            }
        }
    }
}