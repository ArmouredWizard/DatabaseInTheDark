package uk.co.maddwarf.databaseinthedark.screens.contact

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import uk.co.maddwarf.databaseinthedark.data.DatabaseInTheDarkRepository
import uk.co.maddwarf.databaseinthedark.model.ContactDetails
import uk.co.maddwarf.databaseinthedark.model.ScoundrelDetails
import uk.co.maddwarf.databaseinthedark.model.toContact
import uk.co.maddwarf.databaseinthedark.screens.scoundrel.ScoundrelEntryUiState

class ContactEntryViewModel(val databaseInTheDarkRepository: DatabaseInTheDarkRepository) :
    ViewModel() {

        var contactEntryUiState by mutableStateOf(ContactEntryUiState())

    fun updateUiStateEntry(contactDetails: ContactDetails) {
        contactEntryUiState =
            ContactEntryUiState(
                contactDetails = contactDetails,
                isEntryValid = validateInput(contactDetails)
            )
    }

    private fun validateInput(uiState: ContactDetails = contactEntryUiState.contactDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }

    suspend fun saveItem(){
        databaseInTheDarkRepository.insertContact(contact = contactEntryUiState.contactDetails.toContact())
    }




    }//end view model

data class ContactEntryUiState(
    val contactDetails: ContactDetails = ContactDetails(),
    val isEntryValid: Boolean = false
)