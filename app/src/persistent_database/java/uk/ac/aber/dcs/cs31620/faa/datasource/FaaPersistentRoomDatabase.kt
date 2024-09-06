package uk.ac.aber.dcs.cs31620.faa.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.faa.datasource.util.GenderConverter
import uk.ac.aber.dcs.cs31620.faa.datasource.util.LocalDateTimeConverter
import uk.ac.aber.dcs.cs31620.faa.model.Cat
import uk.ac.aber.dcs.cs31620.faa.model.CatDao
import uk.ac.aber.dcs.cs31620.faa.model.Gender
import java.time.LocalDateTime

@Database(entities = [Cat::class], version = 1)
@TypeConverters(LocalDateTimeConverter::class, GenderConverter::class)
abstract class FaaPersistentRoomDatabase : RoomDatabase(), RoomDatabaseI {
    abstract override fun catDao(): CatDao

    override fun closeDb() {
        instance?.close()
        instance = null
    }

    companion object {
        private var instance: FaaPersistentRoomDatabase? = null
        private val coroutineScope = CoroutineScope(Dispatchers.IO)

        @Synchronized
        fun getDatabase(context: Context): FaaPersistentRoomDatabase? {
            if (instance == null) {
                instance =
                    Room.databaseBuilder<FaaPersistentRoomDatabase>(
                        context.applicationContext,
                        FaaPersistentRoomDatabase::class.java,
                        "faa_database"
                    )
                        //.allowMainThreadQueries()
                        .addCallback(roomDatabaseCallback(context))
                        //.addMigrations(MIGRATION_1_2, MIGRATION_2_3
                        .build()
            } // if
            return instance
        }

        private fun roomDatabaseCallback(context: Context): Callback {
            return object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    coroutineScope.launch {
                        populateDatabase(context, getDatabase(context)!!)
                    }
                }
            }
        }

        private suspend fun populateDatabase(context: Context, instance: FaaPersistentRoomDatabase) {

            val imagePath = "file:///android_asset/images/"

            val upToOneYear = LocalDateTime.now().minusDays(365 / 2)
            val from1to2Years = LocalDateTime.now().minusDays(365 + (36 / 2))
            val from2to5Years = LocalDateTime.now().minusDays(365 * 3)
            val over5Years = LocalDateTime.now().minusDays(365 * 10)
            val admissionsDate = LocalDateTime.now().minusDays(60)
            val veryRecentAdmission = LocalDateTime.now()

            val upToOneYearCat = Cat(
                0, "Tibs (< 1)", Gender.MALE,
                "Moggie",
                "Lorem ipsum dolor...",
                upToOneYear,
                veryRecentAdmission,
                "${imagePath}cat1.png"
            )

            val from1to2YearsCat = Cat(
                0,
                "Tibs (1 - 2)",
                Gender.MALE,
                "Moggie",
                "Lorem ipsum dolor sit amet, consectetur...",
                from1to2Years,
                admissionsDate,
                "${imagePath}cat1.png"
            )

            val from2to5YearsCat = Cat(
                0,
                "Tibs (2 - 5)",
                Gender.MALE,
                "Moggie",
                "Lorem ipsum dolor sit amet, consectetur...",
                from2to5Years,
                admissionsDate,
                "${imagePath}cat1.png"
            )

            val over5YearsCat = Cat(
                0,
                "Tibs (> 5)",
                Gender.MALE,
                "Moggie",
                "Lorem ipsum dolor sit amet, consectetur...",
                over5Years,
                admissionsDate,
                "${imagePath}cat1.png"
            )

            val catList = mutableListOf(
                upToOneYearCat,
                upToOneYearCat,
                from1to2YearsCat,
                from1to2YearsCat,
                from2to5YearsCat,
                from2to5YearsCat,
                over5YearsCat,
                over5YearsCat,
                over5YearsCat,
                over5YearsCat
            )

            val dao = instance.catDao()
            dao.insertMultipleCats(catList)
        }
    }
}