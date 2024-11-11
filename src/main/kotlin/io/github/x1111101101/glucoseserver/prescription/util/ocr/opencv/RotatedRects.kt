package io.github.x1111101101.glucoseserver.prescription.util.ocr.opencv

import org.opencv.core.*
import org.opencv.imgproc.Imgproc

fun getOBBContours(binaryMat: Mat): List<RotatedRect> {
    val contours = mutableListOf<MatOfPoint>()
    val hierarchy = Mat()
    Imgproc.findContours(binaryMat, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE)
    val rotatedRects = mutableListOf<RotatedRect>()
    for (contour in contours) {
        val contourPoints = MatOfPoint2f(*contour.toArray())
        val rotatedRect = Imgproc.minAreaRect(contourPoints)
        rotatedRects.add(rotatedRect)
    }
    return rotatedRects
}


fun rotatedRectIntersectionArea(rect1: RotatedRect, rect2: RotatedRect): Float {
    val vertices1 = arrayOf(Point(), Point(), Point(), Point())
    val vertices2 = arrayOf(Point(), Point(), Point(), Point())
    rect1.points(vertices1)
    rect2.points(vertices2)

    val poly1 = MatOfPoint2f(*vertices1)
    val poly2 = MatOfPoint2f(*vertices2)
    val intersectionPoly = MatOfPoint2f()
    val intersectionArea = Imgproc.intersectConvexConvex(poly1, poly2, intersectionPoly)
    poly1.release()
    poly2.release()
    return intersectionArea
}