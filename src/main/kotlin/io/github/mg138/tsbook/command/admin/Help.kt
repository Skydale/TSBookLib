package io.github.mg138.tsbook.command.admin

import io.github.mg138.tsbook.command.util.error.CommandError
import io.github.mg138.tsbook.setting.BookConfig.Language.Format.Page.SelectorPos.*
import io.github.mg138.tsbook.setting.BookConfig.language
import org.bukkit.command.CommandSender

object Help {
    private fun selector(sender: CommandSender, page: Int, max: Int) {
        sender.spigot().sendMessage(
            language.format.page.selector("/tsbooklib:tsbook help ", language.format.page.help, page, 0, max)
        )
    }

    fun call(sender: CommandSender, page: String): Boolean {
        return try {
            call(sender, page.toInt())
        } catch (e: NumberFormatException) {
            CommandError.shouldPutInteger(sender)
            false
        }
    }

    fun call(sender: CommandSender, page: Int = 0): Boolean {
        val max = language.messages.help.pages.size - 1
        val pos = language.format.page.selectorPos
        return if (page in 0..max) {
            if (pos == TOP) selector(sender, page, max)
            sender.sendMessage(language.format.page.header(language.format.page.help, page, 0, max))
            if (pos == BEFORE_HELP) selector(sender, page, max)
            sender.sendMessage(language.messages.help.pages[page])
            if (pos == AFTER_HELP) selector(sender, page, max)
            sender.sendMessage(language.format.page.footer(language.format.page.help, page, 0, max))
            if (pos == BOTTOM) selector(sender, page, max)
            true
        } else {
            sender.sendMessage(language.errors.notInRange(0, max))
            false
        }
    }
}