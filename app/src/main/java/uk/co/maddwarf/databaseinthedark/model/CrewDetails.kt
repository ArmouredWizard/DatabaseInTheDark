package uk.co.maddwarf.databaseinthedark.model

import uk.co.maddwarf.databaseinthedark.data.Crew
import uk.co.maddwarf.databaseinthedark.data.relations.ContactAndRank

data class CrewDetails(
    val crewId:Int = 0,
    val name: String = "",
    val hq: String = "",
    val rep: String = "0",
    val heat: String = "0",
    val tier: String = "0",
    val holdIsStrong: Boolean = false,
    val type: String = "",
    val reputation: String = "",
    val huntingGrounds: String = "",
    val crewAbilities: List<String> = listOf(),
    var crewContactsList:List<ContactAndRank> = listOf()
)

fun CrewDetails.toCrew(): Crew = Crew(
    crewId = 0,
    crewName = name,
    hq = hq.trim(),
    rep=rep.trim(),
    holdIsStrong = holdIsStrong,
    heat = heat.trim(),
    tier = tier.trim(),
    type = type.trim(),
    reputation = reputation.trim(),
    huntingGrounds = huntingGrounds.trim(),
    crewAbilities = crewAbilities,
)

fun Crew.toCrewDetails(): CrewDetails = CrewDetails(
    crewId = crewId,
    name = crewName.trim(),
    hq = hq.trim(),
    rep=rep.trim(),
    holdIsStrong = holdIsStrong,
    heat = heat.trim(),
    tier = tier.trim(),
    type = type.trim(),
    reputation = reputation.trim(),
    huntingGrounds = huntingGrounds.trim(),
    crewAbilities = crewAbilities,
    crewContactsList = listOf()
)