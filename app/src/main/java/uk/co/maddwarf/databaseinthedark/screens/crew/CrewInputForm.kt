package uk.co.maddwarf.databaseinthedark.screens.crew

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uk.co.maddwarf.databaseinthedark.model.CrewDetails
import uk.co.maddwarf.databaseinthedark.screens.composables.TextEntryRowWithInfoIcon
import uk.co.maddwarf.databaseinthedark.screens.composables.TextEntryWithSpinner
import uk.co.maddwarf.databaseinthedark.R
import uk.co.maddwarf.databaseinthedark.data.Contact
import uk.co.maddwarf.databaseinthedark.data.relations.ContactAndRank
import uk.co.maddwarf.databaseinthedark.model.Rank
import uk.co.maddwarf.databaseinthedark.screens.scoundrel.AbilityItem
import uk.co.maddwarf.databaseinthedark.screens.composables.MyButton
import uk.co.maddwarf.databaseinthedark.screens.composables.TitleBlock
import uk.co.maddwarf.databaseinthedark.screens.composables.TraitDots
import uk.co.maddwarf.databaseinthedark.screens.composables.dialogs.AbilityEntryDialog
import uk.co.maddwarf.databaseinthedark.screens.composables.dialogs.ContactEntryDialog
import uk.co.maddwarf.databaseinthedark.screens.composables.dialogs.DeleteAbilityDialog
import uk.co.maddwarf.databaseinthedark.screens.composables.dialogs.RemoveContactDialog
import uk.co.maddwarf.databaseinthedark.screens.scoundrel.ContactItem

@Composable
fun CrewInputForm(
    crewDetails: CrewDetails,
    crewTypeList: List<String>,
    crewReputationList: List<String>,
    everyAbilityList: List<String>,
    everyContactList:List<Contact>,
    onValueChange: (CrewDetails) -> Unit,
    saveContactConnection: (Contact, Rank) -> Unit,
    ) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TextEntryRowWithInfoIcon(
            data = crewDetails.name,
            onValueChange = { onValueChange(crewDetails.copy(name = it)) },
            label = "Crew Name",
            infoText = "Choose a name for your Crew.\n" +
                    "This will help you to find and identify it."
        )
        TextEntryWithSpinner(
            textValue = crewDetails.type,
            label = "Type",
            infoText =  "Choose the Type of Crew.\n" +
                    "This describes the main activities your Crew is known for",
            onValueChange = { if (it.length <= 19) onValueChange(crewDetails.copy(type = it)) },
            itemList = crewTypeList
        )

        TextEntryWithSpinner(
            textValue = crewDetails.reputation,
            label = "Reputation",
            infoText = "Choose the type of Reputation this Crew has.\n" + "",
            itemList = crewReputationList,
            onValueChange = { onValueChange(crewDetails.copy(reputation = it)) },
            )

        TextEntryRowWithInfoIcon(
            data = crewDetails.hq,
            onValueChange = { onValueChange(crewDetails.copy(hq = it)) },
            label = "HQ",
            infoText = "Choose a Location for your Crew's Headquarters.\n" +
                    ""
        )
        TextEntryRowWithInfoIcon(
            data = crewDetails.huntingGrounds,
            onValueChange = { onValueChange(crewDetails.copy(huntingGrounds = it)) },
            label = "Hunting Grounds",
            infoText = "Choose a Hunting Ground for your Crew.\n" +
                    "This is where the Crew touts for business, and gains most of their work.\n" +
                    "It does not have to be in the same place as their HQ"
        )
        val strong = if (crewDetails.holdIsStrong) {
            "Strong"
        } else {
            "Weak"
        }
        TraitDots(
            traitName = "Rep",
            traitValue = crewDetails.rep.toInt(),
            maxValue = 12,
            onDotClicked = { onValueChange(crewDetails.copy(rep = it.toString())) },
            infoText = stringResource(R.string.reputation_info)  //todo expand on this
        )
        TraitDots(
            traitName = "Heat",
            traitValue = crewDetails.heat.toInt(),
            maxValue = 6,
            onDotClicked = { onValueChange(crewDetails.copy(heat = it.toString())) },
            infoText = stringResource(R.string.heat_info) //todo expand
        )
        TraitDots(
            traitName = "Tier",
            traitValue = crewDetails.tier.toInt(),
            maxValue = 4,
            onDotClicked = { onValueChange(crewDetails.copy(tier = it.toString())) },
            infoText = stringResource(R.string.tier_info) //todo rewrite
        )

        var switchOn by remember {
            mutableStateOf(false)
        }
//Hold Switch
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Switch(
                checked = switchOn,
                onCheckedChange = { switchOn_ ->
                    switchOn = switchOn_
                    onValueChange(crewDetails.copy(holdIsStrong = (switchOn)))
                    Log.d("SWITCHING", "switchOn $switchOn, Strong: $strong")
                }
            )
            Text(text = if (switchOn) "Strong" else "Weak")
        }
        //end Hold Switch


        CrewAbilityBlock(
            onValueChange = onValueChange,
            crewDetails = crewDetails,
            everyAbilityList = everyAbilityList
        )
        Spacer(modifier = Modifier.height(5.dp))

        CrewContactsBlock(
            onValueChange = onValueChange,
            crewDetails = crewDetails,
            everyContactList = everyContactList,
            saveContactConnection = saveContactConnection
        )


    }//end column
}//end CrewEntryForm

