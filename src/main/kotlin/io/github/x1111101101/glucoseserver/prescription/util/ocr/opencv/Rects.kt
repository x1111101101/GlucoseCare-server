package io.github.x1111101101.glucoseserver.prescription.util.ocr.opencv

import org.opencv.core.Rect

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

fun Rect.area(): Int {
    return this.width * this.height
}

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