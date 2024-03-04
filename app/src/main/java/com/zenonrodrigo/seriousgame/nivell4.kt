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
import com.zenonrodrigo.seriousgame.room.roomDao
import com.zenonrodrigo.seriousgame.room.roomTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class nivell4 : AppCompatActivity() {
    private var primerClicat: View? = null
    private var segonClicat: View? = null
    private var combinacionsCorrectes: Int = 0
    private lateinit var taskDao: roomDao
    private val handler = Handler(Looper.getMainLooper())
//combinacions de paraules amb imatges
    private val combinacions = mutableListOf(
        Combinacio("Castell", R.drawable.castelldeconte),
        Combinacio("Drac", R.drawable.drac),
        Combinacio("Rosa", R.drawable.rosa),
        Combinacio("Cavaller", R.drawable.cavaller),
        Combinacio("Cavall", R.drawable.cavall),
        Combinacio("Princesa", R.drawable.princesa),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nivell4)

        combinacions.shuffle()
        val db = (applicationContext as App).db
        taskDao = db.taskDao()

        val combinacionsAleatories = combinacions.shuffled()

        setupElement(textViewId = R.id.textViewElement1, imageViewId = R.id.imageViewElement1, combinacio = combinacionsAleatories[0])
        setupElement(textViewId = R.id.textViewElement2, imageViewId = R.id.imageViewElement2, combinacio = combinacionsAleatories[1])
        setupElement(textViewId = R.id.textViewElement3, imageViewId = R.id.imageViewElement3, combinacio = combinacionsAleatories[2])
    }
//configura els elements
    private fun setupElement(textViewId: Int, imageViewId: Int, combinacio: Combinacio) {
        val textViewElement = findViewById<TextView>(textViewId)
        val imageViewElement = findViewById<ImageView>(imageViewId)

        textViewElement.text = combinacio.paraula
        imageViewElement.setImageResource(combinacio.imatge)

        textViewElement.setOnClickListener {
            onElementClicked(textViewElement)
        }

        imageViewElement.setOnClickListener {
            onElementClicked(imageViewElement)
        }
    }
//quan es clica un element
    private fun onElementClicked(view: View) {
        if (view.tag == null && view.isEnabled) {
            if (primerClicat == null) {
                primerClicat = view
                view.setBackgroundColor(Color.YELLOW)
            } else if (segonClicat == null) {
                segonClicat = view
                view.setBackgroundColor(Color.YELLOW)

                if (esCombinacioCorrecta(primerClicat!!, segonClicat!!)) {
                    gestionarCombinacioCorrecta()
                } else {
                    gestionarCombinacioIncorrecta()
                }
            }
        }
    }
    //comprova si la combinacio es correcta
    private fun esCombinacioCorrecta(view1: View, view2: View): Boolean {
        val combinacio1 = obtenirCombinacio(view1)
        val combinacio2 = obtenirCombinacio(view2)

        return combinacio1 != null && combinacio2 != null &&
                (combinacio1.paraula == combinacio2.paraula && combinacio1.imatge == combinacio2.imatge)
    }
    //combina les paraules amb les imatges
    private fun obtenirCombinacio(view: View): Combinacio? {
        return when (view.id) {
            R.id.textViewElement1 -> combinacions[0]
            R.id.imageViewElement1 -> combinacions[0]
            R.id.textViewElement2 -> combinacions[1]
            R.id.imageViewElement2 -> combinacions[1]
            R.id.textViewElement3 -> combinacions[2]
            R.id.imageViewElement3 -> combinacions[2]
            else -> null
        }
    }
    //si la combinacio es correcta
    private fun gestionarCombinacioCorrecta() {
        primerClicat?.visibility = View.INVISIBLE
        segonClicat?.visibility = View.INVISIBLE

        Toast.makeText(this, "¡Correcte!", Toast.LENGTH_SHORT).show()
        combinacionsCorrectes++

        if (combinacionsCorrectes == 3) {
            mostrarMissatgeVictoria()
        }

        resetIncorrectViews()
    }
//si la combinacio es incorrecte
    private fun gestionarCombinacioIncorrecta() {
        primerClicat?.setBackgroundColor(Color.RED)
        segonClicat?.setBackgroundColor(Color.RED)
        Toast.makeText(this, "Incorrecte, prova de nou", Toast.LENGTH_SHORT).show()

        handler.postDelayed({
            resetIncorrectViews()
        }, 1000)
    }

    private fun resetIncorrectViews() {
        primerClicat?.setBackgroundColor(Color.TRANSPARENT)
        segonClicat?.setBackgroundColor(Color.TRANSPARENT)
        primerClicat = null
        segonClicat = null
    }

    private fun mostrarMissatgeVictoria() {
        CoroutineScope(Dispatchers.IO).launch {
            val tasca = roomTask(nivell = 4, punts = 1, completat = true)
            taskDao.insertLvl(tasca)
            taskDao.updateLvl(tasca)

            // Canviar el contexte a Dispatchers.Main per mostrar el diàleg
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

    data class Combinacio(val paraula: String, val imatge: Int)
}
