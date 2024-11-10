package io.github.x1111101101.glucoseserver.prescription.util.ocr

import org.opencv.core.*
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import java.io.File

class OpenCVUtil {

    init {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
    }

    fun loadPrescriptionVerticalLines(imageFile: File): List<Pair<Point, Point>> {
        val image = Imgcodecs.imread(imageFile.absolutePath)
        var (docArea, docGray, docBinary, linesEmphasized) = arrayOfNulls<Mat>(4)
        try {
            val docAreaBB = detectDocumentArea(image)
            docArea = Mat(image, docAreaBB)
            docGray = grayScale(docArea)
            docBinary = Mat()
            Imgproc.adaptiveThreshold(docGray, docBinary, 255.0, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 15, 10.0)
            linesEmphasized = emphasizeLines(docBinary)
            return detectLinesFromBinary(docBinary)
        } finally {
            releaseMats(image, docArea, linesEmphasized, docBinary, image)
        }
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
 * Detect lines and point of lines from binary image wherein lines are emphasized
 */
fun detectLinesFromBinary(binaryMat: Mat): List<Pair<Point, Point>> {
    val linesList = mutableListOf<Pair<Point, Point>>()
    val lines = Mat()
    Imgproc.HoughLinesP(binaryMat, lines, 1.0, Math.PI / 180, 100, 50.0, 10.0)
    for (i in 0 until lines.rows()) {
        val l = lines[i, 0].map { it.toInt() }
        val (x1, y1, x2, y2) = l
        val start = Point(x1.toDouble(), y1.toDouble())
        val end = Point(x2.toDouble(), y2.toDouble())
        linesList.add(start to end)
    }
    releaseMats(lines)
    return linesList
}

/**
 * return a mask wherein line bits are only remained without noises
 */
private fun emphasizeLines(docBinary: Mat, vertical: Boolean = true): Mat {
    var rectSizes = listOf(
        (1.0 to 5.0), (12.0 to 3.0), (2.0 to 10.0), (8.0 to 1.0), (1.0 to 4.0)
    )
    val repeats = arrayOf(15, 1, 3, 1, 20)
    val operations = listOf(
        Imgproc.MORPH_ERODE,
        Imgproc.MORPH_DILATE,
        Imgproc.MORPH_ERODE,
        Imgproc.MORPH_ERODE,
        Imgproc.MORPH_DILATE
    )
    if (!vertical) {
        rectSizes = rectSizes.map { it.second to it.first }
    }

    val morphed = Mat()
    var kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, rectSizes.first().run { Size(first, second) })
    repeat(repeats.first()) {
        Imgproc.morphologyEx(docBinary, morphed, operations.first(), kernel)
    }
    for (i in 1..rectSizes.lastIndex) {
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

private fun releaseMats(vararg mats: Mat?) {
    mats.forEach {
        try {
            it?.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}