package io.github.x1111101101.glucoseserver.prescription.util.ocr

import org.opencv.core.*
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import java.io.File

class OpenCVUtil {

    init {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
    }

    fun loadPrescriptionVerticalLines(imageFile: File) {

    }

}


/**
 * Detect prescription doc area
 * and return bounding box
 */
private fun detectDocumentArea(image: Mat): Rect? {
    val gray = grayScale(image)
    val binary = Mat()
    Imgproc.adaptiveThreshold(gray, binary, 255.0, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 15, 10.0)

    val contours = mutableListOf<MatOfPoint>()
    val hierarchy = Mat()
    Imgproc.findContours(binary, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE)

    val docRect = contours.maxBy { Imgproc.contourArea(it) }.let { Imgproc.boundingRect(it) }
    releaseMats(gray, hierarchy, binary)
    if (docRect == null) {
        return null
    }
    return docRect
}

/**
 * return a mask wherein line bits are only remained without noises
 */
private fun emphasizeLines(docBinary: Mat, vertical: Boolean = true): Mat {
    var rectSizes = listOf(
        (1.0 to 5.0), (12.0 to 3.0), (2.0 to 10.0), (8.0 to 1.0), (1.0 to 4.0)
    )
    val repeats = arrayOf(15, 1, 3, 1, 20)
    val operations = listOf(Imgproc.MORPH_ERODE, Imgproc.MORPH_DILATE, Imgproc.MORPH_ERODE, Imgproc.MORPH_ERODE, Imgproc.MORPH_DILATE)
    if(!vertical) {
        rectSizes = rectSizes.map { it.second to it.first }
    }

    val morphed = Mat()
    var kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, rectSizes.first().run { Size(first, second) })
    repeat(repeats.first()) {
        Imgproc.morphologyEx(docBinary, morphed, operations.first(), kernel)
    }
    for(i in 1..rectSizes.lastIndex) {
        releaseMats(kernel)
        val rectSize = rectSizes[i]
        val operation = operations[i]
        val repeat = repeats[i]
        kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, rectSize.run { Size(first, second) })
        repeat(repeat) {
            Imgproc.morphologyEx(morphed, morphed, operation, kernel)
        }
    }
    releaseMats(kernel)
    return morphed
}

private fun grayScale(mat: Mat): Mat {
    val gray = Mat()
    Imgproc.cvtColor(mat, gray, Imgproc.COLOR_BGR2GRAY)
    return gray
}

private fun releaseMats(vararg mats: Mat) {
    mats.forEach {
        try {
            it.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}