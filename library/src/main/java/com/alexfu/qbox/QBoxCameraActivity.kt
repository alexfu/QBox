package com.alexfu.qbox

import android.os.Bundle
import android.util.Rational
import android.util.Size
import android.view.TextureView
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraX
import androidx.camera.core.Preview
import androidx.camera.core.PreviewConfig

class QBoxCameraActivity : AppCompatActivity() {
    private val viewFinder by lazy<TextureView> { findViewById(R.id.com_alexfu_qbox__view_finder) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.com_alexfu_qbox__activity_camera)

        val previewConfig = PreviewConfig.Builder()
            .setTargetAspectRatio(Rational(1, 1))
            .setTargetResolution(Size(640, 640))
            .setLensFacing(CameraX.LensFacing.BACK)
            .build()

        val preview = Preview(previewConfig)
        preview.setOnPreviewOutputUpdateListener(::updateViewFinder)

        CameraX.bindToLifecycle(this, preview)
    }

    private fun updateViewFinder(output: Preview.PreviewOutput) {
        val parent = viewFinder.parent as ViewGroup
        parent.removeView(viewFinder)
        parent.addView(viewFinder)
        viewFinder.surfaceTexture = output.surfaceTexture
    }
}
