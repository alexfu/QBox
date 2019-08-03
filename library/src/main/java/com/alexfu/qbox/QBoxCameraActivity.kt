package com.alexfu.qbox

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Rational
import android.util.Size
import android.view.TextureView
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata

private const val KEY_BARCODE = "barcode"

class QBoxCameraActivity : AppCompatActivity(), BarcodeDetectionCallback {
    private val viewFinder by lazy<TextureView> { findViewById(R.id.com_alexfu_qbox__view_finder) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.com_alexfu_qbox__activity_camera)

        // Configure preview use case
        val previewConfig = PreviewConfig.Builder()
            .setTargetAspectRatio(Rational(1, 1))
            .setTargetResolution(Size(640, 640))
            .setLensFacing(CameraX.LensFacing.BACK)
            .build()

        val preview = Preview(previewConfig)
        preview.setOnPreviewOutputUpdateListener(::updateViewFinder)

        // Configure analysis use case
        val analyzerConfig = ImageAnalysisConfig.Builder()
            .setCallbackHandler(createAnalyzerHandler())
            .setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
            .build()

        val analysis = ImageAnalysis(analyzerConfig)
        analysis.analyzer = QRAnalyzer(this)

        CameraX.bindToLifecycle(this, preview, analysis)
    }

    override fun onBarcodeDetected(barcodes: List<FirebaseVisionBarcode>) {
        val barcode = QBoxBarcode(barcodes.first())

        val data = Intent()
        data.putExtra(KEY_BARCODE, barcode)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    private fun createAnalyzerHandler(): Handler {
        val thread = HandlerThread("com.alexfu.qbox.QRAnalyzer")
        thread.start()
        return Handler(thread.looper)
    }

    private fun updateViewFinder(output: Preview.PreviewOutput) {
        val parent = viewFinder.parent as ViewGroup
        parent.removeView(viewFinder)
        parent.addView(viewFinder, 0)
        viewFinder.surfaceTexture = output.surfaceTexture
    }
}

private class QRAnalyzer(private val callback: BarcodeDetectionCallback) : ImageAnalysis.Analyzer {
    private val barcodeDetector: FirebaseVisionBarcodeDetector
    private val taskSuccessListener: OnSuccessListener<List<FirebaseVisionBarcode>>

    init {
        // Setup barcode detector
        val options = FirebaseVisionBarcodeDetectorOptions.Builder()
            .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
            .build()

        barcodeDetector = FirebaseVision.getInstance().getVisionBarcodeDetector(options)

        // Set up listener
        taskSuccessListener = OnSuccessListener { barcodes ->
            if (barcodes.isNotEmpty()) {
                callback.onBarcodeDetected(barcodes)
            }
        }
    }

    override fun analyze(imageProxy: ImageProxy?, rotationDegrees: Int) {
        val mediaImage = imageProxy?.image
        if (mediaImage != null) {
            val imageRotation = degreesToFirebaseRotation(rotationDegrees)
            val image = FirebaseVisionImage.fromMediaImage(mediaImage, imageRotation)

            val task = barcodeDetector.detectInImage(image)
            task.addOnSuccessListener(taskSuccessListener)
        }
    }

    private fun degreesToFirebaseRotation(degrees: Int): Int = when(degrees) {
        0 -> FirebaseVisionImageMetadata.ROTATION_0
        90 -> FirebaseVisionImageMetadata.ROTATION_90
        180 -> FirebaseVisionImageMetadata.ROTATION_180
        270 -> FirebaseVisionImageMetadata.ROTATION_270
        else -> throw Exception("Rotation must be 0, 90, 180, or 270.")
    }
}

private interface BarcodeDetectionCallback {
    fun onBarcodeDetected(barcodes: List<FirebaseVisionBarcode>)
}
