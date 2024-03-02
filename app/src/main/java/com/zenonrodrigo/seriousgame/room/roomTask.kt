package com.zenonrodrigo.seriousgame.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "puntuacio")
data class roomTask (
    @PrimaryKey val nivell: Int,
    var punts: Int = 0,
    var completat: Boolean = false
)
