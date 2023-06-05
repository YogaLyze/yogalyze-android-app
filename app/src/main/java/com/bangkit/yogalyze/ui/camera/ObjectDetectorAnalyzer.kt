package com.bangkit.yogalyze.ui.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import java.nio.ByteBuffer

class ObjectDetectorAnalyzer (name : String): ImageAnalysis.Analyzer {

    private var objectDetectionResult: Float = 0.0f

    fun getObjectDetectionResult(): Float {
        return objectDetectionResult
    }


    override fun analyze(image: ImageProxy) {
        // Perform object detection on the image
        // You can use any object detection library or algorithm here
        // Process the detected objects and take appropriate actions
        // For example, draw bounding boxes on the image, extract object information, etc.

//        val gambar = imageProxyToBitmap(image)
        if (image != null){
            objectDetectionResult = 5.5F
        }
        image.close()
    }

//    private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
//        val planeProxy = image.planes[0]
//        val buffer: ByteBuffer = planeProxy.buffer
//        val bytes = ByteArray(buffer.remaining())
//        buffer.get(bytes)
//        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//    }


}