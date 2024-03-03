package com.zenonrodrigo.seriousgame.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface roomDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertLvl(task: roomTask)

    @Update (onConflict = OnConflictStrategy.REPLACE)
    fun updateLvl(task: roomTask)

    @Query("SELECT SUM(punts) FROM puntuacio")
    fun getTotalPunts(): Int




}
