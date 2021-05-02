package io.github.mg138.tsbook.util

object StringUtil {
    fun match(start: Int, string: String, matchers: Array<String>): String? {
        val length = string.length

        for (matcher in matchers) {
            val end = matcher.lastIndex

            for (i in matcher.indices) {
                if (start + i >= length) break
                if (matcher[i] != string[start + i]) break
                if (i >= end) return matcher
            }
        }
        return null
    }
}