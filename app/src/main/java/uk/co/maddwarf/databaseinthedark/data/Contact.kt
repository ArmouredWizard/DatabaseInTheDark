package uk.co.maddwarf.databaseinthedark.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class Contact (
    @PrimaryKey(autoGenerate = true)
    val contactId: Int,
    val name:String
)