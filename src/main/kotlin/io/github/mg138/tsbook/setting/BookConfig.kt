package io.github.mg138.tsbook.setting

import io.github.mg138.tsbook.attribute.ItemRarity
import io.github.mg138.tsbook.attribute.ItemType
import io.github.mg138.tsbook.attribute.stat.StatType
import io.github.mg138.tsbook.setting.config.BookSetting
import io.github.mg138.tsbook.setting.gui.armor.ArmorGUIConfig
import io.github.mg138.tsbook.setting.item.ItemConfig
import io.github.mg138.tsbook.setting.mob.MobConfig
import io.github.mg138.tsbook.setting.util.ConfigBuilder
import io.github.mg138.tsbook.util.translate.TranslatableSetting
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.hover.content.Text
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*

object BookConfig : AbstractConfig() {
    class Language(lang: TranslatableSetting) {
        val prefix: String = lang.get("prefix")
        val format: Format
        val errors: Errors
        val gui: GUI
        val commands: Commands
        val messages: Messages
        val healthIndicator: HealthIndicator
        val attribute: Attribute

        init {
            lang.placeholders["[!prefix]"] = prefix
            println(lang)
            format = Format(lang.getSection("format"))
            errors = Errors(lang.getSection("errors"), format)
            gui = GUI(lang.getSection("gui"))
            commands = Commands(lang.getSection("commands"))
            messages = Messages(lang.getSection("messages"), commands)
            healthIndicator = HealthIndicator(lang.getSection("health_indicator"))
            attribute = Attribute(lang.getSection("attribute"))
        }

        class Format(format: TranslatableSetting) {
            val argumentTypes = ArgumentTypes(format.getSection("argument_types"))
            val page = Page(format.getSection("page"))

            private val range = format.get("range")
            fun range(min: Number, max: Number) = range
                .replace("[!min]", min.toString())
                .replace("[!max]", max.toString())

            class ArgumentTypes(argument: TranslatableSetting) {
                val integer = argument.get("integer")
            }

            class Page(page: TranslatableSetting) {
                private fun number(string: String, command: String, number: Int): TextComponent {
                    return TextComponent(string.replace("[!number]", number.toString())).also {
                        it.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, command)
                        it.hoverEvent = HoverEvent(
                            HoverEvent.Action.SHOW_TEXT,
                            Text(hover.replace("[!page]", number.toString()))
                        )
                    }
                }

                enum class SelectorPos {
                    TOP, BEFORE_HELP, AFTER_HELP, BOTTOM;
                }

                val selectorPos = SelectorPos.valueOf(page.get("selectorPos").toUpperCase())

                private val header = page.get("header")
                fun header(name: String, now: Int, min: Int, max: Int) = header.applyPlaceholder(name, now, min, max)

                private val footer = page.get("footer")
                fun footer(name: String, now: Int, min: Int, max: Int) = footer.applyPlaceholder(name, now, min, max)

                private val notAvailableSymbol = page.get("notAvailable_symbol")
                private val notAvailable = page.get("notAvailable")
                private fun notAvailable() = TextComponent(notAvailable.replace("[!placeholder]", notAvailableSymbol))

                private val prev = page.get("prev")
                private fun prev(command: String, number: Int, min: Int) = when {
                    number > min -> number(prev, command, number - 1)
                    else -> notAvailable()
                }

                private val now = page.get("now")
                private fun now(number: Int) = now.replace("[!number]", number.toString())

                private val next = page.get("next")
                private fun next(command: String, number: Int, max: Int) = when {
                    number < max -> number(next, command, number + 1)
                    else -> notAvailable()
                }

                private val hover = page.get("hover")

                private val selectorMatchers = arrayOf("[!prev]", "[!now]", "[!next]")
                private val selector = page.get("selector")

