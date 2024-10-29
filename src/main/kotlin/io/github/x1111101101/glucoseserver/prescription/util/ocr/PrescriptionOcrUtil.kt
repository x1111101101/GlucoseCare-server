package io.github.x1111101101.glucoseserver.prescription.util.ocr


private val TOKENS_PER_FORMAT = listOf(
    listOf("처방의약품의명칭","1회", "1일"),
    listOf("")
)

class PrescriptionParseException : Exception() {

}

internal data class ParseMaterial(
    val tokens: List<String>,
    val compressedTokens: List<String>,
    val compressedTokenIndexes: HashMap<String, Int>
)

internal fun parsePrescription(ocrResult: String) {
    val tokens = ocrResult.split("\n")
        .filter { it.isNotEmpty() }
    val compressedTokens = tokens.map { it.replace(" ", "") }
    println("TOKENS: ${compressedTokens.joinToString("@")}")
    val compressedTokenIndexes = HashMap<String, Int>()
    compressedTokens.forEachIndexed { index, token ->
        if (!compressedTokenIndexes.containsKey(token)) {
            compressedTokenIndexes[token] = index
        }
    }
    val material = ParseMaterial(tokens, compressedTokens, compressedTokenIndexes)
    for (format in TOKENS_PER_FORMAT.indices) {
        val li = TOKENS_PER_FORMAT[format]
        val mismatch = li.firstOrNull { !compressedTokenIndexes.containsKey(it) }
        println("mismatch: $mismatch")
        if (mismatch != null) continue
        when (format) {
            0-> {
                parseFormat0(material)
                return
            }
        }
    }
    throw PrescriptionParseException()
}

private fun parseFormat0(material: ParseMaterial) {
    println("format0")
    val columnStart = material.compressedTokenIndexes["처방의약품의명칭"]!!

}