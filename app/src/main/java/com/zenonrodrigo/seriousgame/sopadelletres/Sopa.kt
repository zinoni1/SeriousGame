package com.zenonrodrigo.seriousgame.sopadelletres

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.zenonrodrigo.seriousgame.App
import com.zenonrodrigo.seriousgame.MainActivity
import com.zenonrodrigo.seriousgame.R
import com.zenonrodrigo.seriousgame.room.roomDao
import com.zenonrodrigo.seriousgame.room.roomTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Sopa : AppCompatActivity() {
    private val letras = listOf(
        "A", "B", "C", "D", "E", "F", "G", "H",
        "I", "J", "K", "L", "M", "N", "O", "P",
        "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    )
    private val palabraCastell = listOf("C", "A", "S", "T", "E", "L", "L")
    private val palabraPluja = listOf("D", "R", "A", "C")
    private val palabraBolets = listOf("C", "A", "V", "A", "L", "L", "E", "R")
    private val palabraRosa = listOf("R", "O", "S", "A")
    private lateinit var taskDao: roomDao
    private val letrasSeleccionadas = mutableListOf<TextView>()
    private var countCompletat = 0
    private lateinit var gridLayout: GridLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sopa_de_lletres)

        gridLayout = findViewById(R.id.gridLayout)
        val db = (applicationContext as App).db
        taskDao = db.taskDao()
        // FILA ALEATORIA PER CADA FILA
        val filaAleatoriaCasatanya = (0 until gridLayout.rowCount).shuffled().first()
        val filaAleatoriaPluja = (0 until gridLayout.rowCount).filter { it != filaAleatoriaCasatanya }.shuffled().first()
        val filaAleatoriaBolets = (0 until gridLayout.rowCount).filter { it != filaAleatoriaCasatanya && it != filaAleatoriaPluja }.shuffled().first()
        val filaAleatoriaFulla = (0 until gridLayout.rowCount).filter { it != filaAleatoriaCasatanya && it != filaAleatoriaPluja && it != filaAleatoriaBolets }.shuffled().first()

        // OMPLIR FILES AMB CADA PARAULA
        omplirFila(gridLayout, filaAleatoriaCasatanya, palabraCastell)
        omplirFila(gridLayout, filaAleatoriaPluja, palabraPluja)
        omplirFila(gridLayout, filaAleatoriaBolets, palabraBolets)
        omplirFila(gridLayout, filaAleatoriaFulla, palabraRosa)

        // OMPLIR ELS ALTRES ESPAIS AMB LLETRES ALEATORIES
        for (i in 0 until gridLayout.childCount) {
            val child = gridLayout.getChildAt(i)
            if (child is TextView && child.text.isBlank()) {
                val letraAleatoria = letras.random()
                child.text = letraAleatoria
            }
        }

        // Gestionar clics en las letras
        for (i in 0 until gridLayout.childCount) {
            val child = gridLayout.getChildAt(i)
            if (child is TextView) {
                child.setOnClickListener { onLetterClick(it) }
            }
        }
    }

    private fun omplirFila(gridLayout: GridLayout, fila: Int, palabra: List<String>) {
        for (i in palabra.indices) {
            val child = gridLayout.getChildAt(fila * gridLayout.columnCount + i)
            if (child is TextView) {
                child.text = palabra[i]
            }
        }
    }

    private fun onLetterClick(view: View) {
        val textView = view as TextView
        //NO VA AL ELSE FINS QUE EL CONTADOR SIGUI 4

        if (countCompletat != 4) {
            if (letrasSeleccionadas.isNotEmpty()) {
                val ultimaLetraSeleccionada = letrasSeleccionadas.last()
                val indexUltimaLetra = gridLayout.indexOfChild(ultimaLetraSeleccionada)
                val indexLetraActual = gridLayout.indexOfChild(textView)

                if (!sonLetrasEnLaMismaFila(indexUltimaLetra, indexLetraActual)) {
                    return
                }
            }

            if (letrasSeleccionadas.contains(textView)) {
                letrasSeleccionadas.remove(textView)
                textView.setBackgroundColor(Color.parseColor("#FFFEDC"))
            } else {
                letrasSeleccionadas.add(textView)
                textView.setBackgroundColor(Color.BLUE)
            }

            if (isWordComplete(palabraCastell)) {
                completarPalabra(letrasSeleccionadas)
                countCompletat++
            } else if (isWordComplete(palabraPluja)) {
                completarPalabra(letrasSeleccionadas)
                countCompletat++
            } else if (isWordComplete(palabraBolets)) {
                completarPalabra(letrasSeleccionadas)
                countCompletat++
            } else if (isWordComplete(palabraRosa)) {
                completarPalabra(letrasSeleccionadas)
                countCompletat++
            }

            if (countCompletat == 4) {
                mostrarMensajeVictoria()
            }
        }
    }

    private fun sonLetrasEnLaMismaFila(index1: Int, index2: Int): Boolean {
        val fila1 = index1 / gridLayout.columnCount
        val fila2 = index2 / gridLayout.columnCount

        return fila1 == fila2
    }

    private fun isWordComplete(palabra: List<String>): Boolean {
        return letrasSeleccionadas.map { it.text.toString() } == palabra
    }

    private fun completarPalabra(letras: List<TextView>) {
        letras.forEach {
            it.setBackgroundColor(Color.GREEN)
            it.setOnClickListener(null)
        }
        letrasSeleccionadas.clear()
    }

    private fun mostrarMensajeVictoria() {
        CoroutineScope(Dispatchers.IO).launch {
            val task = roomTask(nivell = 6, punts = 1, completat = true)
            taskDao.insertLvl(task)

            withContext(Dispatchers.Main) {
                AlertDialog.Builder(this@Sopa)
                    .setTitle("¡Felicitats!")
                    .setMessage("Has completat el joc.")
                    .setPositiveButton("Acceptar") { _, _ ->
                        finish()
                    }
                    .show()
            }
        }
    }

    private fun volverAlMenu() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()  // Esto cierra la actividad actual y vuelve al menú si es la actividad principal
    }

}