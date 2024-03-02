package com.zenonrodrigo.seriousgame.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface roomDao {

    @Insert
    fun insertLvl(task: roomTask)

    @Query("SELECT SUM(punts) FROM puntuacio")
    fun getTotalPunts(): Int

}
