package uk.co.maddwarf.databaseinthedark.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val category: String,
    val bodyText: String
){
}