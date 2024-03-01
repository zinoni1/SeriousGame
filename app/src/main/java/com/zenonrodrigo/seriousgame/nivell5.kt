package com.zenonrodrigo.seriousgame

import android.os.Bundle
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class nivell5 : AppCompatActivity(), View.OnDragListener {

    private lateinit var phraseTextView: TextView
    private lateinit var dracImageView: ImageView
    private lateinit var solutionAnimal: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nivell5)

        // Inicializa las vistas
        phraseTextView = findViewById(R.id.textView)
        dracImageView = findViewById(R.id.drac)

        // Lista de animales y elección aleatoria
        val animals = listOf("porc", "gallina", "cabra")
        solutionAnimal = animals.random()

        // Texto de la frase
        val phrase = "Me quiero comer un $solutionAnimal ."

        // Setea el texto de la frase
        phraseTextView.text = phrase

        // Configura el oyente de arrastre para la imagen del dragón
        dracImageView.setOnDragListener(this)

        // Agrega el listener de drag para las imágenes de los animales
        val onTouchListener = View.OnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val dragData = View.DragShadowBuilder(view)
                view.startDragAndDrop(null, dragData, view, 0)
            }
            true
        }

        // Asigna los oyentes de arrastre para cada imagen de animal
        val animalImageViews = listOf(
            R.id.porc, R.id.porc1, R.id.porc2,
            R.id.gallina, R.id.gallina1,
            R.id.cabra, R.id.cabra1
        )

        // Configura el atributo 'tag' y el oyente de arrastre para cada imagen de animal
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

                    if (draggedAnimal == solutionAnimal) {
                        Toast.makeText(this, "¡Has ganado!", Toast.LENGTH_SHORT).show()
                        finish() // Finaliza la actividad
                    } else {
                        Toast.makeText(this, "Inténtalo de nuevo", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return true
    }
}
