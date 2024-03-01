package com.zenonrodrigo.seriousgame

import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Nivell2 : AppCompatActivity() {
    private val imagesOrder = intArrayOf(
        R.drawable.historia1, R.drawable.historia2, R.drawable.historia3,
        R.drawable.historia4, R.drawable.historia5, R.drawable.historia6
    )

    private val correctOrder = imagesOrder.copyOf()

    private val dropZoneIds = intArrayOf(
        R.id.drop_zone_1, R.id.drop_zone_2, R.id.drop_zone_3,
        R.id.drop_zone_4, R.id.drop_zone_5, R.id.drop_zone_6
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nivell2)

        val dragListener = MyDragListener()

        for (id in dropZoneIds) {
            findViewById<View>(id).setOnDragListener(dragListener)
        }

        val imagesLayout = findViewById<LinearLayout>(R.id.images_layout)

        for (drawableId in imagesOrder) {
            val imageView = ImageView(this)
            imageView.setImageResource(drawableId)
            imageView.tag = drawableId // Set the resource ID as a tag
            imageView.setOnLongClickListener { v ->
                val dragShadow = View.DragShadowBuilder(v)
                v.startDragAndDrop(null, dragShadow, v, 0)
            }
            imagesLayout.addView(imageView)
        }
    }

    inner class MyDragListener : View.OnDragListener {
        override fun onDrag(v: View?, event: DragEvent?): Boolean {
            when (event?.action) {
                DragEvent.ACTION_DROP -> {
                    val dropTarget = v as LinearLayout
                    val dropped = event.localState as ImageView
                    val droppedImageId = dropped.tag as Int // Retrieve the resource ID from the tag
                    val dropTargetIndex = dropZoneIds.indexOf(dropTarget.id)

                    if (droppedImageId == correctOrder[dropTargetIndex]) {
                        dropTarget.removeAllViews()
                        dropTarget.addView(dropped)
                    } else {
                        Toast.makeText(this@Nivell2, "Orden incorrecto", Toast.LENGTH_SHORT).show()
                    }

                    if (isCompleted()) {
                        // Si se ha completado el orden correctamente, regresar a la actividad principal
                        onBackPressed()
                    }
                }
            }
            return true
        }
    }

    private fun isCompleted(): Boolean {
        for (i in 0 until correctOrder.size) {
            val dropZone = findViewById<LinearLayout>(dropZoneIds[i])
            val imageView = dropZone.getChildAt(0) as? ImageView
            val imageId = imageView?.tag as? Int ?: -1 // Retrieve the resource ID from the tag
            if (imageId != correctOrder[i]) {
                return false
            }
        }
        return true
    }
}
