package uk.co.maddwarf.databaseinthedark.screens.crew

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
import uk.co.maddwarf.databaseinthedark.data.Contact
import uk.co.maddwarf.databaseinthedark.data.DatabaseInTheDarkRepository
import uk.co.maddwarf.databaseinthedark.model.toCrewDetails
import uk.co.maddwarf.databaseinthedark.data.Crew
import uk.co.maddwarf.databaseinthedark.data.relations.ContactAndRank
import uk.co.maddwarf.databaseinthedark.data.relations.ContactsCrewsJoin
import uk.co.maddwarf.databaseinthedark.data.relations.ContactsScoundrelsJoin
import uk.co.maddwarf.databaseinthedark.model.CrewDetails
import uk.co.maddwarf.databaseinthedark.model.Rank
import uk.co.maddwarf.databaseinthedark.model.toCrew
import uk.co.maddwarf.databaseinthedark.screens.scoundrel.ContactListState
import uk.co.maddwarf.databaseinthedark.screens.scoundrel.ScoundrelContactsState
import uk.co.maddwarf.databaseinthedark.screens.scoundrel.ScoundrelEditViewModel

class CrewEditViewModel(
    savedStateHandle: SavedStateHandle,
    val databaseInTheDarkRepository: DatabaseInTheDarkRepository,
) : ViewModel() {

    private val crewId: Int = checkNotNull(savedStateHandle[CrewEditDestination.itemIdArg])

    var crewEditUiState: StateFlow<CrewEditUiState> =
        databaseInTheDarkRepository.getCrewStream(crewId)
            .filterNotNull()
            .map {
                CrewEditUiState(crewDetails = it.toCrewDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CrewEditUiState()
            )

    val crewTypeState: StateFlow<CrewTypeState> =
        databaseInTheDarkRepository.getAllCrewsStream().map{ CrewTypeState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CrewTypeState(listOf())
            )

    val crewContactsState: StateFlow<CrewContactsState> =
        databaseInTheDarkRepository.getCrewWithContactsAndRank(crewId)
            .map { CrewContactsState(crewContactsList = it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CrewContactsState()
            )

    var intermediateCrewUiState by mutableStateOf(IntermediateCrewUiState(crewEditUiState.value.crewDetails))
        private set

    fun initialise() {
        intermediateCrewUiState =
            IntermediateCrewUiState(intermediateCrewDetails = crewEditUiState.value.crewDetails)
    }

    fun updateIntermediateUiState(crewDetails: CrewDetails) {
        intermediateCrewUiState = IntermediateCrewUiState(
            intermediateCrewDetails = crewDetails
        )
    }

    val contactListState: StateFlow<ContactListState> =
        databaseInTheDarkRepository.getAllContactsStream().map { ContactListState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ContactListState()
            )

    fun updateContactsUiState(contact: Contact, rank: Rank) {
        intermediateCrewUiState.intermediateCrewDetails.crewContactsList =
            intermediateCrewUiState.intermediateCrewDetails.crewContactsList + ContactAndRank( contact, rank)
    }

    fun updateContacts() {
        updateIntermediateUiState(
            crewDetails = intermediateCrewUiState.intermediateCrewDetails.copy(
                crewContactsList = crewContactsState.value.crewContactsList
            )
        )
        Log.d(
            "UPDATE CONTACTS VM",
            intermediateCrewUiState.intermediateCrewDetails.crewContactsList.toString()
        )
    }

    suspend fun saveItem() {

        val newCrew = intermediateCrewUiState.intermediateCrewDetails

        // if (validateInput()){
        if (true) {
            Log.d("VALID", "input")
            databaseInTheDarkRepository.updateCrew(newCrew.toCrew())

            //Contacts

            var contactExists = false
            var contactIndex = 0
            intermediateCrewUiState.intermediateCrewDetails.crewContactsList.forEach { crewContact ->
                contactExists = false
                contactListState.value.contactListFromRepo.forEach { repoContact ->
                    if (crewContact.contact.name == repoContact.name) {
                        contactExists = true
                        contactIndex = repoContact.contactId
                        Log.d("CONTACT", crewContact.contact.name + " Exists with ID $contactIndex")
                    }
                }

                if (!contactExists) {
                    Log.d("CONTACT", "NOT EXIST! Adding")
                    contactIndex =
                        databaseInTheDarkRepository.insertContact(crewContact.contact).toInt()
                    Log.d("CONTACT", "Added with ID $contactIndex")
                }

                Log.d("CONTACT INDEX", contactIndex.toString())
                Log.d("ADDING LINK", "ContactID: $contactIndex, Scoundrel ID: $crewId")
                databaseInTheDarkRepository.addContactCrewLink(
                    ContactsCrewsJoin(
                       contactId =  contactIndex,
                        crewId = crewId,
                        rank = crewContact.rank//todo enter real rank
                    )
                )
            }//end contactList for each

            var connected = false
            contactListState.value.contactListFromRepo.forEach { contactFromRepo ->
                connected = false
                intermediateCrewUiState.intermediateCrewDetails.crewContactsList.forEach { scoundrelContact ->
                    if (contactFromRepo.name == scoundrelContact.contact.name) {
                        connected = true
                    }
                }
                if (!connected) {
                    contactIndex = contactFromRepo.contactId
                    databaseInTheDarkRepository.removeSingleContactCrewLink(
                        contactId = contactIndex,
                        crewId = intermediateCrewUiState.intermediateCrewDetails.crewId
                    )
                }
            }


            //end contacts
        }
    }//end save item

    suspend fun deleteCrew(crew: CrewDetails){
        Log.d("DELETE from EDIT", crew.name)
        //   notesInTheNightRepository.deleteCrew(crew.toCrew()) //todo here and DISPLAY

        val scoundrelsToProcess = databaseInTheDarkRepository.getScoundrelsByCrew(crew.crewId)
        Log.d("TO DELETE", scoundrelsToProcess.toString())
        scoundrelsToProcess.forEach {scoundrel ->
            databaseInTheDarkRepository.updateScoundrel(scoundrel.copy(crewId = 0))//todo
        }
        databaseInTheDarkRepository.deleteCrew(crew.toCrew()) //todo FIX CRASH
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}//end viewModel

data class CrewEditUiState(
    var crewDetails: CrewDetails = CrewDetails(),
)

data class IntermediateCrewUiState(
    var intermediateCrewDetails: CrewDetails = CrewDetails()
)

data class CrewTypeState(
    var crewsFromRepo: List<Crew>
)

data class CrewContactsState(
    var crewContactsList: List<ContactAndRank> = listOf()
)



