package uk.co.maddwarf.databaseinthedark.model

import uk.co.maddwarf.databaseinthedark.data.Contact

data class ContactDetails (
    val contactId:Int = 0,
    val name: String = ""
)

//extension functions

fun ContactDetails.toContact(): Contact = Contact(
    contactId = contactId,
    name = name.trim()
)

fun Contact.toContactDetails(): ContactDetails = ContactDetails(
    contactId = contactId,
    name = name.trim()
)
