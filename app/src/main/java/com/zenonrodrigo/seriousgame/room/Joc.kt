package com.zenonrodrigo.seriousgame.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [roomTask::class], version = 1)

abstract class Joc : RoomDatabase() {
    abstract fun taskDao(): roomDao


}