package io.github.mg138.tsbook.setting

import io.github.mg138.tsbook.setting.config.BookSetting
import io.github.mg138.tsbook.setting.gui.armor.ArmorGUIConfig
import io.github.mg138.tsbook.setting.item.ItemConfig
import io.github.mg138.tsbook.setting.mob.MobConfig
import io.github.mg138.tsbook.setting.attribute.AttributeDisplay
import io.github.mg138.tsbook.setting.util.ConfigBuilder
import io.github.mg138.tsbook.util.translate.TranslatableFile
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.hover.content.Text
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

object BookConfig : AbstractConfig() {
    class Language(lang: TranslatableFile) {
        val prefix: String = lang.get("prefix")
        val format: Format
        val errors: Errors
        val gui: GUI
        val commands: Commands
        val messages: Messages
        val healthIndicator: HealthIndicator

        init {
            lang.placeholders["[!prefix]"] = prefix
            format = Format(lang)
            errors = Errors(lang, format)
            gui = GUI(lang)
            commands = Commands(lang)
            messages = Messages(lang, commands)
            healthIndicator = HealthIndicator(lang)
        }

        class Format(lang: TranslatableFile) {
            val argumentTypes = ArgumentTypes(lang)
            private val range = lang.get("format.range")
            fun range(min: Number, max: Number) = range
                .replace("[!min]", min.toString())
                .replace("[!max]", max.toString())

            val page = Page(lang)

            class ArgumentTypes(lang: TranslatableFile) {
                val integer = lang.get("format.argument_types.integer")
            }

            class Page(lang: TranslatableFile) {
                private fun number(string: String, command: String?, number: Int): TextComponent {
                    return TextComponent(string.replace("[!number]", number.toString())).also {
                        if (command != null) {
                            it.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, command)
                        }
                        it.hoverEvent = HoverEvent(
                            HoverEvent.Action.SHOW_TEXT,
                            Text(hover.replace("[!page]", number.toString()))
                        )
                    }
                }

                enum class SelectorPos {
                    TOP,
                    BEFORE_HELP,
                    AFTER_HELP,
                    BOTTOM
                }
                val selectorPos = SelectorPos.valueOf(lang.get("format.page.selectorPos").toUpperCase())
                private val header = lang.get("format.page.header")
                fun header(name: String, now: Int, min: Int, max: Int) = header.applyPlaceholder(name, now, min, max)

                private val footer = lang.get("format.page.footer")
                fun footer(name: String, now: Int, min: Int, max: Int) = footer.applyPlaceholder(name, now, min, max)

                private val notAvailableSymbol = lang.get("format.page.notAvailable_symbol")
                private val notAvailable = lang.get("format.page.notAvailable")
                private fun notAvailable(): TextComponent {
                    return TextComponent(
                        notAvailable.replace("[!placeholder]", notAvailableSymbol)
                    )
                }

                private val prev = lang.get("format.page.prev")
                private fun prev(command: String?, number: Int, min: Int): TextComponent {
                    return if (number > min) {
                        number(prev, command, number - 1)
                    } else {
                        notAvailable()
                    }
                }

                private val now = lang.get("format.page.now")
                private fun now(number: Int) = now.replace("[!number]", number.toString())

                private val next = lang.get("format.page.next")
                private fun next(command: String?, number: Int, max: Int): TextComponent {
                    return if (number < max) {
                        number(next, command, number + 1)
                    } else {
                        notAvailable()
                    }
                }

                private val hover = lang.get("format.page.hover")

                private val selectorMatchers = arrayOf(
                    "[!prev]",
                    "[!now]",
                    "[!next]"
                )
                private val selector = lang.get("format.page.selector")

                private fun match(i: Int, selectorLength: Int, baseCommand: String, name: String, number: Int, min: Int, max: Int, buffer: StringBuffer, component: BaseComponent): Int {
                    for (matcher in selectorMatchers) {
                        val matcherLength = matcher.length

                        var j = 0
                        while (matcher[j] == selector[i + j]) {
                            j++
                            if (j >= matcherLength) {
                                if (buffer.isNotEmpty()) {
                                    component.addExtra(buffer.toString().applyPlaceholder(name,  number, min, max))
                                    buffer.setLength(0)
                                }

                                when (matcher) {
                                    "[!prev]" -> component.addExtra(
                                        prev("$baseCommand ${number - 1}", number, min)
                                    )
                                    "[!now]" -> component.addExtra(
                                        now(number)
                                    )
                                    "[!next]" -> component.addExtra(
                                        next("$baseCommand ${number + 1}", number, max)
                                    )
                                }
                                return matcherLength
                            }
                            if (i + j >= selectorLength) return -1
                        }
                    }
                    return 0
                }
                private fun String.applyPlaceholder(name: String, now: Int, min: Int, max: Int): String {
                    return this
                        .replace("[!name]", name)
                        .replace("[!now]", now.toString())
                        .replace("[!min", min.toString())
                        .replace("[!max]", max.toString())
                }

