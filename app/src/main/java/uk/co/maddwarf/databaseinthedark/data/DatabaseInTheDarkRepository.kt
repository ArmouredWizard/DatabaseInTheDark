package uk.co.maddwarf.databaseinthedark.data

import kotlinx.coroutines.flow.Flow
import uk.co.maddwarf.databaseinthedark.data.relations.ContactsScoundrelsJoin
import uk.co.maddwarf.databaseinthedark.data.relations.CrewWithScoundrels
import uk.co.maddwarf.databaseinthedark.data.relations.ScoundrelWithContacts
import uk.co.maddwarf.databaseinthedark.data.relations.ContactAndRank
import uk.co.maddwarf.databaseinthedark.data.relations.ContactWithCrews
import uk.co.maddwarf.databaseinthedark.data.relations.ContactWithScoundrels
import uk.co.maddwarf.databaseinthedark.data.relations.ContactsCrewsJoin
import uk.co.maddwarf.databaseinthedark.data.relations.CrewAndRank
import uk.co.maddwarf.databaseinthedark.data.relations.CrewWithContacts
import uk.co.maddwarf.databaseinthedark.data.relations.ScoundrelAndRank

interface DatabaseInTheDarkRepository{
    fun getAllScoundrelsStream(): Flow<List<Scoundrel>>
    fun getScoundrelStream(id:Int): Flow<Scoundrel?>
    suspend fun insertScoundrel(scoundrel: Scoundrel):Long
    suspend fun deleteScoundrel(scoundrel: Scoundrel)
    suspend fun updateScoundrel(scoundrel: Scoundrel)

    suspend fun getScoundrelsByCrew(crewName:String):List<Scoundrel>

    fun getAllContactsStream(): Flow<List<Contact>>
    fun getContactStream(id:Int): Flow<Contact?>
    suspend fun insertContact(contact: Contact):Long
    suspend fun deleteContact(contact: Contact)
    suspend fun updateContact(contact: Contact)

    suspend fun addContactScoundrelLink(contactsScoundrelsJoin: ContactsScoundrelsJoin)
    suspend fun removeContactScoundrelLink(scoundrelId:Int)
    suspend fun removeScoundrelContactLink(contactId: Int)
    suspend fun removeSingleContactScoundrelLink(scoundrelId: Int, contactId: Int)

    fun getScoundrelWithContacts(id:Int):Flow<List<ScoundrelWithContacts>>
    fun getScoundrelWithContactsAndRank(id:Int): Flow<List<ContactAndRank>>

    fun getContactWithScoundrels(id:Int):Flow<List<ContactWithScoundrels>>
    fun getContactWithScoundrelAndRank(id: Int): Flow<List<ScoundrelAndRank>>

    fun getAllCrewsStream(): Flow<List<Crew>>
    fun getCrewStream(id: String): Flow<Crew?>
    suspend fun updateCrew(crew: Crew)
    suspend fun deleteCrew(crew: Crew)
    suspend fun insertCrew(crew: Crew):Long

    fun getCrewWithScoundrels(crew: String): Flow<List<CrewWithScoundrels>>

    suspend fun addContactCrewLink(contactsCrewsJoin: ContactsCrewsJoin)
    suspend fun removeContactCrewLink(crewName: String)
    suspend fun removeCrewContactLink(contactId: Int)
    suspend fun removeSingleContactCrewLink(crewName: String, contactId: Int)

    fun getCrewWithContacts(name:String):Flow<List<CrewWithContacts>>
    fun getCrewWithContactsAndRank(name: String): Flow<List<ContactAndRank>>

    fun getContactWithCrews(id:Int):Flow<List<ContactWithCrews>>
    fun getContactWithCrewAndRank(id: Int): Flow<List<CrewAndRank>>




    fun getAllNotesStream(): Flow<List<Note>>
    fun getNoteStream(id:Int): Flow<Note?>
    suspend fun insertNote(note: Note)
    suspend fun deleteNote(note: Note)
    suspend fun updateNote(note: Note)

}