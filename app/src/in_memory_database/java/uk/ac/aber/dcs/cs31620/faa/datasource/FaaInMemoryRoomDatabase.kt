/**
 * The Room database implementation. We link to the Entity
 * class to use to define the cats table.
 * We also say which converters to use when converting to and from
 * database types.
 * We also populate a new database using code.
 * @author Chris Loftus
 * @version 1
 */
package uk.ac.aber.dcs.cs31620.faa.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uk.ac.aber.dcs.cs31620.faa.datasource.util.LocalDateTimeConverter
import uk.ac.aber.dcs.cs31620.faa.datasource.util.GenderConverter
import uk.ac.aber.dcs.cs31620.faa.model.Cat
import uk.ac.aber.dcs.cs31620.faa.model.CatDao

@Database(entities = [Cat::class], version = 1)
@TypeConverters(LocalDateTimeConverter::class, GenderConverter::class)
abstract class FaaInMemoryRoomDatabase : RoomDatabase(), RoomDatabaseI {

    abstract override fun catDao(): CatDao

    override fun closeDb() {
        instance?.close()
        instance = null
    }

    companion object {
        private var instance: FaaInMemoryRoomDatabase? = null

        fun getDatabase(context: Context): FaaInMemoryRoomDatabase? {
            synchronized(this) {
                if (instance == null) {
                    instance =
                        Room.inMemoryDatabaseBuilder(
                            context.applicationContext,
                            FaaInMemoryRoomDatabase::class.java
                        )
                            .allowMainThreadQueries() // Normally you would't but for testing
                            .build()
                }
                return instance!!
            }
        }
    }
}