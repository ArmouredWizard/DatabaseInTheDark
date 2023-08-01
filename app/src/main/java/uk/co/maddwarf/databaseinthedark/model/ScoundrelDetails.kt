package uk.co.maddwarf.databaseinthedark.model

import uk.co.maddwarf.databaseinthedark.data.Scoundrel
import uk.co.maddwarf.databaseinthedark.data.relations.ContactAndRank

data class ScoundrelDetails(
    val scoundrelId: Int = 0,
    val name: String = "",
    val playbook: String = "",
    val crewId: Int = 0,
    val hunt: String = "0",
    val study: String = "0",
    val survey: String = "0",
    val tinker: String = "0",
    val finesse: String = "0",
    val prowl: String = "0",
    val skirmish: String = "0",
    val wreck: String = "0",
    val attune: String = "0",
    val command: String = "0",
    val consort: String = "0",
    val sway: String = "0",
    val heritage: String = "",
    val background: String = "",
    val specialAbilities:List<String> = listOf(),
    var contactsList:List<ContactAndRank> = listOf()

)

//extension Functions
fun ScoundrelDetails.toScoundrel(): Scoundrel = Scoundrel(
    scoundrelId = scoundrelId,
    name = name.trim(),
    playbook = playbook.trim(),
    crewId = crewId,
    hunt = hunt.trim(),
    study = study.trim(),
    survey = survey.trim(),
    tinker = tinker.trim(),
    finesse = finesse.trim(),
    prowl = prowl.trim(),
    skirmish = skirmish.trim(),
    wreck = wreck.trim(),
    attune = attune.trim(),
    command = command.trim(),
    consort = consort.trim(),
    sway = sway.trim(),
    heritage = heritage.trim(),
    background = background.trim(),
    specialAbilities = specialAbilities
)

fun Scoundrel.toScoundrelDetails(): ScoundrelDetails = ScoundrelDetails(
    scoundrelId = scoundrelId,
    name = name,
    playbook = playbook.trim(),
    crewId = crewId,
    hunt = hunt.trim(),
    study = study.trim(),
    survey = survey.trim(),
    tinker = tinker.trim(),
    finesse = finesse.trim(),
    prowl = prowl.trim(),
    skirmish = skirmish.trim(),
    wreck = wreck.trim(),
    attune = attune.trim(),
    command = command.trim(),
    consort = consort.trim(),
    sway = sway.trim(),
    heritage = heritage.trim(),
    background = background.trim(),
    //specialAbilities =specialAbilities
    specialAbilities = specialAbilities.ifEmpty {
        listOf()
    }

)