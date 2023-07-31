package uk.co.maddwarf.databaseinthedark.screens.scoundrel

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.persistentListOf
import uk.co.maddwarf.databaseinthedark.data.Contact
import uk.co.maddwarf.databaseinthedark.data.Crew
import uk.co.maddwarf.databaseinthedark.data.relations.ContactAndRank
import uk.co.maddwarf.databaseinthedark.model.CrewDetails
import uk.co.maddwarf.databaseinthedark.model.Rank
import uk.co.maddwarf.databaseinthedark.model.ScoundrelDetails
import uk.co.maddwarf.databaseinthedark.model.toCrew
import uk.co.maddwarf.databaseinthedark.screens.composables.ActionBlock
import uk.co.maddwarf.databaseinthedark.screens.composables.CrewSpinner
import uk.co.maddwarf.databaseinthedark.screens.composables.MyButton
import uk.co.maddwarf.databaseinthedark.screens.composables.TextEntryRowWithInfoIcon
import uk.co.maddwarf.databaseinthedark.screens.composables.TextEntryWithSpinner
import uk.co.maddwarf.databaseinthedark.screens.composables.TitleBlock
import uk.co.maddwarf.databaseinthedark.screens.composables.dialogs.AbilityEntryDialog
import uk.co.maddwarf.databaseinthedark.screens.composables.dialogs.ContactEntryDialog
import uk.co.maddwarf.databaseinthedark.screens.composables.dialogs.DeleteAbilityDialog
import uk.co.maddwarf.databaseinthedark.screens.composables.dialogs.RemoveContactDialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoundrelInputForm(
    scoundrelDetails: ScoundrelDetails,
    modifier: Modifier = Modifier,
    onValueChange: (ScoundrelDetails) -> Unit = {},
    enabled: Boolean = true,
    crewList: List<Crew>,
    playbookList: List<String>,
    heritageList: List<String>,
    backgroundList: List<String>,
    everyAbilityList: List<String>,
    everyContactList: List<Contact>,
    saveContactConnection: (Contact, Rank) -> Unit,
    doNewContact: (String, Rank) -> Unit
) {
    fun circleClicker(aString: String, aIndex: Int) {
        when (aString) {
            "Hunt" -> onValueChange(scoundrelDetails.copy(hunt = aIndex.toString()))
            "Study" -> onValueChange(scoundrelDetails.copy(study = aIndex.toString()))
            "Survey" -> onValueChange(scoundrelDetails.copy(survey = aIndex.toString()))
            "Tinker" -> onValueChange(scoundrelDetails.copy(tinker = aIndex.toString()))
            "Finesse" -> onValueChange(scoundrelDetails.copy(finesse = aIndex.toString()))
            "Prowl" -> onValueChange(scoundrelDetails.copy(prowl = aIndex.toString()))
            "Skirmish" -> onValueChange(scoundrelDetails.copy(skirmish = aIndex.toString()))
            "Wreck" -> onValueChange(scoundrelDetails.copy(wreck = aIndex.toString()))
            "Attune" -> onValueChange(scoundrelDetails.copy(attune = aIndex.toString()))
            "Command" -> onValueChange(scoundrelDetails.copy(command = aIndex.toString()))
            "Consort" -> onValueChange(scoundrelDetails.copy(consort = aIndex.toString()))
            "Sway" -> onValueChange(scoundrelDetails.copy(sway = aIndex.toString()))
        }
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {

        TextEntryRowWithInfoIcon(
            data = scoundrelDetails.name,
            onValueChange = {
                if (it.length <= 19)
                    onValueChange(scoundrelDetails.copy(name = it))
            },
            label = "Name",
            infoText = "Enter your Scoundrel's Name.\n" +
                    "This can be a Nickname, Moniker, Handle or anything that identifies them."
        )

        TextEntryWithSpinner(
            textValue = scoundrelDetails.playbook,
            label = "Playbook",
            infoText = "Choose your Scoundrel's Playbook.\n" +
                    "This defines their XP Triggers, and initial access to Special Abilities.",
            onValueChange = { if (it.length <= 19) onValueChange(scoundrelDetails.copy(playbook = it)) },
            itemList = playbookList
        )

        //todo CREW
        var crewExpanded by remember { mutableStateOf(false) }
        var chosenCrew by remember {
            mutableStateOf(
                CrewDetails().toCrew()
            )
        }

        fun crewChooser(crew: Crew) {
            crewExpanded = false
            chosenCrew = crew
            onValueChange(scoundrelDetails.copy(crew = chosenCrew.crewName))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CrewSpinner(
                expanded = crewExpanded,
                onClick = { crewExpanded = !crewExpanded },
                list = crewList,
                chooser = ::crewChooser,
                report = chosenCrew
            )
            TextEntryRowWithInfoIcon(
                data = scoundrelDetails.crew,
                onValueChange = { if (it.length <= 19) onValueChange(scoundrelDetails.copy(crew = it)) },
                label = "Crew",
                infoText = "Choose your Scoundrel's Crew.\n" +
                        "This is the group that they do Heists with. Usually your fellow Players.",
                width = 0.85f
            )
        }

        TextEntryWithSpinner(
            textValue = scoundrelDetails.heritage,
            label = "Heritage",
            infoText = "Choose your Scoundrel's Heritage.\n" +
                    "Where does your Scoundrel hail from? Are they local, or from far-afield?",
            onValueChange = { if (it.length <= 19) onValueChange(scoundrelDetails.copy(heritage = it)) },
            itemList = heritageList
        )
        TextEntryWithSpinner(
            textValue = scoundrelDetails.background,
            label = "Background",
            infoText = "Choose your Scoundrel's Background.\n" +
                    "What was their upbringing like? What was their major influence?",
            onValueChange = { if (it.length <= 19) onValueChange(scoundrelDetails.copy(background = it)) },
            itemList = backgroundList
        )

        Box(
            modifier = modifier
                .fillMaxWidth(.9f)
                .background(color = Color.DarkGray)
        ) {
            Column(
                modifier = modifier
                    .padding(20.dp)
            ) {
                ActionBlock(
                    attribute = "Insight",
                    actions = persistentListOf(
                        Pair("Hunt", scoundrelDetails.hunt),
                        Pair("Study", scoundrelDetails.study),
                        Pair("Survey", scoundrelDetails.survey),
                        Pair("Tinker", scoundrelDetails.tinker)
                    ),
                    onRankClicked = { aString: String, aIndex: Int ->
                        circleClicker(aString, aIndex)
                    }
                )//end ActionBlock
                ActionBlock(
                    attribute = "Prowess",
                    actions = persistentListOf(
                        Pair("Finesse", scoundrelDetails.finesse),
                        Pair("Prowl", scoundrelDetails.prowl),
                        Pair("Skirmish", scoundrelDetails.skirmish),
                        Pair("Wreck", scoundrelDetails.wreck)
                    ),
                    onRankClicked = { aString: String, aIndex: Int ->
                        circleClicker(aString, aIndex)
                    }
                )//end ActionBlock
                ActionBlock(
                    attribute = "Resolve",
                    actions = persistentListOf(
                        Pair("Attune", scoundrelDetails.attune),
                        Pair("Command", scoundrelDetails.command),
                        Pair("Consort", scoundrelDetails.consort),
                        Pair("Sway", scoundrelDetails.sway)
                    ),
                    onRankClicked = { aString: String, aIndex: Int ->
                        circleClicker(aString, aIndex)
                    }
                )//end ActionBlock
            }
        }//end Action Blocks box

        //Start Special Abilities block
        SpecialAbilityBlock(
            onValueChange = onValueChange,
            scoundrelDetails = scoundrelDetails,
            everyAbilityList = everyAbilityList
        )
//end Special Abilities Block

        Spacer(modifier = Modifier.height(5.dp))

        ContactsBlock(
            onValueChange = onValueChange,
            scoundrelDetails = scoundrelDetails,
            everyContactList = everyContactList,
            saveContactConnection = saveContactConnection
        )
        //end Contacts block

    }//end Column
}//end ScoundrelInputForm

@Composable
private fun SpecialAbilityBlock(
    onValueChange: (ScoundrelDetails) -> Unit,
    scoundrelDetails: ScoundrelDetails,
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
        if (ability != "") {
            Log.d("ACCEPTED", ability)
            showAbilityDialog = false
            onValueChange(
                scoundrelDetails.copy(
                    specialAbilities = (scoundrelDetails.specialAbilities + listOf(
                        ability.trim()
                    )).distinct()
                )
            )
            newAbility = ""
        }
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
        scoundrelDetails.specialAbilities.forEach {
            if (it != ability) newAbilityList.add(it)
        }
        onValueChange(
            scoundrelDetails.copy(
                specialAbilities = newAbilityList
            )
        )
    }

    val abilityList = scoundrelDetails.specialAbilities
    TitleBlock(title = "", text = "Special Abilities")
    abilityList.forEach { it ->
        AbilityItem(
            ability = it,
            displayDeleteDialog = { abilityDeleteDialogClick(it) }
        )
    }
    MyButton(onClick = { abilityDialogClick() }, text = "Add new Ability", leadingIcon = Icons.Default.Add)
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
}//end Special Ability Block

