package com.alexfu.qbox.demo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alexfu.qbox.QBoxCameraActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.scanButton).setOnClickListener {
            QBoxCameraActivity.start(this, 123)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123 && resultCode == Activity.RESULT_OK) {
            val barCode = QBoxCameraActivity.getBarcode(data)
            Toast.makeText(this, barCode.rawValue, Toast.LENGTH_LONG).show()
        }
    }
}