@Composable
private fun CrewAbilityBlock(
    onValueChange: (CrewDetails) -> Unit,
    crewDetails: CrewDetails,
    everyAbilityList: List<String>
) {
    var newAbility by remember { mutableStateOf("") }
    var showAbilityDialog by remember { mutableStateOf(false) }
    fun abilityDialogClick() {
        showAbilityDialog = !showAbilityDialog
    }

    fun onAbilityChange(ability: String) {
        newAbility = ability
    }

    fun doNewAbility(ability: String) {
        Log.d("ACCEPTED", ability)
        showAbilityDialog = false
        onValueChange(
            crewDetails.copy(
                crewAbilities = (crewDetails.crewAbilities + listOf(
                    ability.trim()
                )).distinct()
            )
        )
        newAbility = ""
    }

    var showDeleteAbilityDialog by remember { mutableStateOf(false) }
    var chosenAbility by remember { mutableStateOf("") }
    fun abilityDeleteDialogClick(ability: String) {
        chosenAbility = ability
        showDeleteAbilityDialog = !showDeleteAbilityDialog
    }

    fun doDeleteAbility(ability: String) {
        Log.d("TO DELETE", ability)
        showDeleteAbilityDialog = false
        val newAbilityList = mutableListOf<String>()
        crewDetails.crewAbilities.forEach {
            if (it != ability) newAbilityList.add(it)
        }
        onValueChange(
            crewDetails.copy(
                crewAbilities = newAbilityList
            )
        )
    }

    val abilityList = crewDetails.crewAbilities
    TitleBlock(title = "", text = "Special Abilities")
    abilityList.forEach { it ->
        AbilityItem(
            ability = it,
            displayDeleteDialog = { abilityDeleteDialogClick(it) }
        )
    }
    MyButton(
        onClick = { abilityDialogClick() },
        text = "Add new Ability",
        leadingIcon = Icons.Default.Add
    )
    if (showAbilityDialog) {
        AbilityEntryDialog(
            ability = newAbility,
            onDismiss = { showAbilityDialog = !showAbilityDialog },
            onAccept = { doNewAbility(it) },
            onValueChange = { onAbilityChange(it) },
            abilityList = everyAbilityList
        )
    }
    if (showDeleteAbilityDialog) {
        DeleteAbilityDialog(
            ability = chosenAbility,
            onDismiss = { showDeleteAbilityDialog = false },
            onAccept = { doDeleteAbility(chosenAbility) }
        )
    }
}//end Crew Ability Block

@Composable
fun CrewContactsBlock(
    onValueChange: (CrewDetails) -> Unit,
    crewDetails: CrewDetails,
    everyContactList: List<Contact>,
    saveContactConnection: (Contact, Rank) -> Unit
){
    var newContactName by remember { mutableStateOf("") }
    var showContactDialog by remember { mutableStateOf(false) }
    var everyContactNameList: MutableList<String> = mutableListOf()
    everyContactList.forEach {
        everyContactNameList.add(it.name)
    }
    everyContactNameList = everyContactNameList.distinct().toMutableList()

    fun contactDialogClick() {
        showContactDialog = !showContactDialog
    }

    fun onContactChange(contactName: String) {
        newContactName = contactName
    }

    fun doNewContact(contactName: String, rank: Rank) {
        Log.d("ACCEPTED", contactName)
        showContactDialog = false

        var newContact: Contact = Contact(contactId = 0, name = contactName /*description = ""*/)
        everyContactList.forEach {
            newContact = if (it.name == contactName) {
                it
            } else {
                Contact(contactId = 0, name = contactName /*description = ""*/)
            }
        }
        saveContactConnection(newContact, rank)
        //onContactChange(contactName)
        newContactName = ""
    }

    var showRemoveContactDialog by remember { mutableStateOf(false) }
    var chosenContact by remember { mutableStateOf("") }

    fun contactDeleteDialogClick(contact: String) {
        chosenContact = contact
        showRemoveContactDialog = !showRemoveContactDialog
    }

    fun doRemoveContact(contactName: String) {
        Log.d("TO REMOVE", contactName)
        showRemoveContactDialog = false
        val newContactList = mutableListOf<ContactAndRank>()
        crewDetails.crewContactsList.forEach {
            if (it.contact.name != contactName) newContactList.add(it)
        }
        onValueChange(
            crewDetails.copy(
                crewContactsList = newContactList
            )
        )
        Log.d("NEW CONTACT LIST", newContactList.toString())
    }

    if (showRemoveContactDialog) {
        RemoveContactDialog(
            contact = chosenContact,
            onDismiss = { showRemoveContactDialog = false },
            onAccept = { doRemoveContact(chosenContact) }
        )
    }

    val contactsList = crewDetails.crewContactsList
    TitleBlock(title = "", text = "Contacts")
    contactsList.forEach { it ->
        ContactItem(
            contact = it,
            displayDeleteContactDialog = { contactDeleteDialogClick(it) },//todo add delete code
        )
    }
    MyButton(onClick = { contactDialogClick() }, text = "Add new Contact", leadingIcon = Icons.Default.Add)
    if (showContactDialog) {
        ContactEntryDialog(
            contactName = newContactName,
            onDismiss = { showContactDialog = !showContactDialog },
            onAccept = {contactName, rank->  doNewContact(contactName, rank) },
            onValueChange = { onContactChange(it) },
            contactList = everyContactNameList
        )
    }



}// end crewContactsBlock