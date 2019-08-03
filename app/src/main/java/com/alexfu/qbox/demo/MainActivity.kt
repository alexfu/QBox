package com.alexfu.qbox.demo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.alexfu.qbox.QBoxCameraActivity
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.scanButton).setOnClickListener {
            startActivity(Intent(this, QBoxCameraActivity::class.java))
        }
    }
}
