package io.github.mg138.tsbook.util

object StringUtil {
    fun String.match(start: Int, other: String): Boolean {
        val length = this.length

        for (j in other.indices) {
            val i = start + j

            if (i >= length || this[i] != other[j]) {
                return false
            }
        }
        return true
    }

    fun matches(start: Int, string: String, matchers: Array<String>): String? {
        matchers.forEach {
            if (string.match(start, it)) return it
        }
        return null
    }
}