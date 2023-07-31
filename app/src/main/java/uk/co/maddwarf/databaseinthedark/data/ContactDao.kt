package uk.co.maddwarf.databaseinthedark.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(contact: Contact):Long

    @Update
    suspend fun update(contact: Contact)

    @Delete
    suspend fun delete(contact: Contact)

    @Query("SELECT * from contacts WHERE contactId = :id")
    fun getContact(id:Int): Flow<Contact>

    @Query("SELECT * from contacts ORDER BY name ASC")
    fun getAllContacts(): Flow<List<Contact>>




}