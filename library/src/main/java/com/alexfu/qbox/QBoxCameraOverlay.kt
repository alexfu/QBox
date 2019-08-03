package com.alexfu.qbox

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class QBoxCameraOverlay @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val overlayPath = Path()
    private val holePath = Path()

    init {
        paint.color = Color.BLACK
        paint.alpha = (255 * 0.8f).toInt()
        paint.style = Paint.Style.FILL

        strokePaint.color = Color.WHITE
        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = resources.getDimension(R.dimen.com_alexfu_qbox__overlay_stroke_width)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        // Make path for the hole
        val holeSize = measuredWidth * .8f
        val holeCornerRadius = holeSize * .05f
        val holeLeft = (measuredWidth / 2) - (holeSize / 2)
        val holeTop = (measuredHeight / 2) - (holeSize / 2)
        val holeRight = holeLeft + holeSize
        val holeBottom = holeTop + holeSize
        holePath.reset()
        holePath.addRoundRect(holeLeft, holeTop, holeRight, holeBottom, holeCornerRadius, holeCornerRadius, Path.Direction.CW)

        // Make path for the overlay
        overlayPath.reset()
        overlayPath.addRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), Path.Direction.CW)

        // Create hole punch in overlay
        overlayPath.op(holePath, Path.Op.DIFFERENCE)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(overlayPath, paint)
        canvas.drawPath(holePath, strokePaint)
    }
}
