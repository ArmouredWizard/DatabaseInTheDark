package uk.co.maddwarf.databaseinthedark.data

import kotlinx.coroutines.flow.Flow
import uk.co.maddwarf.databaseinthedark.data.relations.ContactsScoundrelsJoin
import uk.co.maddwarf.databaseinthedark.data.relations.ContactsScoundrelsJoinDao
import uk.co.maddwarf.databaseinthedark.data.relations.CrewWithScoundrels
import uk.co.maddwarf.databaseinthedark.data.relations.ScoundrelWithContacts
import uk.co.maddwarf.databaseinthedark.data.relations.ContactAndRank
import uk.co.maddwarf.databaseinthedark.data.relations.ContactWithCrews
import uk.co.maddwarf.databaseinthedark.data.relations.ContactWithScoundrels
import uk.co.maddwarf.databaseinthedark.data.relations.ContactsCrewsJoin
import uk.co.maddwarf.databaseinthedark.data.relations.ContactsCrewsJoinDao
import uk.co.maddwarf.databaseinthedark.data.relations.CrewAndRank
import uk.co.maddwarf.databaseinthedark.data.relations.CrewWithContacts
import uk.co.maddwarf.databaseinthedark.data.relations.ScoundrelAndRank


class OfflineDatabaseInTheDarkRepository(
    private val scoundrelDao: ScoundrelDao,
    private val contactDao: ContactDao,
    private val contactsScoundrelsJoinDao: ContactsScoundrelsJoinDao,
    private val crewDao: CrewDao,
    private val noteDao: NoteDao,
    private val contactsCrewsJoinDao: ContactsCrewsJoinDao

) : DatabaseInTheDarkRepository {
    override fun getAllScoundrelsStream(): Flow<List<Scoundrel>> = scoundrelDao.getAllScoundrels()
    override fun getScoundrelStream(id: Int): Flow<Scoundrel?> = scoundrelDao.getScoundrel(id)
    override suspend fun getScoundrelsByCrew(crewName: String): List<Scoundrel> =
        scoundrelDao.getScoundrelsByCrew(crewName)

    override suspend fun insertScoundrel(scoundrel: Scoundrel) = scoundrelDao.insert(scoundrel)
    override suspend fun deleteScoundrel(scoundrel: Scoundrel) = scoundrelDao.delete(scoundrel)
    override suspend fun updateScoundrel(scoundrel: Scoundrel) = scoundrelDao.update(scoundrel)

    override fun getAllContactsStream(): Flow<List<Contact>> = contactDao.getAllContacts()
    override fun getContactStream(id: Int): Flow<Contact?> = contactDao.getContact(id)
    override suspend fun insertContact(contact: Contact) = contactDao.insert(contact)
    override suspend fun deleteContact(contact: Contact) = contactDao.delete(contact)
    override suspend fun updateContact(contact: Contact) = contactDao.update(contact)

    override suspend fun addContactScoundrelLink(contactsScoundrelsJoin: ContactsScoundrelsJoin) =
        contactsScoundrelsJoinDao.addContactScoundrelLink(contactsScoundrelsJoin)

    override suspend fun removeContactScoundrelLink(scoundrelId: Int) =
        contactsScoundrelsJoinDao.removeContactScoundrelLink(scoundrelId)

    override suspend fun removeScoundrelContactLink(contactId: Int) =
        contactsScoundrelsJoinDao.removeScoundrelContactLink(contactId)

    override suspend fun removeSingleContactScoundrelLink(scoundrelId: Int, contactId: Int) =
        contactsScoundrelsJoinDao.removeSingleContactScoundrelLink(
            scoundrelId = scoundrelId,
            contactId = contactId
        )

    override fun getScoundrelWithContacts(id: Int): Flow<List<ScoundrelWithContacts>> =
        contactsScoundrelsJoinDao.getScoundrelWithContacts(id)

    override fun getScoundrelWithContactsAndRank(id:Int): Flow<List<ContactAndRank>> =
        contactsScoundrelsJoinDao.getScoundrelWithContactsAndRank(id)

    override fun getContactWithScoundrels(id: Int): Flow<List<ContactWithScoundrels>> =
        contactsScoundrelsJoinDao.getContactWithScoundrels(id)

    override fun getContactWithScoundrelAndRank(id: Int): Flow<List<ScoundrelAndRank>> =
        contactsScoundrelsJoinDao.getContactWithScoundrelAndRank(id)

    override fun getAllCrewsStream(): Flow<List<Crew>> = crewDao.getAllCrews()
    override fun getCrewStream(id: String): Flow<Crew?> = crewDao.getCrew(id)
    override suspend fun insertCrew(crew: Crew) = crewDao.insert(crew)
    override suspend fun deleteCrew(crew: Crew) = crewDao.delete(crew)
    override suspend fun updateCrew(crew: Crew) = crewDao.update(crew)
    override fun getCrewWithScoundrels(crew: String): Flow<List<CrewWithScoundrels>> =
        crewDao.getCrewWithScoundrels(crew)

    override suspend fun addContactCrewLink(contactsCrewsJoin: ContactsCrewsJoin) = contactsCrewsJoinDao.addContactCrewLink(contactsCrewsJoin)
    override suspend fun removeContactCrewLink(crewName: String)  = contactsCrewsJoinDao.removeContactCrewLink(crewName)
    override suspend fun removeCrewContactLink(contactId: Int)  = contactsCrewsJoinDao.removeCrewContactLink(contactId)
    override suspend fun removeSingleContactCrewLink(crewName: String, contactId: Int)  = contactsCrewsJoinDao.removeSingleContactCrewLink(name = crewName, contactId = contactId)
    override fun getCrewWithContacts(name: String): Flow<List<CrewWithContacts>>  = contactsCrewsJoinDao.getCrewWithContacts(name)
    override fun getCrewWithContactsAndRank(name: String): Flow<List<ContactAndRank>>  = contactsCrewsJoinDao.getCrewWithContactsAndRank(name)
    override fun getContactWithCrews(id: Int): Flow<List<ContactWithCrews>>  = contactsCrewsJoinDao.getContactWithCrews(id)
    override fun getContactWithCrewAndRank(id: Int): Flow<List<CrewAndRank>>  = contactsCrewsJoinDao.getContactWithCrewAndRank(id)


    override fun getAllNotesStream() = noteDao.getAllNotes()
    override fun getNoteStream(id:Int) = noteDao.getNote(id)
    override suspend fun insertNote(note: Note) = noteDao.insert(note)
    override suspend fun deleteNote(note: Note) = noteDao.delete(note)
    override suspend fun updateNote(note: Note) = noteDao.update(note)

}