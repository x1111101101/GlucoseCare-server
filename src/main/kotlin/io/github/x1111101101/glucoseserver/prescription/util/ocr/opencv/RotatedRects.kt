package io.github.x1111101101.glucoseserver.prescription.util.ocr.opencv

import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import java.lang.Math.toDegrees
import kotlin.math.atan2
import kotlin.math.hypot

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

fun createRotatedRect(x1: Double, y1: Double, x2: Double, y2: Double, width: Double): RotatedRect {
    val centerX = (x1 + x2) / 2
    val centerY = (y1 + y2) / 2
    val center = Point(centerX, centerY)

    val height = hypot(x2 - x1, y2 - y1)
    val angle = toDegrees(atan2(y2 - y1, x2 - x1))

    return RotatedRect(center, org.opencv.core.Size(width, height), angle)
}

fun drawRotatedRect(image: Mat, rotatedRect: RotatedRect, color: Scalar = Scalar(0.0, 255.0, 0.0), thickness: Int = 2) {
    val vertices = arrayOfNulls<Point>(4)
    rotatedRect.points(vertices)
    for (i in vertices.indices) {
        Imgproc.line(
            image,
            vertices[i],
            vertices[(i + 1) % vertices.size],
            color,
            thickness
        )
    }
}