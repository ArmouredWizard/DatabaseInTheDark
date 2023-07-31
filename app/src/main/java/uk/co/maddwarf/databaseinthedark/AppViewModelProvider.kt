/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.maddwarf.databaseinthedark

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import uk.co.maddwarf.databaseinthedark.screens.contact.ContactDetailsViewModel
import uk.co.maddwarf.databaseinthedark.screens.contact.ContactDisplayViewModel
import uk.co.maddwarf.databaseinthedark.screens.contact.ContactEditViewModel
import uk.co.maddwarf.databaseinthedark.screens.contact.ContactEntryViewModel
import uk.co.maddwarf.databaseinthedark.screens.crew.CrewDetailsViewModel
import uk.co.maddwarf.databaseinthedark.screens.crew.CrewDisplayViewModel
import uk.co.maddwarf.databaseinthedark.screens.crew.CrewEditViewModel
import uk.co.maddwarf.databaseinthedark.screens.crew.CrewEntryViewModel
import uk.co.maddwarf.databaseinthedark.screens.note.NoteDetailsViewModel
import uk.co.maddwarf.databaseinthedark.screens.note.NoteDisplayViewModel
import uk.co.maddwarf.databaseinthedark.screens.note.NoteEditViewModel
import uk.co.maddwarf.databaseinthedark.screens.note.NoteEntryViewModel
import uk.co.maddwarf.databaseinthedark.screens.scoundrel.ScoundrelDetailsViewModel
import uk.co.maddwarf.databaseinthedark.screens.scoundrel.ScoundrelDisplayViewModel
import uk.co.maddwarf.databaseinthedark.screens.scoundrel.ScoundrelEditViewModel
import uk.co.maddwarf.databaseinthedark.screens.scoundrel.ScoundrelEntryViewModel


object AppViewModelProvider {
    val Factory = viewModelFactory {

        //initializer for ScoundrelDisplayViewModel
        initializer {
            ScoundrelDisplayViewModel(databaseInTheDarkApplication().container.dataBaseInTheDarkRepository)
        }
        // Initializer for ScoundrelEntryViewModel
        initializer {
            ScoundrelEntryViewModel(databaseInTheDarkApplication().container.dataBaseInTheDarkRepository)
        }
        // Initializer for ScoundrelDetailsViewModel
        initializer {
            ScoundrelDetailsViewModel(
                this.createSavedStateHandle(),
                databaseInTheDarkApplication().container.dataBaseInTheDarkRepository
            )
        }
        // Initializer for ScoundrelEditViewModel
        initializer {
            ScoundrelEditViewModel(
                this.createSavedStateHandle(),
                databaseInTheDarkApplication().container.dataBaseInTheDarkRepository
            )
        }
/*
        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(notesInTheNightApplication().container.notesInTheNightRepository)
        }
*/

        // Initializer for NoteEntryViewModel
        initializer {
            NoteEntryViewModel(databaseInTheDarkApplication().container.dataBaseInTheDarkRepository)
        }
        // Initializer for NoteDisplayViewModel
        initializer {
            NoteDisplayViewModel(databaseInTheDarkApplication().container.dataBaseInTheDarkRepository)
        }
        // Initializer for NoteDetailsViewModel
        initializer {
            NoteDetailsViewModel(
                this.createSavedStateHandle(),
                databaseInTheDarkApplication().container.dataBaseInTheDarkRepository
            )
        }
       // Initializer for NoteEditViewModel
       initializer {
           NoteEditViewModel(
               this.createSavedStateHandle(),
               databaseInTheDarkApplication().container.dataBaseInTheDarkRepository
           )
       }

        // Initializer for CrewDisplayViewModel
        initializer {
            CrewDisplayViewModel(databaseInTheDarkApplication().container.dataBaseInTheDarkRepository)
        }
        // Initializer for CrewEntryViewModel
        initializer {
            CrewEntryViewModel(databaseInTheDarkApplication().container.dataBaseInTheDarkRepository)
        }
        // Initializer for CrewDetailsViewModel
        initializer {
            CrewDetailsViewModel(
                this.createSavedStateHandle(),
                databaseInTheDarkApplication().container.dataBaseInTheDarkRepository
            )
        }
        // Initializer for CrewEditViewModel
        initializer {
            CrewEditViewModel(
                this.createSavedStateHandle(),
                databaseInTheDarkApplication().container.dataBaseInTheDarkRepository
            )
        }
        /*
                //init for SettingsViewModel
                initializer {
                    SettingsViewModel(
                        notesInTheNightApplication().container.notesInTheNightRepository
                    )
                }
        */

        initializer {
            ContactDisplayViewModel(
                databaseInTheDarkApplication().container.dataBaseInTheDarkRepository
            )
        }

        initializer {
            ContactEntryViewModel(
                databaseInTheDarkApplication().container.dataBaseInTheDarkRepository
            )
        }

        initializer {
            ContactDetailsViewModel(
                this.createSavedStateHandle(),
                databaseInTheDarkApplication().container.dataBaseInTheDarkRepository
            )
        }

        initializer {
            ContactEditViewModel(
                this.createSavedStateHandle(),
                databaseInTheDarkApplication().container.dataBaseInTheDarkRepository
            )
        }
    }
}

fun CreationExtras.databaseInTheDarkApplication(): DatabaseInTheDarkApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as DatabaseInTheDarkApplication)
