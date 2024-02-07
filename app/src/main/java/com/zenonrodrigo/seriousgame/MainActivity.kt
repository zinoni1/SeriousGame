package com.zenonrodrigo.seriousgame

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Puedes cargar la vista de inicio o iniciar otra actividad aquí
        // Por ejemplo, si tienes una pantalla de inicio, podrías hacer algo así:
        val intent = Intent(this, Nivell3::class.java)
        startActivity(intent)
        finish() // Termina la actividad actual para evitar volver atrás a ella
    }
}
