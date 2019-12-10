package com.alexfu.qbox

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Rational
import android.util.Size
import android.view.TextureView
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata

class QBoxCamera : BaseQBoxCamera() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.com_alexfu_qbox__activity_camera)
        setViewFinder(findViewById(R.id.com_alexfu_qbox__view_finder))
        startPreview()
    }

    companion object {
        fun start(activity: Activity, requestCode: Int) {
            val intent = Intent(activity, QBoxCamera::class.java)
            activity.startActivityForResult(intent, requestCode)
        }

        fun start(fragment: Fragment, requestCode: Int) {
            val intent = Intent(fragment.requireContext(), QBoxCamera::class.java)
            fragment.startActivityForResult(intent, requestCode)
        }

        fun getBarcode(data: Intent?): QBoxBarcode {
            return data?.getSerializableExtra(KEY_BARCODE) as? QBoxBarcode ?: QBoxBarcode()
        }
    }
}
