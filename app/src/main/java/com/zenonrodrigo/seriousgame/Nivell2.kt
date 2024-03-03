package com.zenonrodrigo.seriousgame

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Nivell2 : AppCompatActivity() {
    private lateinit var imageViews: List<ImageView>
    private lateinit var resultViews: List<ImageView>
    private var selectedImage: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nivell2)

        // Lista de las imágenes arrastrables
        imageViews = listOf(
            findViewById(R.id.historia1),
            findViewById(R.id.historia2),
            findViewById(R.id.historia3),
            findViewById(R.id.historia4),
            findViewById(R.id.historia5),
            findViewById(R.id.historia6)
        )

        // Lista de las ImageView de destino
        resultViews = listOf(
            findViewById(R.id.ordenat1),
            findViewById(R.id.ordenat2),
            findViewById(R.id.ordenat3),
            findViewById(R.id.ordenat4),
            findViewById(R.id.ordenat5),
            findViewById(R.id.ordenat6)
        )

        // Establecer OnClickListener para las imágenes arrastrables
        imageViews.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                selectedImage = imageView
                Toast.makeText(this@Nivell2, "Imagen seleccionada", Toast.LENGTH_SHORT).show()
            }
        }

        // Establecer OnClickListener para las ImageView de destino
        // Establecer OnClickListener para las ImageView de destino
        resultViews.forEachIndexed { index, resultView ->
            resultView.setOnClickListener {
                selectedImage?.let { selectedImageView ->
                    val drawable = selectedImageView.drawable
                    val correctImageView = imageViews[index]
                    if (selectedImageView == correctImageView) {
                        resultView.setImageDrawable(drawable)
                        selectedImage?.visibility = View.GONE // Ocultar el ImageView de origen
                        selectedImage = null
                        if (checkAllImagesInPlace()) {
                            showMessage("¡Felicidades! Has completado el rompecabezas.")
                            finish()
                        } else {
                            showMessage("¡Bien hecho! Has colocado una imagen en su lugar correcto.")
                        }
                    } else {
                        showMessage("¡Selecciona una imagen correcta para este espacio!")
                    }
                }
            }
        }

    }

    // Comprobar si todas las imágenes están en su lugar correcto
    private fun checkAllImagesInPlace(): Boolean {
        return imageViews.zip(resultViews).all { (imageView, resultView) ->
            val imageViewDrawable = imageView.drawable
            val resultViewDrawable = resultView.drawable
            imageViewDrawable != null && resultViewDrawable != null &&
                    imageViewDrawable.constantState == resultViewDrawable.constantState
        }
    }


    // Mostrar un mensaje
    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
