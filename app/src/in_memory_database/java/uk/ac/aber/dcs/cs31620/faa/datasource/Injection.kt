package uk.ac.aber.dcs.cs31620.faa.datasource

import android.content.Context

object Injection {
    fun getDatabase(context: Context): RoomDatabaseI =
        FaaInMemoryRoomDatabase.getDatabase(context)!!
}