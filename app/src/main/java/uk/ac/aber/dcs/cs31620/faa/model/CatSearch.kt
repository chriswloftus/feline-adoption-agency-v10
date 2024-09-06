package uk.ac.aber.dcs.cs31620.faa.model

/**
 * Makes passing of search criteria easier
 * @author Chris Loftus
 */
const val DEFAULT_DISTANCE = 10

data class CatSearch(
    var breed: String = "",
    var gender: String = "",
    var ageRange: String = "",
    var distance: Int = DEFAULT_DISTANCE
)