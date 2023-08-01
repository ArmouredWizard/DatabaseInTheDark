package uk.co.maddwarf.databaseinthedark.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "crews")
data class Crew(
    @PrimaryKey(autoGenerate = false)
    val crewId:Int,
    val crewName: String,
    val hq: String,
    val rep: String,
    val tier: String,
    val heat: String,
    val holdIsStrong: Boolean,
    val type: String,
    val reputation: String,
    val huntingGrounds: String,
    val crewAbilities: List<String>

)