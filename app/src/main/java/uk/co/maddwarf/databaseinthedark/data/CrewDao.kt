package uk.co.maddwarf.databaseinthedark.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import uk.co.maddwarf.databaseinthedark.data.relations.CrewWithScoundrels

@Dao
interface CrewDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(crew: Crew):Long

    @Update
    suspend fun update(crew: Crew)

    @Delete
    suspend fun delete(crew: Crew)

    @Query("SELECT * from crews WHERE crewId = :id")
    fun getCrew(id:Int): Flow<Crew>

    @Query("SELECT * from crews ORDER BY crewName ASC")
    fun getAllCrews(): Flow<List<Crew>>

    @Transaction
    @Query ("SELECT * FROM crews WHERE crewId = :crewId")
     fun getCrewWithScoundrels(crewId:Int):Flow<List<CrewWithScoundrels>>



}