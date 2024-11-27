package io.github.x1111101101.glucoseserver.prescription.util.ocr.opencv

import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import kotlin.math.max
import kotlin.math.min

fun calculateRectIntersectionArea(rect1: Rect, rect2: Rect): Int {
    val xLeft = max(rect1.x, rect2.x)
    val yTop = max(rect1.y, rect2.y)
    val xRight = min(rect1.x + rect1.width, rect2.x + rect2.width)
    val yBottom = min(rect1.y + rect1.height, rect2.y + rect2.height)
    if (xRight <= xLeft || yBottom <= yTop) {
        return 0
    }
    return (xRight - xLeft) * (yBottom - yTop)
}

/*
fun filterLargerBoundingBoxes(boundingBoxes: List<Rect>): List<Rect> {
    val filteredBoxes = mutableListOf<Rect>()

    for (i in boundingBoxes.indices) {
        val boxA = boundingBoxes[i]
        var shouldAdd = true

        for (j in boundingBoxes.indices) {
            if (i != j) {
                val boxB = boundingBoxes[j]

                // 두 bounding box가 겹칠 경우
                if (boxA.intersect(boxB)) {
                    // 면적이 더 큰 bounding box만 남기기
                    if (boxA.area() <= boxB.area()) {
                        shouldAdd = false
                        break
                    }
                }
            }
        }

        // 더 큰 bounding box인 경우에만 리스트에 추가
        if (shouldAdd) {
            filteredBoxes.add(boxA)
        }
    }

    return filteredBoxes
}

 */

fun Rect.area(): Int {
    return this.width * this.height
}
/*
fun removeOverlappingBoundingBoxes(boundingBoxes: List<Rect>): List<Rect> {
    val nonOverlappingBoxes = mutableListOf<Rect>()

    for (i in boundingBoxes.indices) {
        var isOverlapping = false
        val boxA = boundingBoxes[i]

        for (j in boundingBoxes.indices) {
            if (i != j) {
                val boxB = boundingBoxes[j]
                if (boxA.intersect(boxB)) {
                    isOverlapping = true
                    break
                }
            }
        }
        if (!isOverlapping) {
            nonOverlappingBoxes.add(boxA)
        }
    }

    return nonOverlappingBoxes
}


fun Rect.intersect(other: Rect): Boolean {
    val xOverlap = (this.x < other.x + other.width) && (this.x + this.width > other.x)
    val yOverlap = (this.y < other.y + other.height) && (this.y + this.height > other.y)
    return xOverlap && yOverlap
}

 */

// TODO release mem
fun calculateIntersectionArea(rotatedRect: RotatedRect, rect: Rect): Double {
    // RotatedRect를 Point 배열로 변환
    val rotatedRectPoints = Array(4) { Point() }
    rotatedRect.points(rotatedRectPoints)

    // Rect를 Point 배열로 변환
    val rectPoints = arrayOf(
        Point(rect.x.toDouble(), rect.y.toDouble()),
        Point(rect.x.toDouble() + rect.width, rect.y.toDouble()),
        Point(rect.x.toDouble() + rect.width, rect.y.toDouble() + rect.height),
        Point(rect.x.toDouble(), rect.y.toDouble() + rect.height)
    )

    // 교집합 계산
    val intersection = mutableListOf<Point>()
    val result = Imgproc.intersectConvexConvex(
        MatOfPoint2f(*rotatedRectPoints),
        MatOfPoint2f(*rectPoints),
        MatOfPoint2f(*intersection.toTypedArray())
    )

    // 교집합이 없으면 면적은 0
    if (result <= 0) return 0.0

    // 교집합 면적 계산
    return Imgproc.contourArea(MatOfPoint(*intersection.toTypedArray()))
}