package uk.ac.aber.dcs.cs31620.faa.datasource

import uk.ac.aber.dcs.cs31620.faa.model.CatDao

interface RoomDatabaseI {
    fun catDao(): CatDao
    fun closeDb()
}