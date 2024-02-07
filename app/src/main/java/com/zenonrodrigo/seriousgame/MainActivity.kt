package com.zenonrodrigo.seriousgame

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {

    private lateinit var draggableCard1: MaterialCardView
    private lateinit var draggableCard2: MaterialCardView
    private lateinit var draggableCard3: MaterialCardView
    private lateinit var draggableCard4: MaterialCardView
    private lateinit var parentContainer: DraggableCoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar las vistas
        draggableCard1 = findViewById(R.id.draggableCard1)
        draggableCard2 = findViewById(R.id.draggableCard2)
        draggableCard3 = findViewById(R.id.draggableCard3)
        draggableCard4 = findViewById(R.id.draggableCard4)
        parentContainer = findViewById(R.id.parentCoordinatorLayout)

        // Agregar las vistas arrastrables al DraggableCoordinatorLayout
        parentContainer.addDraggableChild(draggableCard1)
        parentContainer.addDraggableChild(draggableCard2)
        parentContainer.addDraggableChild(draggableCard3)
        parentContainer.addDraggableChild(draggableCard4)

        // Establecer un ViewDragListener personalizado
        parentContainer.setViewDragListener(object : DraggableCoordinatorLayout.ViewDragListener {
            override fun onViewCaptured(view: View, i: Int) {
                // Establecer la bandera isDragged en true para la MaterialCardView correspondiente
                when (view.id) {
                    R.id.draggableCard1 -> draggableCard1.isDragged = true
                    R.id.draggableCard2 -> draggableCard2.isDragged = true
                    R.id.draggableCard3 -> draggableCard3.isDragged = true
                    R.id.draggableCard4 -> draggableCard4.isDragged = true
                }
            }

            override fun onViewReleased(view: View, v: Float, v1: Float) {
                // Establecer la bandera isDragged en false para la MaterialCardView correspondiente
                when (view.id) {
                    R.id.draggableCard1 -> draggableCard1.isDragged = false
                    R.id.draggableCard2 -> draggableCard2.isDragged = false
                    R.id.draggableCard3 -> draggableCard3.isDragged = false
                    R.id.draggableCard4 -> draggableCard4.isDragged = false
                }
            }
        })
    }
}
