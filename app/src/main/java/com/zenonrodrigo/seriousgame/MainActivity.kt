package com.zenonrodrigo.seriousgame

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.zenonrodrigo.seriousgame.room.roomDao
import com.zenonrodrigo.seriousgame.room.roomTask
import com.zenonrodrigo.seriousgame.sopadelletres.Sopa
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    private lateinit var taskDao: roomDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db = (applicationContext as App).db
        taskDao = db.taskDao()
        //boton 3 intent al pulsar
        val boton2 = findViewById<View>(R.id.lvl2)
        val boton5 = findViewById<View>(R.id.lvl5)
        val boton1 = findViewById<View>(R.id.lvl1)
        val boton3 = findViewById<View>(R.id.lvl3)
        val boton6 = findViewById<View>(R.id.lvl6)
        val boton4 = findViewById<View>(R.id.lvl4)
        val punts = findViewById<TextView>(R.id.punts)

        CoroutineScope(Dispatchers.IO).launch {
            val totalPunts = taskDao.getTotalPunts()
            withContext(Dispatchers.Main) {
                punts.text = "Punts: $totalPunts"
            }

        }
        boton4.setOnClickListener {
            val intent = Intent(this, nivell4::class.java)
            startActivity(intent)
        }
        boton1.setOnClickListener {
            val intent = Intent(this, Nivell1::class.java)
            startActivity(intent)
        }
        boton2.setOnClickListener {
            val intent = Intent(this, Nivell2::class.java)
            startActivity(intent)
        }
        boton3.setOnClickListener {
            val intent = Intent(this, Nivell3::class.java)
            startActivity(intent)
        }
        boton6.setOnClickListener {
            val intent = Intent(this, Sopa::class.java)
            startActivity(intent)
        }
        boton5.setOnClickListener {
            val intent = Intent(this, nivell5::class.java)
            startActivity(intent)
        }
    }
}
