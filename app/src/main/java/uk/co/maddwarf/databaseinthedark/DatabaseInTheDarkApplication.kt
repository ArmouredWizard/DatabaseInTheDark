package uk.co.maddwarf.databaseinthedark

import android.app.Application
import uk.co.maddwarf.databaseinthedark.data.AppContainer
import uk.co.maddwarf.databaseinthedark.data.AppDataContainer

class DatabaseInTheDarkApplication: Application(){
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}