package uk.co.maddwarf.databaseinthedark.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import uk.co.maddwarf.databaseinthedark.data.Crew
import uk.co.maddwarf.databaseinthedark.data.Scoundrel

data class CrewWithScoundrels(
    @Embedded val crew: Crew,
    @Relation(
        parentColumn = "crewId",
        entityColumn = "crewId"
    )
    val scoundrels: List<Scoundrel>
)