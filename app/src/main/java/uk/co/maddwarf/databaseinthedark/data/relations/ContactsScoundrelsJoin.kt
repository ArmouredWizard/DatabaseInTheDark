package uk.co.maddwarf.databaseinthedark.data.relations

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Junction
import androidx.room.Relation
import uk.co.maddwarf.databaseinthedark.data.Contact
import uk.co.maddwarf.databaseinthedark.data.Scoundrel
import uk.co.maddwarf.databaseinthedark.model.Rank

@Entity(
    primaryKeys = ["scoundrelId", "contactId"],
    indices = [Index("scoundrelId"), Index("contactId")],
/*    foreignKeys = [
        ForeignKey(
            entity = Scoundrel::class,
            parentColumns = arrayOf("scoundrelId"),
            childColumns = arrayOf("scoundrelId")
        ),
        ForeignKey(
            entity = Contact::class,
            parentColumns = arrayOf("contactId"),
            childColumns = arrayOf("contactId")
        )
    ]*/
)
data class ContactsScoundrelsJoin(
    val contactId: Int,
    val scoundrelId: Int,
    val rank:Rank
)

data class ScoundrelWithContacts(
    @Embedded val scoundrel: Scoundrel,
    @Relation(
    parentColumn = "scoundrelId",
        entityColumn = "contactId",
        associateBy = Junction(ContactsScoundrelsJoin::class)
    )
    val contactsList: List<Contact>
)

data class ContactWithScoundrels(
    @Embedded val contact: Contact,
    @Relation(
        parentColumn = "contactId",
        entityColumn = "scoundrelId",
        associateBy = Junction(ContactsScoundrelsJoin::class)
    )
    val scoundrelList: List<Scoundrel>
)

data class ContactAndRank(
    // @Embedded val scoundrel: Scoundrel,
    @Embedded val contact: Contact,
    val rank: Rank
)

data class ScoundrelAndRank(
    @Embedded val scoundrel: Scoundrel,
    val rank: Rank
)