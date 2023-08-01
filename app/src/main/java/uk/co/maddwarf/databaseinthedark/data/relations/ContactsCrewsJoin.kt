package uk.co.maddwarf.databaseinthedark.data.relations

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Junction
import androidx.room.Relation
import uk.co.maddwarf.databaseinthedark.data.Contact
import uk.co.maddwarf.databaseinthedark.data.Crew
import uk.co.maddwarf.databaseinthedark.data.Scoundrel
import uk.co.maddwarf.databaseinthedark.model.Rank

@Entity(
primaryKeys = ["crewName", "contactId"],
    indices = [Index("crewName"), Index("contactId")]
)
data class ContactsCrewsJoin(
    val crewId: Int,
    val contactId:Int,
    val rank: Rank
)

data class CrewWithContacts(
    @Embedded val crew: Crew,
    @Relation(
        parentColumn = "crewId",
        entityColumn = "contactId",
        associateBy = Junction(ContactsCrewsJoin::class)
    )
    val contactsList: List<Contact>
)

data class ContactWithCrews(
    @Embedded val contact: Contact,
    @Relation(
        parentColumn = "contactId",
        entityColumn = "crewId",
        associateBy = Junction(ContactsCrewsJoin::class)
    )
    val screwList: List<Crew>
)

data class CrewAndRank(
    @Embedded val crew: Crew,
    val rank: Rank
)