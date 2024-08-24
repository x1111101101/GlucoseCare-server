package io.github.x1111101101.glucoseserver.food.dish.util

import java.util.*

class SimpleNameSearchHelper {

    private val words = HashMap<String, MutableMap<SearchHelperItem, Int>>()
    private val names = HashMap<String, HashSet<SearchHelperItem>>()

    fun add(uuid: UUID, name: String) {
        val item = SearchHelperItem(uuid, name)
        ngram(clearString(name)) { len, s ->
            val map = words.computeIfAbsent(s) { HashMap() }
            val weight = (len * len).let { it * it }
            map[item] = weight
        }
        names.computeIfAbsent(name) { HashSet() }.add(item)
    }

    fun remove(uuid: UUID, name: String) {
        val item = SearchHelperItem(uuid, name)
        ngram(clearString(name)) { len, s ->
            words[s]?.remove(item)
        }
        names[name]?.remove(item)
    }

    fun recommend(query: String): List<SearchHelperItem> {
        val scores = HashMap<SearchHelperItem, Int>()
        names[query]?.forEach {
            scores[it] = 10000
        }
        ngram(clearString(query)) { _, s ->
            val matched = words[s] ?: return@ngram
            matched.forEach { (item, weight) ->
                scores[item] = (scores[item] ?: 0) + weight
            }
        }
        val filtered = if (scores.size > 20) {
            val weightAvg = scores.values.sum() / scores.size.toDouble()
            scores.filterValues { it >= weightAvg * 0.8 }
        } else scores
        val sorted = filtered.toList().sortedByDescending { it.second }
        return sorted.map { it.first }
    }

    private fun clearString(name: String): String {
        return name.replace(" ", "").replace(",", "")
    }

}

data class SearchHelperItem(val uuid: UUID, val name: String)

private fun ngram(s: String, action: (length: Int, String) -> Unit) {
    for (len in 1..s.length) {
        var start = 0
        while (start + len <= s.length) {
            val sub = s.substring(start, start + len)
            start++
            action(len, sub)
        }
    }
}