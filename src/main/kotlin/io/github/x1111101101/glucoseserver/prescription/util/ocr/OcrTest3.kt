package io.github.x1111101101.glucoseserver.prescription.util.ocr

import com.google.cloud.vision.v1.EntityAnnotation
import io.github.x1111101101.glucoseserver.prescription.model.internal.ocr.callGVO
import io.github.x1111101101.glucoseserver.prescription.util.ocr.OcrTest1.Box
import io.github.x1111101101.glucoseserver.prescription.util.ocr.opencv.OpenCVUtil
import io.github.x1111101101.glucoseserver.prescription.util.ocr.opencv.drawRotatedRect
import io.github.x1111101101.glucoseserver.prescription.util.ocr.opencv.imshow
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import java.io.File
import java.util.*

/**
 * 1. 문서 영역만 잘라냄
 * 2.
 */
object OcrTest3 {

    fun test(imageFile: File) {
        var tempFile = File.createTempFile("serviceserver", ".jpg").also { it.deleteOnExit() }
        tempFile = File("C:\\Users\\Sangyeob\\Desktop\\p_ocr\\test2", "temp.jpg")
        OpenCVUtil.saveDocumentArea(imageFile, tempFile)
        val lines = OpenCVUtil.detectVerticalLines(tempFile)

        val response = callGVO(tempFile.readBytes())
        val textAnnotations = response.responsesList[0].textAnnotationsList
        val visit = HashSet<UUID>()
        val merged = ArrayList<Pair<Box, Set<EntityAnnotation>>>()
        val boxes = textAnnotations.map { it.toBox() to it }.sortedBy { it.first.y.min }
            .filter { it.first.y.run { max - min } < 100 }
        Thread.sleep(1000)
        val mat = Imgcodecs.imread(tempFile.absolutePath)


        fun isEnglish(c: Char): Boolean {
            return c in 'A'..'Z' || c in 'a'..'z'
        }

        boxes.forEach { box->
            val (bb, annotation) = box
            val chars = annotation.description.map { if(it.isDigit() || isEnglish(it) || it.isWhitespace()) it else '!' }.toTypedArray()
            val string = StringBuilder().also { chars.forEach { c-> it.append(c) } }.toString()
            println("DD: " + string)
            val (x1,x2) = bb.x
            val (y1, y2) = bb.y

            Imgproc.rectangle(mat, Point(x1.toDouble(), y1.toDouble()), Point(x2.toDouble(), y2.toDouble()), Scalar(100.0))
            //Imgproc.putText(mat, string, Point(bb.x.min.toDouble(), bb.y.min.toDouble()), Imgproc.FONT_HERSHEY_SIMPLEX, 1.0, Scalar(50.0))
        }
        lines.forEach {
            //Imgproc.line(mat, it.first, it.second, Scalar(0.0, 0.0, 255.0))
        }
        val cells = OpenCVUtil.detectTableCells(tempFile)
        cells.forEach { obb->
            drawRotatedRect(mat, obb)
        }
        imshow("TEST", mat)
        val outputFile = File("C:\\Users\\Sangyeob\\Desktop\\p_ocr\\test2", "test.png")
        Imgcodecs.imwrite(outputFile.absolutePath, mat)
        println("lineCount: ${lines.size}")
    }

}