@Composable
fun AbilityItem(
    ability: String,
    modifier: Modifier = Modifier,
    enableDelete: Boolean = true,
    displayDeleteDialog: (String) -> Unit
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = ability,
                    style = MaterialTheme.typography.bodyMedium
                )
                if (enableDelete) {
                    Icon(
                        imageVector = Icons.TwoTone.Delete,
                        contentDescription = "Delete",
                        Modifier
                            .clickable { displayDeleteDialog(ability) }
                            .align(Alignment.CenterVertically)
                            .size(25.dp)
                    )
                }
            }
        }
    }
}//end AbilityItem


@Composable
fun ContactsBlock(
    onValueChange: (ScoundrelDetails) -> Unit,
    scoundrelDetails: ScoundrelDetails,
    everyContactList: List<Contact>,
    saveContactConnection: (Contact, Rank) -> Unit
) {
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

    //begin delete contact code

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
        scoundrelDetails.contactsList.forEach {
            if (it.contact.name != contactName) newContactList.add(it)
        }
        onValueChange(
            scoundrelDetails.copy(
                contactsList = newContactList
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
    //end delete contact code



    val contactsList = scoundrelDetails.contactsList
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
            onAccept = {p1, p2->  doNewContact(p1, p2) },
            onValueChange = { onContactChange(it) },
            contactList = everyContactNameList
        )
    }

}//end Contacts Block

@Composable
fun ContactItem(
    contact: ContactAndRank,
    modifier: Modifier = Modifier,
    enableDelete: Boolean = true,
    displayDeleteContactDialog: (String) -> Unit = {},
    onClick: (ContactAndRank) -> Unit = {}
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick(contact) },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = contact.contact.name + " " +  contact.rank.rankName,
                    style = MaterialTheme.typography.bodyMedium
                )
                if (enableDelete) {
                    Icon(
                        imageVector = Icons.TwoTone.Delete,
                        contentDescription = "Delete",
                        Modifier
                            .clickable { displayDeleteContactDialog(contact.contact.name) }
                            .align(Alignment.CenterVertically)
                            .size(25.dp)
                    )
                }
            }
        }
    }
}//end ContactItem
