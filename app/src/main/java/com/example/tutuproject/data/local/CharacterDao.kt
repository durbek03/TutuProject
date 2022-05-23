package com.example.tutuproject.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tutuproject.data.models.Character

@Dao
interface CharacterDao {

    @Insert
    fun saveCharacter(vararg character: Character)

    @Query("select * from Character")
    fun getSavedCharacters() : List<Character>

    @Query("delete from Character")
    fun clearCharacterTable()
}