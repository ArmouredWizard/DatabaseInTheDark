package uk.co.maddwarf.databaseinthedark.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scoundrels")
data class Scoundrel (
@PrimaryKey(autoGenerate = true)
    val scoundrelId: Int,
    val name: String,
    val playbook: String,
    val crew: String,
    val hunt:String,
    val study:String,
    val survey:String,
    val tinker:String,
    val finesse: String,
    val prowl: String,
    val skirmish: String,
    val wreck: String,
    val attune: String,
    val command: String,
    val consort: String,
    val sway: String,
    val heritage: String,
    val background: String,
    val specialAbilities: List<String>

){
}