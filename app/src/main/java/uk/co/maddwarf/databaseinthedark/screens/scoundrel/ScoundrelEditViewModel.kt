package uk.co.maddwarf.databaseinthedark.screens.scoundrel

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
import uk.co.maddwarf.databaseinthedark.data.Crew
import uk.co.maddwarf.databaseinthedark.data.DatabaseInTheDarkRepository
import uk.co.maddwarf.databaseinthedark.data.Scoundrel
import uk.co.maddwarf.databaseinthedark.data.relations.ContactsScoundrelsJoin
import uk.co.maddwarf.databaseinthedark.data.relations.ContactAndRank
import uk.co.maddwarf.databaseinthedark.model.CrewDetails
import uk.co.maddwarf.databaseinthedark.model.Rank
import uk.co.maddwarf.databaseinthedark.model.ScoundrelDetails
import uk.co.maddwarf.databaseinthedark.model.toCrew
import uk.co.maddwarf.databaseinthedark.model.toScoundrel
import uk.co.maddwarf.databaseinthedark.model.toScoundrelDetails


class ScoundrelEditViewModel(
    savedStateHandle: SavedStateHandle,
    val databaseInTheDarkRepository: DatabaseInTheDarkRepository
) :
    ViewModel() {

    private val scoundrelId: Int =
        checkNotNull(savedStateHandle[ScoundrelEditDestination.itemIdArg])


    var editUiState: StateFlow<ScoundrelEditUiState> =
        databaseInTheDarkRepository.getScoundrelStream(scoundrelId)
            .filterNotNull()
            .map {
                ScoundrelEditUiState(scoundrelDetails = it.toScoundrelDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ScoundrelEditUiState()
            )

    var intermediateScoundrelUiState by mutableStateOf(IntermediateScoundrelUiState())
        private set

    val scoundrelListFromRepoState: StateFlow<ScoundrelListFromRepoState> =
        databaseInTheDarkRepository.getAllScoundrelsStream().map { ScoundrelListFromRepoState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ScoundrelListFromRepoState()
            )

    val editCrewUiState: StateFlow<EditCrewUiState> =
        databaseInTheDarkRepository.getAllCrewsStream().map { EditCrewUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = EditCrewUiState()
            )

    val contactListState: StateFlow<ContactListState> =
        databaseInTheDarkRepository.getAllContactsStream().map { ContactListState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ContactListState()
            )

    val scoundrelContactsState: StateFlow<ScoundrelContactsState> =
        databaseInTheDarkRepository.getScoundrelWithContactsAndRank(scoundrelId)
            .map { ScoundrelContactsState(scoundrelContactsList = it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ScoundrelContactsState()
            )

    fun initialise() {
        updateIntermediateUiState(editUiState.value.scoundrelDetails)
    }

    fun updateContacts() {
        updateIntermediateUiState(
            scoundrelDetails = intermediateScoundrelUiState.intermediateScoundrelDetails.copy(
                contactsList = scoundrelContactsState.value.scoundrelContactsList//todo crashes empty list. why no list???
            )
        )
        Log.d(
            "UPDATE CONTACTS VM",
            intermediateScoundrelUiState.intermediateScoundrelDetails.contactsList.toString()
        )
    }

    fun updateIntermediateUiState(scoundrelDetails: ScoundrelDetails) {
        Log.d(
            "VM UPDATE",
            intermediateScoundrelUiState.intermediateScoundrelDetails.contactsList.toString()
        )
        intermediateScoundrelUiState = IntermediateScoundrelUiState(
            intermediateScoundrelDetails = scoundrelDetails,
            isEntryValid = validateInput(scoundrelDetails)
        )
    }

    private fun validateInput(uiState: ScoundrelDetails = intermediateScoundrelUiState.intermediateScoundrelDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && playbook.isNotBlank() //&& crew.isNotBlank()
        }
    }

    fun updateContactsUiState(contact: Contact, rank: Rank) {
        intermediateScoundrelUiState.intermediateScoundrelDetails.contactsList =
            intermediateScoundrelUiState.intermediateScoundrelDetails.contactsList + ContactAndRank( contact, rank)
    }

    fun doNewContact(contactName: String, rank: Rank) {
        Log.d("CONTACT", contactName)
        val newList =
            (intermediateScoundrelUiState.intermediateScoundrelDetails.contactsList + ContactAndRank(Contact(
                contactId = 0,
                name = contactName
            ), rank))
        Log.d("NEW LIST", newList.toString())
        updateIntermediateUiState(
            intermediateScoundrelUiState.intermediateScoundrelDetails.copy(
                contactsList = newList
            )
        )
    }

    suspend fun saveItem() {
        if (validateInput()) {

            Log.d(
                "SAVE CONTACTS",
                intermediateScoundrelUiState.intermediateScoundrelDetails.contactsList.toString()
            )
            Log.d(
                "SAVE ABILITY",
                intermediateScoundrelUiState.intermediateScoundrelDetails.specialAbilities.size.toString()
            )
            // val scoundrelId =
            databaseInTheDarkRepository.updateScoundrel(intermediateScoundrelUiState.intermediateScoundrelDetails.toScoundrel())

            var crewExists = false
           // var thisCrewId = 0
            editCrewUiState.value.crewList.forEach {
                if (it.crewName == intermediateScoundrelUiState.intermediateScoundrelDetails.crew) {
                    crewExists = true
                   // thisCrewId = it.crewId
                }
            }
            if (!crewExists) {
                databaseInTheDarkRepository.insertCrew(
                    CrewDetails(
                        name = intermediateScoundrelUiState.intermediateScoundrelDetails.crew
                    ).toCrew()

                ).toInt()
            }

            Log.d(
                "SD",
                intermediateScoundrelUiState.intermediateScoundrelDetails.contactsList.toString()
            )

            var contactExists = false
            var contactIndex = 0
            intermediateScoundrelUiState.intermediateScoundrelDetails.contactsList.forEach { scoundrelContact ->
                contactExists = false
                contactListState.value.contactListFromRepo.forEach { repoContact ->
                    if (scoundrelContact.contact.name == repoContact.name) {
                        contactExists = true
                        contactIndex = repoContact.contactId
                        Log.d("CONTACT", scoundrelContact.contact.name + " Exists with ID $contactIndex")
                    }
                }

                if (!contactExists) {
                    Log.d("CONTACT", "NOT EXIST! Adding")
                    contactIndex =
                        databaseInTheDarkRepository.insertContact(scoundrelContact.contact).toInt()
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

            var connected = false
            contactListState.value.contactListFromRepo.forEach { contactFromRepo ->
                connected = false
                intermediateScoundrelUiState.intermediateScoundrelDetails.contactsList.forEach { scoundrelContact ->
                    if (contactFromRepo.name == scoundrelContact.contact.name) {
                        connected = true
                    }
                }
                if (!connected) {
                    contactIndex = contactFromRepo.contactId
                    databaseInTheDarkRepository.removeSingleContactScoundrelLink(
                        contactId = contactIndex,
                        scoundrelId = intermediateScoundrelUiState.intermediateScoundrelDetails.scoundrelId
                    )
                }
            }

        }//end IF VALID INPUT
    }//end SaveItem

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}//end ViewModel

data class ScoundrelEditUiState(
    var scoundrelDetails: ScoundrelDetails = ScoundrelDetails()
)

data class IntermediateScoundrelUiState(
    var intermediateScoundrelDetails: ScoundrelDetails = ScoundrelDetails(),
    var isEntryValid: Boolean = false
)

data class ScoundrelListFromRepoState(
    var scoundrelFromEditRepo: List<Scoundrel> = listOf()
)

data class EditCrewUiState(
    var crewList: List<Crew> = listOf()
)

data class ScoundrelContactsState(
    var scoundrelContactsList: List<ContactAndRank> = listOf()
)
