package uk.co.maddwarf.databaseinthedark.data.relations

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactsCrewsJoinDao{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addContactCrewLink(contactsCrewsJoin: ContactsCrewsJoin)

    @Transaction
    @Query("SELECT * FROM crews where crewId = :id")
    fun getCrewWithContacts(id: Int): Flow<List<CrewWithContacts>>

    @Transaction
    @Query("SELECT * FROM contacts where contactId = :id")
    fun getContactWithCrews(id:Int): Flow<List<ContactWithCrews>>

    @Query("DELETE from ContactsCrewsJoin WHERE crewId = :id")
    suspend fun removeContactCrewLink(id: Int)

    @Query("DELETE from ContactsCrewsJoin WHERE contactId = :contactId")
    suspend fun removeCrewContactLink(contactId: Int)

    @Transaction
    @Query("DELETE from ContactsCrewsJoin WHERE crewId = :crewId AND contactId = :contactId")
    suspend fun removeSingleContactCrewLink(crewId:Int, contactId: Int)

    //testing
    @Transaction
    @Query("select * from crews inner join ContactsCrewsJoin" +
            " on crews.crewId = ContactsCrewsJoin.crewId\n" +
            "inner join contacts on contacts.contactId = ContactsCrewsJoin.contactId " +
            "WHERE crews.crewId = :crewId")
    fun getCrewWithContactsAndRank(crewId: Int): Flow<List<ContactAndRank>>

    @Transaction
    @Query("select * from contacts inner join ContactsCrewsJoin" +
            " on contacts.contactId = ContactsCrewsJoin.contactId\n" +
            "inner join crews on crews.crewId = ContactsCrewsJoin.crewId " +
            "WHERE contacts.contactId = :id")
    fun getContactWithCrewAndRank(id: Int): Flow<List<CrewAndRank>>


}