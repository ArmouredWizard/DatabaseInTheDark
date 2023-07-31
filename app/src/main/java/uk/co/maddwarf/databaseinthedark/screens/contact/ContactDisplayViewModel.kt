package uk.co.maddwarf.databaseinthedark.screens.contact

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import uk.co.maddwarf.databaseinthedark.data.Contact
import uk.co.maddwarf.databaseinthedark.data.DatabaseInTheDarkRepository
import uk.co.maddwarf.databaseinthedark.data.Scoundrel

class ContactDisplayViewModel(val databaseInTheDarkRepository: DatabaseInTheDarkRepository) :
    ViewModel() {
    val contactDisplayUiState: StateFlow<ContactDisplayUiState> =
        databaseInTheDarkRepository.getAllContactsStream().map { ContactDisplayUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ContactDisplayUiState()
            )

    suspend fun deleteContact(contact: Contact) {
        Log.d("DELETE IN VM", contact.name+ " " + contact.contactId)
        databaseInTheDarkRepository.removeScoundrelContactLink(contact.contactId)
        databaseInTheDarkRepository.deleteContact(contact)
        //todo
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}//end viewModel

data class ContactDisplayUiState(
    val contactList: List<Contact> = listOf(),
)