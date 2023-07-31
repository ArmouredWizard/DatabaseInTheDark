package uk.co.maddwarf.databaseinthedark.screens.note

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
import uk.co.maddwarf.databaseinthedark.model.NoteDetails
import uk.co.maddwarf.databaseinthedark.model.toNote
import uk.co.maddwarf.databaseinthedark.model.toNoteDetails

class NoteEditViewModel(
    savedStateHandle: SavedStateHandle,
    val databaseInTheDarkRepository: DatabaseInTheDarkRepository,
) : ViewModel() {

    private val noteId: Int = checkNotNull(savedStateHandle[NoteEditDestination.itemIdArg])

    var editUiState: StateFlow<NoteEditUiState> =
        databaseInTheDarkRepository.getNoteStream(noteId)
            .filterNotNull()
            .map {
                NoteEditUiState(noteDetails = it.toNoteDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = NoteEditUiState()
            )

    var intermediateUiState by mutableStateOf(IntermediateUiState(editUiState.value.noteDetails))
        private set

    fun initialise() {
        intermediateUiState =
            IntermediateUiState(intermediateNoteDetails = editUiState.value.noteDetails)
    }

    fun updateIntermediateUiState(noteDetails: NoteDetails) {
        intermediateUiState = IntermediateUiState(
            intermediateNoteDetails = noteDetails
        )
    }

    suspend fun saveItem() {

        val newNote = intermediateUiState.intermediateNoteDetails

        // if (validateInput()){
        if (true) {
            Log.d("VALID", "input")
            databaseInTheDarkRepository.updateNote(newNote.toNote())
        }
    }

    suspend fun deleteNote(note: NoteDetails){
        databaseInTheDarkRepository.deleteNote(note.toNote())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}//end viewModel

data class NoteEditUiState(
    var noteDetails: NoteDetails = NoteDetails(),
)

data class IntermediateUiState(
    var intermediateNoteDetails: NoteDetails = NoteDetails()
)