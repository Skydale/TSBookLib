package io.github.mg138.tsbook.util

import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent

object ComponentUtil {
    fun replace(
        string: String,
        matchers: Array<String>,
        fallback: (BaseComponent, String) -> Unit,
        matched: (BaseComponent, String) -> Unit
    ): BaseComponent {
        val component = TextComponent()

        val length = string.length
        val buffer = StringBuffer()

        var i = 0
        while (i < length) {
            val s = StringUtil.matches(i, string, matchers)

            if (s == null) {
                buffer.append(string[i])
                i++
            } else {
                fallback(component, buffer.toString())
                matched(component, s)

                buffer.setLength(0)
                i += s.length
            }
        }

        if (buffer.isNotEmpty()) {
            fallback(component, buffer.toString())
        }
        return component
    }
}