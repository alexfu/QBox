package com.alexfu.qbox

import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import java.io.Serializable

data class QBoxBarcode(val rawValue: String? = null) : Serializable {
    constructor(barcode: FirebaseVisionBarcode) : this(rawValue = barcode.rawValue)
}
