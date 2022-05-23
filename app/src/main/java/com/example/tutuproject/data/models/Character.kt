package com.example.tutuproject.data.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Character(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo
    val characterName: String,
    @ColumnInfo
    val status: String,
    @ColumnInfo
    val species: String,
    @ColumnInfo
    val origin: String,
    @ColumnInfo
    val image: String
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        return (other is Character
                && this.characterName == other.characterName
                && this.origin == other.origin
                && this.species == other.species
                && this.status == other.status)
    }
}