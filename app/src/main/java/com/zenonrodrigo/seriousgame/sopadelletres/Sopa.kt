package com.zenonrodrigo.seriousgame.sopadelletres

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.widget.GridLayout
import android.widget.ImageView
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
    private val lletres = listOf(
        "A", "B", "C", "D", "E", "F", "G", "H",
        "I", "J", "K", "L", "M", "N", "O", "P",
        "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    )
    // Paraules a trobar
    private val paraulaCastell = listOf("C", "A", "S", "T", "E", "L", "L")
    private val paraulaPluja = listOf("D", "R", "A", "C")
    private val paraulaBolets = listOf("C", "A", "V", "A", "L", "L", "E", "R")
    private val paraulaRosa = listOf("R", "O", "S", "A")
    private lateinit var taskDao: roomDao
    private val lletresSeleccionades = mutableListOf<TextView>()
    private var countCompletat = 0
    private lateinit var gridLayout: GridLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sopa_de_lletres)

        gridLayout = findViewById(R.id.gridLayout)
        val db = (applicationContext as App).db
        taskDao = db.taskDao()
        // FILA ALEATÒRIA PER A CADA FILA

        val filaAleatoriaCasatanya = (0 until gridLayout.rowCount).shuffled().first()
        val filaAleatoriaPluja = (0 until gridLayout.rowCount).filter { it != filaAleatoriaCasatanya }.shuffled().first()
        val filaAleatoriaBolets = (0 until gridLayout.rowCount).filter { it != filaAleatoriaCasatanya && it != filaAleatoriaPluja }.shuffled().first()
        val filaAleatoriaFulla = (0 until gridLayout.rowCount).filter { it != filaAleatoriaCasatanya && it != filaAleatoriaPluja && it != filaAleatoriaBolets }.shuffled().first()

        // OMPLE LES FILES AMB CADA PARAULA
        omplirFila(gridLayout, filaAleatoriaCasatanya, paraulaCastell)
        omplirFila(gridLayout, filaAleatoriaPluja, paraulaPluja)
        omplirFila(gridLayout, filaAleatoriaBolets, paraulaBolets)
        omplirFila(gridLayout, filaAleatoriaFulla, paraulaRosa)

        // OMPLE ELS ALTRES ESPAIS AMB LLETRES ALEATÒRIES
        for (i in 0 until gridLayout.childCount) {
            val child = gridLayout.getChildAt(i)
            if (child is TextView && child.text.isBlank()) {
                val lletraAleatoria = lletres.random()
                child.text = lletraAleatoria
            }
        }

        // Gestiona clics a les lletres
        for (i in 0 until gridLayout.childCount) {
            val child = gridLayout.getChildAt(i)
            if (child is TextView) {
                child.setOnClickListener { onLetterClick(it) }
            }
        }
    }
// Funció per a omplir les files amb les paraules
    private fun omplirFila(gridLayout: GridLayout, fila: Int, paraula: List<String>) {
        for (i in paraula.indices) {
            val child = gridLayout.getChildAt(fila * gridLayout.columnCount + i)
            if (child is TextView) {
                child.text = paraula[i]
            }
        }
    }
// Funció per a gestionar els clics a les lletres
    private fun onLetterClick(view: View) {
        val textView = view as TextView
        // NO VA AL ELSE FINS QUE EL COMPTADOR SIGUI 4

        if (countCompletat != 4) {
            if (lletresSeleccionades.isNotEmpty()) {
                val ultimaLletraSeleccionada = lletresSeleccionades.last()
                val indexUltimaLletra = gridLayout.indexOfChild(ultimaLletraSeleccionada)
                val indexLletraActual = gridLayout.indexOfChild(textView)

                if (!sonLletresEnLaMateixaFila(indexUltimaLletra, indexLletraActual)) {
                    return
                }
            }
// SI LA LLETRA JA HA ESTAT SELECCIONADA, LA DESELECCIONA
            if (lletresSeleccionades.contains(textView)) {
                lletresSeleccionades.remove(textView)
                textView.setBackgroundColor(Color.parseColor("#FFFEDC"))
            } else {
                lletresSeleccionades.add(textView)
                textView.setBackgroundColor(Color.BLUE)
            }

            if (isParaulaCompletada(paraulaCastell)) {
                completarParaula(lletresSeleccionades)
                countCompletat++
            } else if (isParaulaCompletada(paraulaPluja)) {
                completarParaula(lletresSeleccionades)
                countCompletat++
            } else if (isParaulaCompletada(paraulaBolets)) {
                completarParaula(lletresSeleccionades)
                countCompletat++
            } else if (isParaulaCompletada(paraulaRosa)) {
                completarParaula(lletresSeleccionades)
                countCompletat++
            }

            if (countCompletat == 4) {
                mostrarMissatgeVictoria()
            }
        }
    }
    // Funció per a comprovar si les lletres estan a la mateixa fila
    private fun sonLletresEnLaMateixaFila(index1: Int, index2: Int): Boolean {
        val fila1 = index1 / gridLayout.columnCount
        val fila2 = index2 / gridLayout.columnCount

        return  fila1 == fila2
    }
// Funció per a comprovar si la paraula està completada
    private fun isParaulaCompletada(paraula: List<String>): Boolean {
        return lletresSeleccionades.map { it.text.toString() } == paraula
    }
// Funció per a completar la paraula
    private fun completarParaula(lletres: List<TextView>) {
        lletres.forEach {
            it.setBackgroundColor(Color.GREEN)
            it.setOnClickListener(null)
        }
        lletresSeleccionades.clear()
    }
// Funció per a mostrar el missatge de victòria
    private fun mostrarMissatgeVictoria() {


            CoroutineScope(Dispatchers.IO).launch {
                val tasca = roomTask(nivell = 6, punts = 1, completat = true)
                taskDao.insertLvl(tasca)
                taskDao.updateLvl(tasca)

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
}

