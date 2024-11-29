package io.github.x1111101101.glucoseserver.prescription.util.ocr

import com.google.cloud.vision.v1.EntityAnnotation
import io.github.x1111101101.glucoseserver.prescription.model.api.PrescriptionOcrResult
import io.github.x1111101101.glucoseserver.prescription.model.internal.ocr.callGVO
import io.github.x1111101101.glucoseserver.prescription.util.ocr.opencv.*
import org.opencv.core.Point
import org.opencv.core.Rect
import org.opencv.core.RotatedRect
import org.opencv.core.Scalar
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import java.io.File


object PrescriptionOcrUtil {

    /**
     * proc:
     * - crop document area
     * - match bounding boxes
     * -
     */
    fun parse(imagefile: File): PrescriptionOcrResult {
        val documentAreaFile = File.createTempFile("serviceserver", ".jpg").also { it.deleteOnExit() }
        OpenCVUtil.saveDocumentArea(imagefile, documentAreaFile)
        val response = callGVO(documentAreaFile.readBytes())
        val textAnnotations = response.responsesList[0].textAnnotationsList
        val boxes = textAnnotations.map { it.toBox() to it }.sortedBy { it.first.y.min }
            .filter { it.first.y.run { max - min } < 100 }
        val mat = Imgcodecs.imread(documentAreaFile.absolutePath) // 시각화용
        val mat2 = Imgcodecs.imread(documentAreaFile.absolutePath) // 시각화용

        boxes.forEach {
            val (bb, ann) = it
            val (x1, x2) = bb.x
            val (y1, y2) = bb.y
            val textRect = Rect(x1, y1, x2 - x1, y2 - y1)
            Imgproc.rectangle(mat2, textRect, Scalar(0.0, 0.0, 255.0), 2)
        }
        imshow("TEXTS", mat2)

        val cells = OpenCVUtil.detectTableCells(documentAreaFile)
        val groups = group(cells, boxes)
        groups.forEach {
            val colorR = Math.random() * 255

            val color = Scalar(colorR, 255 - colorR, Math.random() * 150 + 50)
            val (cell, texts) = it
            drawRotatedRect(mat, cell, color)
            texts.forEach {
                val bb = it.first
                val (x1, x2) = bb.x
                val (y1, y2) = bb.y
                val textRect = Rect(x1, y1, x2 - x1, y2 - y1)
                Imgproc.rectangle(mat, textRect, color, 2)
            }
        }
        imshow("matched", mat)
        return PrescriptionOcrResult(emptyList())
    }

    private fun group(cells: List<RotatedRect>, textBoxes: List<Pair<OcrTest1.Box, EntityAnnotation>>) = run {
        val array = Array(cells.size) { ArrayList<Pair<OcrTest1.Box, EntityAnnotation>>() }

        fun findMaxIntersected(bb: OcrTest1.Box): Int {
            val (x1, x2) = bb.x
            val (y1, y2) = bb.y
            val textRect = Rect(x1, y1, x2 - x1, y2 - y1)
            var result = -1
            var max = 0
            cells.forEachIndexed { index, it ->
                val area =
                    calculateRectIntersectionArea(it.boundingRect(), textRect)// calculateIntersectionArea(it, textRect)
                if (area > max) {
                    max = area
                    result = index
                }
            }
            return result
        }

        textBoxes.forEach {
            val bb = it.first
            val maxIdx = findMaxIntersected(bb)
            if (maxIdx >= 0) {
                array[maxIdx] += it
            } else {
                println("Mismatch: ${it.second.description}")
            }
        }

        return@run array.mapIndexed { index, pairs -> cells[index] to pairs }.filter { it.second.isNotEmpty() }.toList()
    }

}

