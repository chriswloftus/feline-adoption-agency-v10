package uk.ac.aber.dcs.cs31620.faa.datasource

import android.app.Application
import uk.ac.aber.dcs.cs31620.faa.model.Cat
import uk.ac.aber.dcs.cs31620.faa.model.Gender
import java.time.LocalDateTime

/**
 * Provides a facade between the UI and the DAO
 * @author Chris Loftus
 */
class FaaRepository(application: Application) {
    private val catDao = Injection.getDatabase(application).catDao()

    suspend fun insert(cat: Cat){
        catDao.insertSingleCat(cat)
    }

    suspend fun insertMultipleCats(cats: List<Cat>){
        catDao.insertMultipleCats(cats)
    }

    fun getAllCats() = catDao.getAllCats()

    fun getCats(
        breed: String,
        gender: Gender,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ) = catDao.getCats(breed, gender, startDate, endDate)

    fun getRecentCats(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ) = catDao.getCatsAdmittedBetweenDates(startDate, endDate)

    fun getCatsByBreed(
        breed: String
    ) = catDao.getCatsByBreed(breed)

    fun getCatsByGender(
        gender: Gender
    ) = catDao.getCatsByGender(gender)

    fun getCatsBornBetweenDates(startDate: LocalDateTime, endDate: LocalDateTime)=
        catDao.getCatsBornBetweenDates(startDate, endDate)

    fun getCatsByBreedAndGender(breed: String, gender: String)=
        catDao.getCatsByBreedAndGender(breed, gender)

    fun getCatsByBreedAndBornBetweenDates(breed: String, startDate: LocalDateTime, endDate: LocalDateTime)=
        catDao.getCatsByBreedAndBornBetweenDates(breed, startDate, endDate)

    fun getCatsByGenderAndBornBetweenDates(gender: Gender, startDate: LocalDateTime, endDate: LocalDateTime)=
        catDao.getCatsByGenderAndBornBetweenDates(gender, startDate, endDate)

   /* fun getRecentCatsSync(startDate: LocalDateTime, endDate: LocalDateTime)=
        catDao.getCatsAdmittedBetweenDatesSync(startDate, endDate)*/
}