package com.zenonrodrigo.seriousgame

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.android.material.card.MaterialCardView
import com.zenonrodrigo.seriousgame.sopadelletres.Sopa


class MainActivity : AppCompatActivity() {

    //private lateinit var draggableCard1: MaterialCardView
    //private lateinit var draggableCard2: MaterialCardView
    //private lateinit var draggableCard3: MaterialCardView
    //private lateinit var draggableCard4: MaterialCardView
    //private lateinit var parentContainer: DraggableCoordinatorLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Puedes cargar la vista de inicio o iniciar otra actividad aquí
        // Por ejemplo, si tienes una pantalla de inicio, podrías hacer algo así:
        setContentView(R.layout.activity_main)

        //boton 3 intent al pulsar
        val boton5 = findViewById<View>(R.id.lvl5)
        val boton1 = findViewById<View>(R.id.lvl1)
        val boton3 = findViewById<View>(R.id.lvl3)
        val boton7 = findViewById<View>(R.id.lvl7)
        boton1.setOnClickListener {
            val intent = Intent(this, Nivell1::class.java)
            startActivity(intent)
        }
        boton3.setOnClickListener {
            val intent = Intent(this, Nivell3::class.java)
            startActivity(intent)
        }
        boton7.setOnClickListener {
            val intent = Intent(this, Sopa::class.java)
            startActivity(intent)
        }
        boton5.setOnClickListener {
            val intent = Intent(this, nivell5::class.java)
            startActivity(intent)
        }

        // Inicializar las vistas

        // Agregar las vistas arrastrables al DraggableCoordinatorLayout
        //parentContainer.addDraggableChild(draggableCard1)
        // parentContainer.addDraggableChild(draggableCard2)
        //parentContainer.addDraggableChild(draggableCard3)
        // parentContainer.addDraggableChild(draggableCard4)

        // Establecer un ViewDragListener personalizado
        //  parentContainer.setViewDragListener(object : DraggableCoordinatorLayout.ViewDragListener {
        // override fun onViewCaptured(view: View, i: Int) {
                // Establecer la bandera isDragged en true para la MaterialCardView correspondiente
        // when (view.id) {
        //    R.id.draggableCard1 -> draggableCard1.isDragged = true
        //   R.id.draggableCard2 -> draggableCard2.isDragged = true
        //   R.id.draggableCard3 -> draggableCard3.isDragged = true
        //    R.id.draggableCard4 -> draggableCard4.isDragged = true
        //  }
        // }

        // override fun onViewReleased(view: View, v: Float, v1: Float) {
                // Establecer la bandera isDragged en false para la MaterialCardView correspondiente
        //     when (view.id) {
        //     R.id.draggableCard1 -> draggableCard1.isDragged = false
        //     R.id.draggableCard2 -> draggableCard2.isDragged = false
        //     R.id.draggableCard3 -> draggableCard3.isDragged = false
        //     R.id.draggableCard4 -> draggableCard4.isDragged = false
        //  }
        // }
        // })

    }
}
