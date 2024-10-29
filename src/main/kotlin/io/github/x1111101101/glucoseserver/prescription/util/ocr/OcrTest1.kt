package io.github.x1111101101.glucoseserver.prescription.util.ocr

import com.google.cloud.vision.v1.AnnotateImageResponse
import com.google.cloud.vision.v1.EntityAnnotation
import java.lang.Integer.min
import java.util.*
import kotlin.math.abs
import kotlin.math.max

object OcrTest1 {

    class Group {

    }

    var avrLineHeight = 0
    val boxMembers = HashMap<Box, EntityAnnotation>()

    class Box(
        val x: MinMax = MinMax(),
        val y: MinMax = MinMax()
    ) {
        val uuid = UUID.randomUUID()


        fun append(box: Box) {
            x.append(box.x)
            y.append(box.y)
        }

        override fun hashCode(): Int {
            return uuid.hashCode()
        }
    }

    data class MinMax(var min: Int = Int.MAX_VALUE, var max: Int = Int.MIN_VALUE) {
        fun append(value: Int) {
            min = min(min, value)
            max = max(max, value)
        }

        fun append(value: MinMax) {
            append(value.min)
            append(value.max)
        }

        fun isWithin(value: Int) = value in min..max
        fun hasOverlap(other: MinMax): Boolean {
            val r = !(other.max < min || other.min > max)
            println("OVL: ${this} : ${other} = $r")
            return r
        }

        fun gap(other: MinMax) = min(abs(other.max - min), abs(other.min - max))
        fun len() = max - min
    }

    fun checkMerge(p: Box, c: Box): Boolean {
        if(p.y.hasOverlap(c.y)) return true
        return (p.y.run { max - min } < avrLineHeight) && p.y.len() + p.y.gap(c.y) < avrLineHeight
    }

    fun calcAvrLineHeight(singleLines: List<Box>): Int {
        return singleLines.sumOf { it.y.max - it.y.min } / singleLines.size
    }

    fun test(data: AnnotateImageResponse) {
        val textAnnotations = data.textAnnotationsList
        println("FT: ${data.fullTextAnnotation.text}")
        val visit = HashSet<UUID>()
        val merged = ArrayList<Pair<Box, Set<EntityAnnotation>>>()
        val boxes = textAnnotations.map { it.toBox() to hashSetOf(it) }.sortedBy { it.first.y.min }
            .filter { it.first.y.run { max - min } < 100 }
        avrLineHeight = calcAvrLineHeight(boxes.map { it.first })
        var i = 0
        while (i in boxes.indices) {
            val entry = boxes[i]
            val box = entry.first
            val entities = entry.second
            val firstBox = Box(box.x.copy(), box.y.copy() )
            i++
            if (visit.contains(box.uuid)) continue
            merged.add(entry)
            visit.add(box.uuid)
            for (k in i+1..boxes.lastIndex) {
                val other = boxes[k]
                if (visit.contains(other.first.uuid)) continue
                println("check: ${entities.last().description} & ${other.second.first().description}")
                if (!checkMerge(box, other.first)) {
                    break
                }
                box.append(other.first)
                entities.addAll(other.second)
                visit.add(other.first.uuid)
            }
            println("i: $i")

        }
        merged.forEach {
            val wordsSorted = it.second.sortedBy { it.boundingPoly.verticesList.minOf { it.x } }
            println("S: ${wordsSorted.map { it.description }.joinToString("_")}")
        }
        var lines = data.textAnnotationsList[0].description.split('\n').toMutableList()
        lines.forEach {
            println("line: ${it}")
        }
    }

}

fun EntityAnnotation.toBox(): OcrTest1.Box {
    val poly = this.boundingPoly
    return OcrTest1.Box().also {
        poly.verticesList.forEach { v ->
            it.x.append(v.x)
            it.y.append(v.y)
        }
    }
}