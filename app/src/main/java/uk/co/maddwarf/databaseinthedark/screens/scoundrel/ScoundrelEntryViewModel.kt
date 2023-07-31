package uk.co.maddwarf.databaseinthedark.screens.scoundrel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import uk.co.maddwarf.databaseinthedark.data.Contact
import uk.co.maddwarf.databaseinthedark.data.Crew
import uk.co.maddwarf.databaseinthedark.data.DatabaseInTheDarkRepository
import uk.co.maddwarf.databaseinthedark.model.ScoundrelDetails
import uk.co.maddwarf.databaseinthedark.model.toScoundrel
import uk.co.maddwarf.databaseinthedark.data.Scoundrel
import uk.co.maddwarf.databaseinthedark.data.relations.ContactsScoundrelsJoin
import uk.co.maddwarf.databaseinthedark.data.relations.ContactAndRank
import uk.co.maddwarf.databaseinthedark.model.CrewDetails
import uk.co.maddwarf.databaseinthedark.model.Rank
import uk.co.maddwarf.databaseinthedark.model.toCrew

class ScoundrelEntryViewModel(val databaseInTheDarkRepository: DatabaseInTheDarkRepository) :
    ViewModel() {

    var scoundrelEntryUiState by mutableStateOf(ScoundrelEntryUiState())
        private set

    val crewUiState: StateFlow<CrewUiState> =
        databaseInTheDarkRepository.getAllCrewsStream().map{ CrewUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CrewUiState()
            )

    val scoundrelListState: StateFlow<ScoundrelListState> =
        databaseInTheDarkRepository.getAllScoundrelsStream().map { ScoundrelListState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ScoundrelListState()
            )

    val contactListState: StateFlow<ContactListState> =
        databaseInTheDarkRepository.getAllContactsStream().map{ContactListState(it)}
            .stateIn(
        scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ContactListState()
    )


    fun updateUiStateEntry(scoundrelDetails: ScoundrelDetails) {
        scoundrelEntryUiState =
            ScoundrelEntryUiState(
                scoundrelDetails = scoundrelDetails,
                isEntryValid = validateInput(scoundrelDetails)
            )
    }

    fun doNewContact(contactName: String, rank: Rank) {
        Log.d("CONTACT", contactName)
        val newList = (scoundrelEntryUiState.scoundrelDetails.contactsList + ContactAndRank(Contact(
            contactId = 0,
            name = contactName
        ), rank))
        Log.d("NEW LIST", newList.toString())
        updateUiStateEntry(scoundrelEntryUiState.scoundrelDetails.copy(contactsList = newList))
    }

    fun updateContactsUiState(contact: Contact, rank: Rank){
        scoundrelEntryUiState.scoundrelDetails.contactsList = scoundrelEntryUiState.scoundrelDetails.contactsList + ContactAndRank(contact, rank)
        Log.d("UPDATED CONTACT LIST", scoundrelEntryUiState.scoundrelDetails.contactsList.toString())
    }

    private fun validateInput(uiState: ScoundrelDetails = scoundrelEntryUiState.scoundrelDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && playbook.isNotBlank() //&& crew.isNotBlank()
        }
    }

    suspend fun saveItem() {
        if (validateInput()) {

            Log.d("SAVE", scoundrelEntryUiState.scoundrelDetails.contactsList.toString())
            val scoundrelId =
                databaseInTheDarkRepository.insertScoundrel(scoundrelEntryUiState.scoundrelDetails.toScoundrel())

            var crewExists = false
            crewUiState.value.crewList.forEach {
                if (it.crewName == scoundrelEntryUiState.scoundrelDetails.crew){
                    crewExists = true
                }
            }
            if (!crewExists) {
              databaseInTheDarkRepository.insertCrew(
                  CrewDetails(
                    name = scoundrelEntryUiState.scoundrelDetails.crew,
                ).toCrew()
              )
            }

            Log.d("SD", scoundrelEntryUiState.scoundrelDetails.contactsList.toString())

            var contactExists = false
            var contactIndex = 0
            scoundrelEntryUiState.scoundrelDetails.contactsList.forEach {scoundrelContact ->
                contactExists = false
                contactListState.value.contactListFromRepo.forEach {repoContact ->
                    if (scoundrelContact.contact.name == repoContact.name){
                        contactExists = true
                        contactIndex = repoContact.contactId
                        Log.d("CONTACT", scoundrelContact.contact.name + " Exists with ID $contactIndex")
                    }
                }

                if (!contactExists){
                    Log.d("CONTACT", "NOT EXIST! Adding")
                    contactIndex = databaseInTheDarkRepository.insertContact(scoundrelContact.contact).toInt()
                    Log.d("CONTACT", "Added with ID $contactIndex")
                }

                Log.d("CONTACT INDEX", contactIndex.toString())
                Log.d("ADDING LINK", "ContactID: $contactIndex, Scoundrel ID: $scoundrelId")
                databaseInTheDarkRepository.addContactScoundrelLink(
                    ContactsScoundrelsJoin(
                        contactIndex.toInt(),
                        scoundrelId.toInt(),
                        scoundrelContact.rank//todo enter real rank
                    )
                )
            }//end contactList for each

        }//end IF VALID INPUT
    }//end SaveItem

    companion object {
        const val TIMEOUT_MILLIS = 5_000L
    }

}//end View Model

data class ScoundrelEntryUiState(
    val scoundrelDetails: ScoundrelDetails = ScoundrelDetails(),
    val isEntryValid: Boolean = false
)

data class ScoundrelListState(
    val scoundrelListFromRepo: List<Scoundrel> = listOf()
)

data class CrewUiState(
    val crewList:List<Crew> = listOf()
)

data class ContactListState(
    val contactListFromRepo: List<Contact> = listOf()
)

