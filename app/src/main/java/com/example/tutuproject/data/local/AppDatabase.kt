package com.example.tutuproject.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tutuproject.data.models.Character

@Database(entities = [Character::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}