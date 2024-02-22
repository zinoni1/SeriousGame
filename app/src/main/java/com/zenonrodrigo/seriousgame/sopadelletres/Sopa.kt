package com.zenonrodrigo.seriousgame.sopadelletres

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.zenonrodrigo.seriousgame.R
class Sopa : AppCompatActivity() {
    private val letras = listOf(
        "A", "B", "C", "D", "E", "F", "G", "H",
        "I", "J", "K", "L", "M", "N", "O", "P",
        "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    )
    private val palabraCasatanya = listOf("C", "A", "S", "T", "A", "N", "Y", "A")
    private val palabraPluja = listOf("P", "L", "U", "J", "A")
    private val palabraBolets = listOf("B", "O", "L", "E", "T", "S")
    private val palabraFulla = listOf("F", "U", "L", "L", "A")

    private val letrasSeleccionadas = mutableListOf<TextView>()
    private var countCompletat = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sopa_de_lletres)

        val gridLayout: GridLayout = findViewById(R.id.gridLayout)

        // FILA ALEATORIA PER CADA FILA
        val filaAleatoriaCasatanya = (0 until gridLayout.rowCount).shuffled().first()
        val filaAleatoriaPluja = (0 until gridLayout.rowCount).filter { it != filaAleatoriaCasatanya }.shuffled().first()
        val filaAleatoriaBolets = (0 until gridLayout.rowCount).filter { it != filaAleatoriaCasatanya && it != filaAleatoriaPluja }.shuffled().first()
        val filaAleatoriaFulla = (0 until gridLayout.rowCount).filter { it != filaAleatoriaCasatanya && it != filaAleatoriaPluja && it != filaAleatoriaBolets }.shuffled().first()

        // OMPLIR FILES AMB CADA PARAULA
        omplirFila(gridLayout, filaAleatoriaCasatanya, palabraCasatanya)
        omplirFila(gridLayout, filaAleatoriaPluja, palabraPluja)
        omplirFila(gridLayout, filaAleatoriaBolets, palabraBolets)
        omplirFila(gridLayout, filaAleatoriaFulla, palabraFulla)

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
            // VERIFICA SI LA LLETRA JA HA SIGUT SELECCIONADA
            if (letrasSeleccionadas.contains(textView)) {
                // Deselecciona la LLETRA I LA  PINTA DE GRIS
                letrasSeleccionadas.remove(textView)
                textView.setBackgroundColor(Color.parseColor("#FAFAFA")) // Cambiar el color de nuevo a gris claro
            } else {
                // PINTA EL FONS CLICKAT DE BLAU
                letrasSeleccionadas.add(textView)
                textView.setBackgroundColor(Color.BLUE) // Cambiar el color a azul cuando se selecciona
            }

            // MIRA QUE S'HAGI COMPLETAT CADA PARAULA
            if (isWordComplete(palabraCasatanya)) {
                letrasSeleccionadas.forEach { it.setBackgroundColor(Color.GREEN) }
                letrasSeleccionadas.clear()
                textView.setOnClickListener(null) // Desactivar clic para letras de la palabra completada
                countCompletat++
            } else if (isWordComplete(palabraPluja)) {
                letrasSeleccionadas.forEach { it.setBackgroundColor(Color.GREEN) }
                letrasSeleccionadas.clear()
                textView.setOnClickListener(null)
                countCompletat++
            } else if (isWordComplete(palabraBolets)) {
                letrasSeleccionadas.forEach { it.setBackgroundColor(Color.GREEN) }
                letrasSeleccionadas.clear()
                textView.setOnClickListener(null)
                countCompletat++
            } else if (isWordComplete(palabraFulla)) {
                letrasSeleccionadas.forEach { it.setBackgroundColor(Color.GREEN) }
                letrasSeleccionadas.clear()
                textView.setOnClickListener(null)
                countCompletat++
            }

            // SI ES IGUAL A 4 PORTA A LA FUNCIÓ DE MISSAATGE DE VICTORIA
            if (countCompletat == 4) {
                mostrarMensajeVictoria()
            }
        }
    }

    private fun isWordComplete(palabra: List<String>): Boolean {
        return letrasSeleccionadas.map { it.text.toString() } == palabra
    }

    private fun mostrarMensajeVictoria() {
        AlertDialog.Builder(this)
            .setTitle("¡Felicitats!")
            .setMessage("Has completat totes les paraules.")
            .setPositiveButton("Acceptar") { _, _ ->
                finish()
            }
            .show()
    }
}
