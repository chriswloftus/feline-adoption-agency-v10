package uk.ac.aber.dcs.cs31620.faa.datasource.util

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.TimeZone

/**
 * Used to convert LocalDateTime to Long and vice versa
 * @author Chris Loftus
 */
object LocalDateTimeConverter {
    @TypeConverter
    @JvmStatic
    fun toLocalDate(timestamp: Long): LocalDateTime = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(timestamp),
        TimeZone.getDefault().toZoneId()
    )

    @TypeConverter
    @JvmStatic
    fun toTimestamp(localDateTime: LocalDateTime): Long =
        localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}