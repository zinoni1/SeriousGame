package com.zenonrodrigo.seriousgame

import android.os.Bundle
import android.view.DragEvent
import android.view.MotionEvent
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

class nivell5 : AppCompatActivity(), View.OnDragListener {

    private lateinit var fraseTextView: TextView
    private lateinit var dracImageView: ImageView
    private lateinit var animalSolucio: String
    private lateinit var taskDao: roomDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nivell5)

        // Inicialitza les vistes
        fraseTextView = findViewById(R.id.textView)
        dracImageView = findViewById(R.id.drac)
        val db = (applicationContext as App).db
        taskDao = db.taskDao()
        // Llista d'animals i elecció aleatòria
        val animals = listOf("porc", "gallina", "cabra")
        animalSolucio = animals.random()

        // Text de la frase
        val frase = "Vull menjar-me un/a $animalSolucio."

        // Defineix el text de la frase
        fraseTextView.text = frase

        dracImageView.setOnDragListener(this)

        val onTouchListener = View.OnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val dragData = View.DragShadowBuilder(view)
                view.startDragAndDrop(null, dragData, view, 0)
            }
            true
        }

        val animalImageViews = listOf(
            R.id.porc, R.id.porc1, R.id.porc2,
            R.id.gallina, R.id.gallina1,
            R.id.cabra, R.id.cabra1
        )

        for (imageViewId in animalImageViews) {
            val imageView = findViewById<ImageView>(imageViewId)
            imageView.tag = imageView.contentDescription?.toString() ?: ""
            imageView.setOnTouchListener(onTouchListener)
        }
    }

    override fun onDrag(v: View?, event: DragEvent?): Boolean {
        when (event?.action) {
            DragEvent.ACTION_DROP -> {
                val dropTarget = v as? ImageView

                if (dropTarget != null && dropTarget == dracImageView) {
                    val draggedView = event.localState as? ImageView
                    val draggedAnimal = draggedView?.tag as? String

                    if (draggedAnimal == animalSolucio) {
                        CoroutineScope(Dispatchers.IO).launch {
                            val tasca = roomTask(nivell = 5, punts = 1, completat = true)
                            taskDao.insertLvl(tasca)
                            taskDao.updateLvl(tasca)

                            withContext(Dispatchers.Main) {
                                AlertDialog.Builder(this@nivell5)
                                    .setTitle("¡Felicitats!")
                                    .setMessage("Has completat el joc.")
                                    .setPositiveButton("Acceptar") { _, _ ->
                                        finish()
                                    }
                                    .show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "Intenta-ho de nou", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return true
    }
}
