package io.github.mg138.tsbook.util

import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent

object ComponentUtil {
    fun replace(
        string: String,
        matchers: Array<String>,
        whenNotMatch: (BaseComponent, String) -> Unit,
        whenMatch: (BaseComponent, String) -> Unit
    ): BaseComponent {
        val component = TextComponent()

        val length = string.length
        val buffer = StringBuffer()

        var i = 0
        while (i < length) {
            val s = StringUtil.match(i, string, matchers)

            if (s == null) {
                buffer.append(string[i])
                i++
            } else {
                whenNotMatch(component, buffer.toString())
                whenMatch(component, s)

                buffer.setLength(0)
                i += s.length
            }
        }

        if (buffer.isNotEmpty()) {
            whenNotMatch(component, buffer.toString())
        }
        return component
    }
}