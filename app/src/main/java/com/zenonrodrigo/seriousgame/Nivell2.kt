package com.zenonrodrigo.seriousgame

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.zenonrodrigo.seriousgame.room.roomDao
import com.zenonrodrigo.seriousgame.room.roomTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Nivell2 : AppCompatActivity() {
    private lateinit var imageViews: List<ImageView>
    private lateinit var resultViews: List<ImageView>
    private var selectedImage: ImageView? = null
    private lateinit var taskDao: roomDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nivell2)
        val db = (applicationContext as App).db
        taskDao = db.taskDao()
        // Llista de les imatges arrossegables
        imageViews = listOf(
            findViewById(R.id.historia1),
            findViewById(R.id.historia2),
            findViewById(R.id.historia3),
            findViewById(R.id.historia4),
            findViewById(R.id.historia5),
            findViewById(R.id.historia6)
        )

        // Llista de les ImageView de destí
        resultViews = listOf(
            findViewById(R.id.ordenat1),
            findViewById(R.id.ordenat2),
            findViewById(R.id.ordenat3),
            findViewById(R.id.ordenat4),
            findViewById(R.id.ordenat5),
            findViewById(R.id.ordenat6)
        )

        // Establir OnClickListener per a les imatges arrossegables
        imageViews.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                selectedImage = imageView
                Toast.makeText(this@Nivell2, "Imatge seleccionada", Toast.LENGTH_SHORT).show()
            }
        }

        // Establir OnClickListener per a les ImageView de destí
        resultViews.forEachIndexed { index, resultView ->
            resultView.setOnClickListener {
                selectedImage?.let { selectedImageView ->
                    val drawable = selectedImageView.drawable
                    val correctImageView = imageViews[index]
                    if (selectedImageView == correctImageView) {
                        resultView.setImageDrawable(drawable)
                        selectedImage?.visibility = View.GONE // Ocultar la ImageView d'origen
                        selectedImage = null
                        if (checkAllImagesInPlace()) {
                            CoroutineScope(Dispatchers.IO).launch {
                                val task = roomTask(nivell = 2, punts = 1, completat = true)
                                taskDao.insertLvl(task)
                                taskDao.updateLvl(task)

                                withContext(Dispatchers.Main) {
                                    AlertDialog.Builder(this@Nivell2)
                                        .setTitle("Felicitats!")
                                        .setMessage("Has completat el joc.")
                                        .setPositiveButton("Acceptar") { _, _ ->
                                            finish()
                                        }
                                        .show()
                                }
                            }
                        } else {
                            showMessage("Bé fet! Has col·locat una imatge al lloc correcte.")
                        }
                    } else {
                        showMessage("Selecciona una imatge correcta per a aquest espai!")
                    }
                }
            }
        }

    }

    // Comprovar si totes les imatges estan al seu lloc correcte
    private fun checkAllImagesInPlace(): Boolean {
        return imageViews.zip(resultViews).all { (imageView, resultView) ->
            val imageViewDrawable = imageView.drawable
            val resultViewDrawable = resultView.drawable
            imageViewDrawable != null && resultViewDrawable != null &&
                    imageViewDrawable.constantState == resultViewDrawable.constantState
        }
    }

    // Mostrar un missatge
    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
