package uk.ac.aber.dcs.cs31620.faa.datasource.util

import androidx.room.TypeConverter
import uk.ac.aber.dcs.cs31620.faa.model.Gender

object GenderConverter {
    @TypeConverter
    @JvmStatic
    fun toString(gender: Gender) = gender.toString()

    @TypeConverter
    @JvmStatic
    fun toGender(gender: String) = Gender.valueOf(gender)
}