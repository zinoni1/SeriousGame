package com.zenonrodrigo.seriousgame

import android.app.Application
import androidx.room.Room
import com.zenonrodrigo.seriousgame.room.Joc
class App: Application() {
    lateinit var db: Joc

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            this,
            Joc::class.java,
            "joc"
        ).build()
    }

}