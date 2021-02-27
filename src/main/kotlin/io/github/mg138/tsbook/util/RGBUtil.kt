package io.github.mg138.tsbook.util

import net.md_5.bungee.api.ChatColor
import java.util.regex.Pattern

object RGBUtil {
    class TextColor(var r: Int, var g: Int, var b: Int)

    private val hex = Pattern.compile("#[0-9a-fA-F]{6}")
    private val gradient = Pattern.compile("<#[0-9a-fA-F]{6}>[^<]*</#[0-9a-fA-F]{6}>")

    fun translate(input: String): String {
        var text = input
        text = ChatColor.translateAlternateColorCodes('&', text)
        text = setGradient(text)
        text = setColor(text)
        return text
    }

    //#RRGGBB
    private fun setColor(input: String): String {
        var text = input
        val m = hex.matcher(text)
        while (m.find()) {
            val color = m.group()
            text = text.replace(color, ChatColor.of(color).toString())
        }
        return text
    }

    //<#RRGGBB>Text</#RRGGBB>
    private fun setGradient(input: String): String {
        var text = input
        val m = gradient.matcher(text)
        while (m.find()) {
            val original = m.group()
            val message = original.substring(9, original.length - 10)
            val start = original.substring(2, 8).toInt(16)
            val startR = start shr 16 and 0xFF
            val startG = start shr 8 and 0xFF
            val startB = start and 0xFF
            val end = original.substring(original.length - 7, original.length - 1).toInt(16)
            val endR = end shr 16 and 0xFF
            val endG = end shr 8 and 0xFF
            val endB = end and 0xFF

            text = text.replace(original, asGradient(message, TextColor(startR, startG, startB), TextColor(endR, endG, endB)))
        }
        return text
    }

    private fun asGradient(text: String, start: TextColor, end: TextColor): String {
        val sb = StringBuilder()

        var i = 0
        var actualLength = -1
        while (i < text.length) {
            if (text[i] == ChatColor.COLOR_CHAR) {
                i += 2
                continue
            }
            actualLength++
            i++
        }
        if (actualLength < 1) return text

        var j = 0
        var offset = 0
        var flags = ""
        while (j < text.length) {
            if (text[j] == ChatColor.COLOR_CHAR) {
                val flag = text[j + 1]
                if (flag == 'r') {
                    flags = ""
                } else {
                    flags += (ChatColor.COLOR_CHAR)
                    flags += flag
                }
                j += 2
                offset += 2
                continue
            }

            val r = (start.r + (end.r - start.r) / actualLength * (j - offset))
            val g = (start.g + (end.g - start.g) / actualLength * (j - offset))
            val b = (start.b + (end.b - start.b) / actualLength * (j - offset))
            sb.append(String.format("#%06x", ((r shl 16) + (g shl 8) + b)) + flags + text[j])
            j++
        }
        return sb.toString()
    }
}