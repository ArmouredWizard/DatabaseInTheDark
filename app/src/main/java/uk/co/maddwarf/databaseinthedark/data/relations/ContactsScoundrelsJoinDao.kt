package uk.co.maddwarf.databaseinthedark.data.relations

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactsScoundrelsJoinDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addContactScoundrelLink(contactsScoundrelsJoin: ContactsScoundrelsJoin)

    //TODO MORE STUFF

    @Transaction
    @Query("SELECT * FROM scoundrels where scoundrelId = :id")
    fun getScoundrelWithContacts(id: Int): Flow<List<ScoundrelWithContacts>>

    @Transaction
    @Query("SELECT * FROM contacts where contactId = :id")
    fun getContactWithScoundrels(id:Int): Flow<List<ContactWithScoundrels>>

    @Query("DELETE from ContactsScoundrelsJoin WHERE scoundrelId = :scoundrelId")
    suspend fun removeContactScoundrelLink(scoundrelId: Int)

    @Query("DELETE from ContactsScoundrelsJoin WHERE contactId = :contactId")
    suspend fun removeScoundrelContactLink(contactId: Int)

    @Transaction
    @Query("DELETE from ContactsScoundrelsJoin WHERE scoundrelId = :scoundrelId AND contactId = :contactId")
    suspend fun removeSingleContactScoundrelLink(scoundrelId: Int, contactId: Int)

    //testing
    @Transaction
    @Query("select * from scoundrels inner join ContactsScoundrelsJoin" +
            " on scoundrels.scoundrelId = ContactsScoundrelsJoin.scoundrelId\n" +
            "inner join contacts on contacts.contactId = ContactsScoundrelsJoin.contactId " +
            "WHERE scoundrels.scoundrelId = :id")
    fun getScoundrelWithContactsAndRank(id: Int): Flow<List<ContactAndRank>>

    @Transaction
    @Query("select * from contacts inner join ContactsScoundrelsJoin" +
            " on contacts.contactId = ContactsScoundrelsJoin.contactId\n" +
            "inner join scoundrels on scoundrels.scoundrelId = ContactsScoundrelsJoin.scoundrelId " +
            "WHERE contacts.contactId = :id")
    fun getContactWithScoundrelAndRank(id: Int): Flow<List<ScoundrelAndRank>>

}