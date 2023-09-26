package uk.ac.aber.dcs.cs31620.faa.model

import androidx.lifecycle.LiveData
import androidx.room.*
import java.time.LocalDateTime

@Dao
interface CatDao {
    @Insert
    fun insertSingleCat(cat: Cat)

    @Insert
    fun insertMultipleCats(catsList: List<Cat>)

    @Update
    fun updateCat(cat: Cat)

    @Delete
    fun deleteCat(cat: Cat)

    @Query("DELETE FROM cats")
    fun deleteAll()

    @Query("SELECT * FROM cats")
    fun getAllCats(): LiveData<List<Cat>>

    @Query("""SELECT * FROM cats WHERE breed = :breed AND
        gender = :gender AND dob BETWEEN :startDate AND :endDate""")
    fun getCats(
        breed: String,
        gender: Gender,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): LiveData<List<Cat>>

    // Updated to add ORDER BY as requested by the exercise in Workshop 16, section 1
    @Query("""SELECT * FROM cats WHERE admission_date
        BETWEEN :startDate AND :endDate ORDER BY :startDate DESC""")
    fun getCatsAdmittedBetweenDates(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): LiveData<List<Cat>>

    // We won't assume the use of a coroutine scope for this one, just to make debugging
    // simple
    @Query("""SELECT * FROM cats WHERE admission_date
        BETWEEN :startDate AND :endDate""")
    fun getCatsAdmittedBetweenDatesSync(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<Cat>

    // Extra functions based on the exercise from workshop 16 section 1
    @Query("SELECT * FROM cats WHERE breed = :breed")
    fun getCatsByBreed(
        breed: String
    ): LiveData<List<Cat>>

    @Query("SELECT * FROM cats WHERE gender = :gender")
    fun getCatsByGender(
        gender: Gender
    ): LiveData<List<Cat>>

    @Query("SELECT * FROM cats WHERE dob BETWEEN :startDate AND :endDate")
    fun getCatsBornBetweenDates(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): LiveData<List<Cat>>

    @Query("SELECT * FROM cats WHERE breed = :breed AND gender = :gender")
    fun getCatsByBreedAndGender(breed: String, gender: String): LiveData<List<Cat>>

    @Query(
        """SELECT * FROM cats WHERE breed = :breed 
              AND dob BETWEEN :startDate AND :endDate"""
    )
    fun getCatsByBreedAndBornBetweenDates(
        breed: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): LiveData<List<Cat>>

    @Query(
        """SELECT * FROM cats WHERE gender = :gender 
              AND dob BETWEEN :startDate AND :endDate"""
    )
    fun getCatsByGenderAndBornBetweenDates(
        gender: Gender,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): LiveData<List<Cat>>

}