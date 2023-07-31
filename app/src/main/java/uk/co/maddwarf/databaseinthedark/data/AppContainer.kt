package uk.co.maddwarf.databaseinthedark.data

import android.content.Context

interface AppContainer{
    val dataBaseInTheDarkRepository:DatabaseInTheDarkRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [ItemsRepository]
     */
    override val dataBaseInTheDarkRepository: DatabaseInTheDarkRepository by lazy {
        OfflineDatabaseInTheDarkRepository(
            DatabaseInTheDarkDatabase.getDatabase(context).scoundrelDao(),
         //   DatabaseInTheDarkDatabase.getDatabase(context).defaultValueDao(),
            DatabaseInTheDarkDatabase.getDatabase(context).contactDao(),
            DatabaseInTheDarkDatabase.getDatabase(context).contactsScoundrelsJoinDao(),
            DatabaseInTheDarkDatabase.getDatabase(context).crewDao(),
            DatabaseInTheDarkDatabase.getDatabase(context).noteDao(),
            DatabaseInTheDarkDatabase.getDatabase(context).contactsCrewsJoinDao()
        )
    }
}