                private fun match(
                    i: Int,
                    length: Int,
                    command: String,
                    name: String,
                    number: Int,
                    min: Int,
                    max: Int,
                    buffer: StringBuffer,
                    component: BaseComponent
                ): Int {
                    for (matcher in selectorMatchers) {
                        val matcherLength = matcher.length

                        var j = 0
                        while (matcher[j] == selector[i + j]) {
                            j++
                            if (j >= matcherLength) {
                                if (buffer.isNotEmpty()) {
                                    component.addExtra(buffer.toString().applyPlaceholder(name, number, min, max))
                                    buffer.setLength(0)
                                }

                                when (matcher) {
                                    "[!prev]" -> component.addExtra(prev("$command ${number - 1}", number, min))
                                    "[!now]" -> component.addExtra(now(number))
                                    "[!next]" -> component.addExtra(next("$command ${number + 1}", number, max))
                                }
                                return matcherLength
                            }
                            if (i + j >= length) return -1
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

                fun selector(command: String, name: String, number: Int, min: Int, max: Int): BaseComponent {
                    val component = TextComponent()

                    var i = 0
                    val selectorLength = selector.length
                    val buffer = StringBuffer()

                    while (i < selectorLength) {
                        val j = match(i, selectorLength, command, name, number, min, max, buffer, component)
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
                    if (buffer.isNotEmpty()) component.addExtra(
                        buffer.toString().applyPlaceholder(name, number, min, max)
                    )
                    return component
                }

                val help = page.get("help")
            }
        }

        class Errors(errors: TranslatableSetting, val format: Format) {
            val effect = Effect(errors.getSection("effect"))
            val gui = GUI(errors.getSection("gui"))

            val unknownCommand = errors.get("unknown_command")
            val itemNotFound = errors.get("item_not_found")
            val playerOnly = errors.get("player_only")
            val playerNotFound = errors.get("player_not_found")
            val handEmpty = errors.get("hand_empty")

            private val shouldPutElse = errors.get("should_put_else")
            fun shouldPutInteger() = shouldPutElse.replace("[!else]", format.argumentTypes.integer)

            val noSuchOption = errors.get("no_such_option")

            private val notInRange = errors.get("not_in_range")
            fun notInRange(min: Number, max: Number) = notInRange.replace("[!range]", format.range(min, max))

            class Effect(effect: TranslatableSetting) {
                val noActiveEffect = effect.get("no_active_effect")
            }

            class GUI(gui: TranslatableSetting) {
                val badItem = gui.get("bad_item")
            }
        }

        class GUI(gui: TranslatableSetting) {
            val equipment = Equipment(gui.getSection("equipment"))

            class Equipment(lang: TranslatableSetting) {
                val name = lang.get("name")
            }
        }

        class Commands(commands: TranslatableSetting) {
            val feedback = Feedback(commands.getSection("feedback"))

            class Feedback(feedback: TranslatableSetting) {
                val get = feedback.get("get")
                val give = feedback.get("give")
                val unid = feedback.get("unid")
                val effect = feedback.get("effect")
                val debug = feedback.get("debug")
            }
        }

        class Messages(messages: TranslatableSetting, commands: Commands) {
            val help = Help(commands)
            val effect = Effect(messages.getSection("effect"))

            val reload = messages.get("reload")
            val reloaded = messages.get("reloaded")
            val get = messages.get("get")

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

            class Effect(effect: TranslatableSetting) {
                val cleared = effect.get("cleared")
                val applied = effect.get("applied")
            }
        }

        class HealthIndicator(healthIndicator: TranslatableSetting) {
            val title = healthIndicator.get("title")
        }

        class Attribute(attribute: TranslatableSetting) {
            val item = Item(attribute.getSection("item"))
            val stat = Stat(attribute.getSection("stat"))

            class Item(item: TranslatableSetting) {
                val format = item.get("format")
                val rarity: MutableMap<ItemRarity, String> = EnumMap(ItemRarity::class.java)
                val type: MutableMap<ItemType, String> = EnumMap(ItemType::class.java)

                init {
                    ItemRarity.values().forEach { rarity ->
                        item.getUnsafe("rarity.${rarity.name}")?.let {
                            this.rarity[rarity] = it
                        }
                    }
                    ItemType.values().forEach { type ->
                        item.getUnsafe("type.${type.name}")?.let {
                            this.type[type] = it
                        }
                    }
                }
            }

            class Stat(stat: TranslatableSetting) {
                val name: MutableMap<StatType, String> = EnumMap(StatType::class.java)
                val format: MutableMap<StatType, String> = EnumMap(StatType::class.java)
                val indicator: MutableMap<StatType, String> = EnumMap(StatType::class.java)

                init {
                    StatType.values().forEach { type ->
                        stat.getUnsafe("name.${type.name}")?.let {
                            name[type] = it
                        }
                        stat.getUnsafe("format.${type.name}")?.let {
                            format[type] = it
                        }
                        stat.getUnsafe("indicator.${type.name}")?.let {
                            indicator[type] = it
                        }
                    }
                }
            }
        }
    }

    private lateinit var languageSetting: TranslatableSetting
    private lateinit var bookSetting: BookSetting
    lateinit var language: Language

    override fun load(plugin: JavaPlugin, jar: File) {
        val start = System.currentTimeMillis()

        val cb = ConfigBuilder(plugin, jar)

        plugin.logger.info("Loading configuration...")
        bookSetting = BookSetting(cb.create("", "config.yml"))

        plugin.logger.info("Loading language file: ${bookSetting.locale}...")
        languageSetting = TranslatableSetting(cb.loadDirectory("lang", "${bookSetting.locale}.yml"))
        language = Language(languageSetting)

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