package com.zenonrodrigo.seriousgame

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.room.RoomDatabase
import com.zenonrodrigo.seriousgame.room.roomDao
import com.zenonrodrigo.seriousgame.room.roomTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class nivell4 : AppCompatActivity() {
    private var firstClicked: View? = null
    private var secondClicked: View? = null
    private var combinacionesCorrectas: Int = 0
    private lateinit var taskDao: roomDao
    private val handler = Handler(Looper.getMainLooper())

    private val combinaciones = mutableListOf(
        Combinacion("Castell", R.drawable.castelldeconte),
        Combinacion("Drac", R.drawable.drac),
        Combinacion("Rosa", R.drawable.rosa),
        Combinacion("Cavaller", R.drawable.cavaller),
        Combinacion("Cavall", R.drawable.cavall),
        Combinacion("Princesa", R.drawable.princesa),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nivell4)

        combinaciones.shuffle()
        val db = (applicationContext as App).db
        taskDao = db.taskDao()

        // Mezclar las combinaciones aleatoriamente
        val combinacionesAleatorias = combinaciones.shuffled()

        setupElemento(R.id.textViewElement1, R.id.imageViewElement1, combinacionesAleatorias[0])
        setupElemento(R.id.textViewElement2, R.id.imageViewElement2, combinacionesAleatorias[1])
        setupElemento(R.id.textViewElement3, R.id.imageViewElement3, combinacionesAleatorias[2])
    }

    private fun setupElemento(textViewId: Int, imageViewId: Int, combinacion: Combinacion) {
        val textViewElemento = findViewById<TextView>(textViewId)
        val imageViewElemento = findViewById<ImageView>(imageViewId)

        textViewElemento.text = combinacion.palabra
        imageViewElemento.setImageResource(combinacion.imagen)

        textViewElemento.setOnClickListener {
            onElementClicked(textViewElemento)
        }

        imageViewElemento.setOnClickListener {
            onElementClicked(imageViewElemento)
        }
    }

    private fun onElementClicked(view: View) {
        if (view.tag == null && view.isEnabled) {
            if (firstClicked == null) {
                firstClicked = view
                view.setBackgroundColor(Color.YELLOW)
            } else if (secondClicked == null) {
                secondClicked = view
                view.setBackgroundColor(Color.YELLOW)

                if (isCorrectCombination(firstClicked!!, secondClicked!!)) {
                    handleCorrectCombination()
                } else {
                    handleIncorrectCombination()
                }
            }
        }
    }

    private fun isCorrectCombination(view1: View, view2: View): Boolean {
        val combinacion1 = obtenerCombinacion(view1)
        val combinacion2 = obtenerCombinacion(view2)

        return combinacion1 != null && combinacion2 != null &&
                (combinacion1.palabra == combinacion2.palabra && combinacion1.imagen == combinacion2.imagen)
    }

    private fun obtenerCombinacion(view: View): Combinacion? {
        return when (view.id) {
            R.id.textViewElement1 -> combinaciones[0]
            R.id.imageViewElement1 -> combinaciones[0]
            R.id.textViewElement2 -> combinaciones[1]
            R.id.imageViewElement2 -> combinaciones[1]
            R.id.textViewElement3 -> combinaciones[2]
            R.id.imageViewElement3 -> combinaciones[2]
            else -> null
        }
    }

    private fun handleCorrectCombination() {
        firstClicked?.visibility = View.INVISIBLE
        secondClicked?.visibility = View.INVISIBLE

        Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show()
        combinacionesCorrectas++

        if (combinacionesCorrectas == 3) {
            mostrarMensajeVictoria()
        }

        resetIncorrectViews()
    }

    private fun handleIncorrectCombination() {
        firstClicked?.setBackgroundColor(Color.RED)
        secondClicked?.setBackgroundColor(Color.RED)
        Toast.makeText(this, "Incorrecto, intenta de nuevo", Toast.LENGTH_SHORT).show()

        handler.postDelayed({
            resetIncorrectViews()
        }, 1000)
    }

    private fun resetIncorrectViews() {
        firstClicked?.setBackgroundColor(Color.TRANSPARENT)
        secondClicked?.setBackgroundColor(Color.TRANSPARENT)
        firstClicked = null
        secondClicked = null
    }

    private fun mostrarMensajeVictoria() {
        CoroutineScope(Dispatchers.IO).launch {
            val task = roomTask(nivell = 4, punts = 1, completat = true)
            taskDao.insertLvl(task)

            // Cambiar el contexto a Dispatchers.Main para mostrar el diálogo
            withContext(Dispatchers.Main) {
                AlertDialog.Builder(this@nivell4)
                    .setTitle("¡Felicitats!")
                    .setMessage("Has completat el joc.")
                    .setPositiveButton("Acceptar") { _, _ ->
                        finish()
                    }
                    .show()
            }
        }
    }


    data class Combinacion(val palabra: String, val imagen: Int)
}
