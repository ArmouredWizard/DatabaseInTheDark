package uk.co.maddwarf.databaseinthedark.screens.crew

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
import uk.co.maddwarf.databaseinthedark.data.relations.ContactAndRank
import uk.co.maddwarf.databaseinthedark.data.relations.ContactsCrewsJoin
import uk.co.maddwarf.databaseinthedark.data.relations.ContactsScoundrelsJoin
import uk.co.maddwarf.databaseinthedark.model.CrewDetails
import uk.co.maddwarf.databaseinthedark.model.Rank
import uk.co.maddwarf.databaseinthedark.model.toCrew

class CrewEntryViewModel(private val databaseInTheDarkRepository: DatabaseInTheDarkRepository): ViewModel() {

    var crewEntryUiState by mutableStateOf(CrewEntryUiState())
        private set

    fun updateUiState(crewDetails: CrewDetails) {
        crewEntryUiState =
            CrewEntryUiState(crewDetails = crewDetails, isEntryValid = validateInput(crewDetails))
    }

    val crewListEntryState: StateFlow<CrewListEntryState> =
        databaseInTheDarkRepository.getAllCrewsStream().map{ CrewListEntryState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CrewListEntryState(listOf())
            )

    val crewContactListState:StateFlow<CrewContactsListState> =
        databaseInTheDarkRepository.getAllContactsStream().map { CrewContactsListState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CrewContactsListState(listOf())
            )

    fun updateContactsUiState(contact: Contact, rank: Rank){
        crewEntryUiState.crewDetails.crewContactsList = crewEntryUiState.crewDetails.crewContactsList + ContactAndRank(contact, rank)
        Log.d("UPDATED CONTACT LIST", crewEntryUiState.crewDetails.crewContactsList.toString())
    }

    private fun validateInput(uiState: CrewDetails = crewEntryUiState.crewDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }

    suspend fun saveItem(){
        Log.d("SAVING", crewEntryUiState.crewDetails.name)
        if (validateInput()){
            Log.d("VALID", "input")
            databaseInTheDarkRepository.insertCrew(crewEntryUiState.crewDetails.toCrew())

            //start save contacts

            var contactExists = false
            var contactIndex = 0
            crewEntryUiState.crewDetails.crewContactsList.forEach {crewContact ->
                contactExists = false
                crewContactListState.value.contactsFromRepo.forEach {repoContact ->
                    if (crewContact.contact.name == repoContact.name){
                        contactExists = true
                        contactIndex = repoContact.contactId
                        Log.d("CONTACT", crewContact.contact.name + " Exists with ID $contactIndex")
                    }
                }

                if (!contactExists){
                    Log.d("CONTACT", "NOT EXIST! Adding")
                    contactIndex = databaseInTheDarkRepository.insertContact(crewContact.contact).toInt()
                    Log.d("CONTACT", "Added with ID $contactIndex")
                }

                Log.d("CONTACT INDEX", contactIndex.toString())
                Log.d("ADDING LINK", "ContactID: $contactIndex, Crew ID: ${crewEntryUiState.crewDetails.name}")
                databaseInTheDarkRepository.addContactCrewLink(
                    ContactsCrewsJoin(
                        contactId = contactIndex.toInt(),
                        crewName = crewEntryUiState.crewDetails.name,
                        rank = crewContact.rank
                    )
                )
            }//end contactList for each

            //end save contacts





        }

        //todo SAVE CONATCS

    }//end save

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}//end viewModel

data class CrewEntryUiState(
    val crewDetails: CrewDetails = CrewDetails(),
    val isEntryValid: Boolean = false
)

data class CrewListEntryState(
    val crewsFromRepo: List<Crew>
)

data class CrewContactsListState(
    val contactsFromRepo: List<Contact>
)

