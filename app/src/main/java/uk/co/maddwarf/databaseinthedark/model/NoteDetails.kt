package uk.co.maddwarf.databaseinthedark.model

import uk.co.maddwarf.databaseinthedark.data.Note


data class NoteDetails(
    val id: Int = 0,
    val title: String = "",
    val bodyText: String = "",
    val category: String = ""
)

fun NoteDetails.toNote(): Note = Note(
    id = id,
    title = title.trim(),
    bodyText = bodyText.trim(),
    category = category.trim()
)

fun Note.toNoteDetails(): NoteDetails = NoteDetails(
    id = id,
    title = title.trim(),
    bodyText = bodyText.trim(),
    category = category.trim()
)