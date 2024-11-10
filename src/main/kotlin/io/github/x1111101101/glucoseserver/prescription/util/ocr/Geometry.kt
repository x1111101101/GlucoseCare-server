package io.github.x1111101101.glucoseserver.prescription.util.ocr

import kotlin.math.pow
import kotlin.math.sqrt

data class ConvexRect(val leftTop: Dot, val rightTop: Dot, val leftBottom: Dot, val rightBottom: Dot) {

}

data class AABB(val leftTop: Dot, val width: Int, val height: Int) {

}

data class Line(val start: Dot, val end: Dot) {

    fun slope(): Float? {
        val (x1, y1) = start
        val (x2, y2) = end
        return if (x2 != x1) (y2 - y1).toFloat() / (x2 - x1) else null
    }

    fun length(): Float {
        val (x1, y1) = start
        val (x2, y2) = end
        val dx = x2 - x1.toDouble()
        val dy = y2 - y1.toDouble()
        return sqrt(dx.pow(2) + dy.pow(2)).toFloat()
    }
}

data class Dot(val x: Int, val y: Int)