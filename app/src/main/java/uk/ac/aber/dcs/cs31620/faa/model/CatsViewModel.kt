package uk.ac.aber.dcs.cs31620.faa.model

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.faa.R
import uk.ac.aber.dcs.cs31620.faa.datasource.FaaRepository
import java.time.LocalDateTime
import java.util.Locale

const val NUM_DAYS_RECENT: Long = 30

class CatsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FaaRepository = FaaRepository(application)

    var recentCats: LiveData<List<Cat>> = loadRecentCats()
        private set

    var catList: LiveData<List<Cat>> = repository.getAllCats()
        private set

    private val ageConstraints = application.resources.getStringArray(R.array.age_range_array)
    private val anyAge = ageConstraints[0]
    private val anyGender = application.resources.getStringArray(R.array.gender_array)[0]
    private val anyBreed = application.resources.getStringArray(R.array.breed_array)[0]

    var catSearch: CatSearch by mutableStateOf(
        CatSearch(
            breed = anyBreed,
            gender = anyGender,
            ageRange = anyAge,
            distance = DEFAULT_DISTANCE
        )
    )
        private set

    fun updateCatSearch(value: CatSearch) {
        // Now reissue the search
        getCats(value)
    }

    fun insertCat(newCat: Cat) {
        viewModelScope.launch(Dispatchers.IO){
            repository.insert(newCat)
        }
    }

    private fun loadRecentCats(): LiveData<List<Cat>> {
        // We actually make the present the future. This is a fudge to
        // make sure the LiveData query remains relevant to the admission
        // of new cats after the query has been made. If we don't do this
        // the LiveData will not emit onChange requests to its Observers.
        // Bug: we should force re-query when the real current date
        // changes to a new day, otherwise the recent cats period for
        // the LiveData query will stretch!
        val endDate = LocalDateTime.now().plusDays(365)
        val pastDate = LocalDateTime.now().minusDays(NUM_DAYS_RECENT)

        return repository.getRecentCats(pastDate, endDate)
    }

    private fun getCats(newCatSearch: CatSearch) {
        val startDate: LocalDateTime
        val endDate: LocalDateTime
        var changed = false

        // Did anything change since last time?
        if (newCatSearch.breed != catSearch.breed) {
            changed = true
        }
        if (newCatSearch.gender != catSearch.gender) {
            changed = true
        }
        if (newCatSearch.ageRange != catSearch.ageRange) {
            changed = true
        }
        if (newCatSearch.distance != catSearch.distance) {
            changed = true
        }

        if (changed) {
            // We load again based on the values of newCatSearch.
            // We look for values that are defaults: those are the ones that
            // need excluding from the request, and determine which method
            // Dao overload to call.
            if (newCatSearch.breed != anyBreed && newCatSearch.gender == anyGender && newCatSearch.ageRange == anyAge) {
                catList = repository.getCatsByBreed(newCatSearch.breed)
            } else if (newCatSearch.breed == anyBreed && newCatSearch.gender != anyGender && newCatSearch.ageRange == anyAge) {
                catList =
                    repository.getCatsByGender(Gender.valueOf(newCatSearch.gender.uppercase(Locale.ROOT)))
            } else if (newCatSearch.breed == anyBreed && newCatSearch.gender == anyGender && newCatSearch.ageRange != anyAge) {
                startDate = getStartDate(newCatSearch.ageRange)
                endDate = getEndDate(newCatSearch.ageRange)
                catList = repository.getCatsBornBetweenDates(startDate, endDate)
            } else if (newCatSearch.breed != anyBreed && newCatSearch.gender != anyGender && newCatSearch.ageRange == anyAge) {
                catList =
                    repository.getCatsByBreedAndGender(newCatSearch.breed, newCatSearch.gender)
            } else if (newCatSearch.breed != anyBreed && newCatSearch.gender == anyGender && newCatSearch.ageRange != anyAge) {
                startDate = getStartDate(newCatSearch.ageRange)
                endDate = getEndDate(newCatSearch.ageRange)
                catList = repository.getCatsByBreedAndBornBetweenDates(
                    newCatSearch.breed,
                    startDate,
                    endDate
                )
            } else if (newCatSearch.breed == anyBreed && newCatSearch.gender != anyGender && newCatSearch.ageRange != anyAge) {
                startDate = getStartDate(newCatSearch.ageRange)
                endDate = getEndDate(newCatSearch.ageRange)
                catList = repository.getCatsByGenderAndBornBetweenDates(
                    Gender.valueOf(
                        newCatSearch.gender.uppercase(
                            Locale.ROOT
                        )
                    ), startDate, endDate
                )
            } else if (newCatSearch.breed != anyBreed && newCatSearch.gender != anyGender && newCatSearch.ageRange != anyAge) {
                startDate = getStartDate(newCatSearch.ageRange)
                endDate = getEndDate(newCatSearch.ageRange)
                catList = repository.getCats(
                    newCatSearch.breed,
                    Gender.valueOf(newCatSearch.gender.uppercase(Locale.ROOT)),
                    startDate,
                    endDate
                )
            } else if (newCatSearch.distance == catSearch.distance){
                // Only do this search for all other cases except distance change
                // We're not handling distance at the moment
                catList = repository.getAllCats()
            }

            // We can now update the state variable
            catSearch = newCatSearch
        }
    }

    private fun getEndDate(ageConstraint: String): LocalDateTime =
        when (ageConstraint) {
            ageConstraints[1] -> // 0 - 1 year
                // End date will be now. Fudge time: we add year here (into the future)
                // to solve the issue of LiveData queries showing up to 1 year cats
                // and a brand new cat is registered that was born on today's date
                // and has a time slightly later than the time used in the query and so
                // the LiveData update does not happen
                LocalDateTime.now().plusDays(365)
            ageConstraints[2] -> // 1 - 2 years
                // End date is 1 year ago
                LocalDateTime.now().minusDays(364)
            ageConstraints[3] -> // 2 - 5 years
                // End date is 2 years ago
                LocalDateTime.now().minusDays(365 * 2 - 1)
            else -> // Older
                // End date is 5 years ago
                LocalDateTime.now().minusDays(365 * 5 - 1)
        }

    private fun getStartDate(ageConstraint: String): LocalDateTime =
        when (ageConstraint) {
            ageConstraints[1] -> // 0 - 1 year
                // Start date is 1 year ago
                LocalDateTime.now().minusDays(365)
            ageConstraints[2] -> // 1 - 2 years
                // Start date is 2 year ago
                LocalDateTime.now().minusDays(365 * 2)
            ageConstraints[3] -> // 2 - 5 years
                // Start date is 5 years ago
                LocalDateTime.now().minusDays(365 * 5)
            else -> // Start date is > 5 years ago
                // Just use a very large number
                LocalDateTime.now().minusDays(365 * 40 - 1)
        }
}