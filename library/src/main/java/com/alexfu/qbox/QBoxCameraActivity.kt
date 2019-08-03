package com.alexfu.qbox

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Rational
import android.util.Size
import android.view.TextureView
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*

class QBoxCameraActivity : AppCompatActivity() {
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
        analysis.analyzer = QRAnalyzer()

        CameraX.bindToLifecycle(this, preview, analysis)
    }

    private fun createAnalyzerHandler(): Handler {
        val thread = HandlerThread("com.alexfu.qbox.QRAnalyzer")
        thread.start()
        return Handler(thread.looper)
    }

    private fun updateViewFinder(output: Preview.PreviewOutput) {
        val parent = viewFinder.parent as ViewGroup
        parent.removeView(viewFinder)
        parent.addView(viewFinder)
        viewFinder.surfaceTexture = output.surfaceTexture
    }

    private class QRAnalyzer : ImageAnalysis.Analyzer {
        override fun analyze(image: ImageProxy?, rotationDegrees: Int) {
            // TODO: Run image through ML Kit
        }
    }
}
