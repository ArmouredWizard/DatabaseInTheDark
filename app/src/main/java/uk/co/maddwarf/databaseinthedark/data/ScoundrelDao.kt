package uk.co.maddwarf.databaseinthedark.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoundrelDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(scoundrel: Scoundrel):Long

    @Update
    suspend fun update(scoundrel: Scoundrel)

    @Delete
    suspend fun delete(scoundrel: Scoundrel)

    @Query("SELECT * from scoundrels WHERE scoundrelId = :id")
    fun getScoundrel(id:Int): Flow<Scoundrel>

    @Query("SELECT * from scoundrels ORDER BY name ASC")
    fun getAllScoundrels(): Flow<List<Scoundrel>>

    @Query("SELECT * from scoundrels WHERE crew = :crewName")
    suspend fun getScoundrelsByCrew(crewName:String):List<Scoundrel>

}