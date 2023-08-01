package uk.co.maddwarf.databaseinthedark.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uk.co.maddwarf.databaseinthedark.data.relations.ContactsCrewsJoin
import uk.co.maddwarf.databaseinthedark.data.relations.ContactsCrewsJoinDao
import uk.co.maddwarf.databaseinthedark.data.relations.ContactsScoundrelsJoin
import uk.co.maddwarf.databaseinthedark.data.relations.ContactsScoundrelsJoinDao
import uk.co.maddwarf.databaseinthedark.data.relations.Converters

@Database(
    entities = [
        Scoundrel::class,
        Contact::class,
        ContactsScoundrelsJoin::class,
        Crew::class,
        Note::class,
        ContactsCrewsJoin::class
    ],
    version = 16,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class DatabaseInTheDarkDatabase : RoomDatabase() {
    abstract fun scoundrelDao(): ScoundrelDao
    abstract fun contactDao(): ContactDao
    abstract fun contactsScoundrelsJoinDao(): ContactsScoundrelsJoinDao
    abstract fun crewDao(): CrewDao
    abstract fun noteDao():NoteDao
    abstract fun contactsCrewsJoinDao(): ContactsCrewsJoinDao

    companion object {
        @Volatile
        private var Instance: DatabaseInTheDarkDatabase? = null
        fun getDatabase(context: Context): DatabaseInTheDarkDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    DatabaseInTheDarkDatabase::class.java,
                    "item_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}