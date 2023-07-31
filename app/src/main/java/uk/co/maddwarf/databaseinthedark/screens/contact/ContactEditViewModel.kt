package uk.co.maddwarf.databaseinthedark.screens.contact

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import uk.co.maddwarf.databaseinthedark.data.DatabaseInTheDarkRepository
import uk.co.maddwarf.databaseinthedark.model.ContactDetails
import uk.co.maddwarf.databaseinthedark.model.ScoundrelDetails
import uk.co.maddwarf.databaseinthedark.model.toContact
import uk.co.maddwarf.databaseinthedark.model.toContactDetails
import uk.co.maddwarf.databaseinthedark.model.toScoundrelDetails
import uk.co.maddwarf.databaseinthedark.screens.scoundrel.IntermediateScoundrelUiState
import uk.co.maddwarf.databaseinthedark.screens.scoundrel.ScoundrelDetailsDestination
import uk.co.maddwarf.databaseinthedark.screens.scoundrel.ScoundrelEditUiState
import uk.co.maddwarf.databaseinthedark.screens.scoundrel.ScoundrelEditViewModel

class ContactEditViewModel(
    savedStateHandle: SavedStateHandle,
    val databaseInTheDarkRepository: DatabaseInTheDarkRepository
) :
    ViewModel() {

    private val contactId: Int =
        checkNotNull(savedStateHandle[ContactEditDestination.itemIdArg])

    var editUiState: StateFlow<ContactEditUiState> =
        databaseInTheDarkRepository.getContactStream(contactId)
            .filterNotNull()
            .map {
                ContactEditUiState(contactDetails = it.toContactDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ContactEditUiState()
            )

    var intermediateContactUiState by mutableStateOf(IntermediateContactUiState())
        private set

suspend fun saveItem(){
    //TODO
    databaseInTheDarkRepository.updateContact(contact = intermediateContactUiState.intermediateContactDetails.toContact())

}

    fun initialise() {
        updateIntermediateUiState(editUiState.value.contactDetails)
    }


    fun updateIntermediateUiState(contactDetails: ContactDetails){
        Log.d(
            "VM UPDATE",
            intermediateContactUiState.intermediateContactDetails.toString()
        )
        intermediateContactUiState = IntermediateContactUiState(
            intermediateContactDetails = contactDetails,
            isEntryValid = validateInput(contactDetails)
        )
    }
    private fun validateInput(uiState: ContactDetails = intermediateContactUiState.intermediateContactDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() //&& crew.isNotBlank()
        }
    }



    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
    }//end View model

data class ContactEditUiState(
    var contactDetails: ContactDetails = ContactDetails()
)

data class IntermediateContactUiState(
    var intermediateContactDetails: ContactDetails = ContactDetails(),
    var isEntryValid: Boolean = false
)