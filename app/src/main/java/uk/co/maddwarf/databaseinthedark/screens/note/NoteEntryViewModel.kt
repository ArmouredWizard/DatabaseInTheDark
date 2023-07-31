package uk.co.maddwarf.databaseinthedark.screens.note

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import uk.co.maddwarf.databaseinthedark.data.DatabaseInTheDarkRepository
import uk.co.maddwarf.databaseinthedark.model.NoteDetails
import uk.co.maddwarf.databaseinthedark.model.toNote

class NoteEntryViewModel(private val databaseInTheDarkRepository: DatabaseInTheDarkRepository ): ViewModel() {

    var noteUiState by mutableStateOf(NoteUiState())
        private set

    fun updateUiState(noteDetails: NoteDetails) {
        noteUiState =
            NoteUiState(noteDetails = noteDetails, isEntryValid = validateInput(noteDetails))
    }

    private fun validateInput(uiState: NoteDetails = noteUiState.noteDetails): Boolean {
        return with(uiState) {
            title.isNotBlank()
        }
    }

    suspend fun saveItem(){
        Log.d("SAVING", noteUiState.noteDetails.title)
        if (validateInput()){
            Log.d("VALID", "input")
            databaseInTheDarkRepository.insertNote(noteUiState.noteDetails.toNote())
        }
    }//end save
}//end viewModel

data class NoteUiState(
    val noteDetails: NoteDetails = NoteDetails(),
    val isEntryValid: Boolean = false
)