                fun selector(baseCommand: String, name: String, number: Int, min: Int, max: Int): BaseComponent {
                    val component = TextComponent()

                    var i = 0
                    val selectorLength = selector.length
                    val buffer = StringBuffer()

                    while (i < selectorLength) {
                        val j = match(i, selectorLength, baseCommand, name, number, min, max, buffer, component)
                        if (j > 0) {
                            i += j
                        } else if (j < 0) {
                            buffer.append(selector.substring(i, selectorLength))
                            break
                        } else {
                            buffer.append(selector[i])
                            i++
                        }
                    }
                    if (buffer.isNotEmpty()) component.addExtra(buffer.toString().applyPlaceholder(name, number, min, max))
                    return component
                }
                val help = lang.get("format.page.help")
            }
        }

        class Errors(lang: TranslatableFile, val format: Format) {
            val unknownCommand = lang.get("errors.unknown_command")
            val itemNotFound = lang.get("errors.item_not_found")
            val playerOnly = lang.get("errors.player_only")
            val playerNotFound = lang.get("errors.player_not_found")
            val handEmpty = lang.get("errors.hand_empty")
            val effect = Effect(lang)

            private val shouldPutElse = lang.get("errors.should_put_else")
            fun shouldPutInteger() = shouldPutElse.replace("[!else]", format.argumentTypes.integer)

            val noSuchOption = lang.get("errors.no_such_option")
            val gui = GUI(lang)

            private val notInRange = lang.get("errors.not_in_range")
            fun notInRange(min: Number, max: Number) = notInRange.replace("[!range]", format.range(min, max))

            class Effect(lang: TranslatableFile) {
                val noActiveEffect = lang.get("errors.effect.no_active_effect")
            }

            class GUI(lang: TranslatableFile) {
                val badItem = lang.get("errors.gui.bad_item")
            }
        }

        class GUI(lang: TranslatableFile) {
            val equipment = Equipment(lang)

            class Equipment(lang: TranslatableFile) {
                val name = lang.get("gui.equipment.name")
            }
        }

        class Commands(lang: TranslatableFile) {
            val feedback = Feedback(lang)

            class Feedback(lang: TranslatableFile) {
                val get = lang.get("commands.feedback.get")
                val give = lang.get("commands.feedback.give")
                val unid = lang.get("commands.feedback.unid")
                val effect = lang.get("commands.feedback.effect")
                val debug = lang.get("commands.feedback.debug")
            }
        }

        class Messages(lang: TranslatableFile, commands: Commands) {
            val help = Help(commands)

            val reload = lang.get("messages.reload")
            val reloaded = lang.get("messages.reloaded")
            val get = lang.get("messages.get")
            val effect = Effect(lang)

            class Help(commands: Commands) {
                val pages = listOf(
                    arrayOf(
                        commands.feedback.get,
                        commands.feedback.give,
                        commands.feedback.unid
                    ),
                    arrayOf(
                        commands.feedback.effect,
                        commands.feedback.debug
                    )
                )
            }

            class Effect(lang: TranslatableFile) {
                val cleared = lang.get("messages.effect.cleared")
                val applied = lang.get("messages.effect.applied")
            }
        }

        class HealthIndicator(lang: TranslatableFile) {
            val title = lang.get("health_indicator.title")
        }
    }

    private lateinit var languageFile: TranslatableFile
    private lateinit var bookSetting: BookSetting
    lateinit var language: Language

    override fun load(plugin: JavaPlugin, jar: File) {
        val start = System.currentTimeMillis()

        val cb = ConfigBuilder(plugin, jar)

        plugin.logger.info("Loading configuration...")
        bookSetting = BookSetting(cb.create("", "config.yml"))

        plugin.logger.info("Loading language file: ${bookSetting.locale}...")
        languageFile = TranslatableFile(cb.loadDirectory("lang", "${bookSetting.locale}.yml"))
        language = Language(languageFile)

        plugin.logger.info("Caching stat format/name...")
        AttributeDisplay.load(languageFile)

        plugin.logger.info("Loading item settings...")
        ItemConfig.load(cb.loadToMap("Items", "ID"), cb.loadToMap("Unidentified", "ID"))

        plugin.logger.info("Loading MythicMobs settings...")
        MobConfig.load(cb.loadToSectionMap("MythicMobs"))

        plugin.logger.info("Loading GUI settings...")
        ArmorGUIConfig.load(cb.create("GUI/", "Equipment.yml"))

        plugin.logger.info("Took me... [${System.currentTimeMillis() - start}ms] to load!")
    }

    override fun unload() {
        ItemConfig.unload()
        MobConfig.unload()
        ArmorGUIConfig.unload()
    }
}