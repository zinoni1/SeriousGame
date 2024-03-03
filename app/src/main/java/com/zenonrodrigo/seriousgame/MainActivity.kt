package com.zenonrodrigo.seriousgame

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.zenonrodrigo.seriousgame.room.roomDao
import com.zenonrodrigo.seriousgame.sopadelletres.Sopa
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    private lateinit var taskDao: roomDao
    private lateinit var punts: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db = (applicationContext as App).db
        taskDao = db.taskDao()
        punts = findViewById<TextView>(R.id.punts)

        setupButtons()
        loadPoints()
    }

    override fun onResume() {
        super.onResume()
        loadPoints()
    }

    private fun setupButtons() {
        val boton5 = findViewById<View>(R.id.lvl5)
        val boton1 = findViewById<View>(R.id.lvl1)
        val boton3 = findViewById<View>(R.id.lvl3)
        val boton6 = findViewById<View>(R.id.lvl6)
        val boton4 = findViewById<View>(R.id.lvl4)

        boton4.setOnClickListener {
            val intent = Intent(this, nivell4::class.java)
            startActivity(intent)
        }
        boton1.setOnClickListener {
            val intent = Intent(this, Nivell1::class.java)
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

    private fun loadPoints() {
        CoroutineScope(Dispatchers.IO).launch {
            val puntsTotal = taskDao.getTotalPunts()
            withContext(Dispatchers.Main) {
                punts.text = "Punts: $puntsTotal"
            }
        }
    }

}
