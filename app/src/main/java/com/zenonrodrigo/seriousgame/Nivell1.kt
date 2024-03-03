package com.zenonrodrigo.seriousgame

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.zenonrodrigo.seriousgame.room.roomDao
import com.zenonrodrigo.seriousgame.room.roomTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Nivell1 : AppCompatActivity() {
    private lateinit var taskDao: roomDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nivell1)
        val db = (applicationContext as App).db
        taskDao = db.taskDao()
        val videoView = findViewById<VideoView>(R.id.video_view)
        val videoPath = "android.resource://" + packageName + "/" + R.raw.video_santjordi
        val uri = Uri.parse(videoPath)
        videoView.setVideoURI(uri)

        val mediaController = MediaController(this)
        videoView.setMediaController(mediaController)
        mediaController.setAnchorView(videoView)

        videoView.setOnCompletionListener {


            CoroutineScope(Dispatchers.IO).launch {
                val task = roomTask(nivell = 1, punts = 1, completat = true)
                taskDao.insertLvl(task)
                taskDao.updateLvl(task)

                withContext(Dispatchers.Main) {
                    AlertDialog.Builder(this@Nivell1)
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


}
