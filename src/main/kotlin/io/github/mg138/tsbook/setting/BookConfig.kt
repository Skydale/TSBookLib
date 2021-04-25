package io.github.mg138.tsbook.setting

import io.github.mg138.tsbook.item.attribute.ItemRarity
import io.github.mg138.tsbook.item.attribute.ItemType
import io.github.mg138.tsbook.item.attribute.stat.StatType
import io.github.mg138.tsbook.setting.config.BookSetting
import io.github.mg138.tsbook.setting.util.Section
import io.github.mg138.tsbook.setting.gui.armor.ArmorGUIConfig
import io.github.mg138.tsbook.setting.item.ItemConfig
import io.github.mg138.tsbook.setting.mob.MobConfig
import io.github.mg138.tsbook.setting.util.ConfigBuilder
import io.github.mg138.tsbook.setting.util.translate.TranslatableSetting
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
            format = Format(lang.getSection(Format::class))
            errors = Errors(lang.getSection(Errors::class), format)
            gui = GUI(lang.getSection(GUI::class))
            commands = Commands(lang.getSection(Commands::class))
            messages = Messages(lang.getSection(Messages::class), commands)
            healthIndicator = HealthIndicator(lang.getSection(HealthIndicator::class))
            attribute = Attribute(lang.getSection(Attribute::class))
        }

        @Section("format")
        class Format(format: TranslatableSetting) {
            val argumentTypes = ArgumentTypes(format.getSection(ArgumentTypes::class))
            val page = Page(format.getSection(Page::class))

            private val range = format.get("range")
            fun range(min: Number, max: Number) = range
                .replace("[!min]", min.toString())
                .replace("[!max]", max.toString())

            @Section("argument_types")
            class ArgumentTypes(argument: TranslatableSetting) {
                val integer = argument.get("integer")
            }

            @Section("page")
            class Page(page: TranslatableSetting) {
                val help = Help(page.getSection("help"), this)

                private fun number(string: String, command: String, number: Int): TextComponent {
                    return TextComponent(string.replace("[!number]", number.toString())).also {
                        it.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, command)
                        it.hoverEvent = HoverEvent(
                            HoverEvent.Action.SHOW_TEXT,
                            Text(hover.replace("[!page]", number.toString()))
                        )
                    }
                }

                private val notAvailable = TextComponent(page.get("notAvailable"))

                private val prev = page.get("prev")
                private fun prev(command: String, now: Int, min: Int) = when {
                    now > min -> number(prev, command, now - 1)
                    else -> notAvailable
                }

                private val now = page.get("now")
                private fun now(number: Int) = now.replace("[!number]", number.toString())

                private val next = page.get("next")
                private fun next(command: String, now: Int, max: Int) = when {
                    now < max -> number(next, command, now + 1)
                    else -> notAvailable
                }

                private val hover = page.get("hover")

                enum class SelectorPos {
                    TOP, BEFORE_HELP, AFTER_HELP, BOTTOM;
                }

                class PageSelector(val name: String, val now: Int, val min: Int, val max: Int)

                class Help(help: TranslatableSetting, private val page: Page) {
                    val selectorPos = SelectorPos.valueOf(help.get("selectorPos").toUpperCase())

                    private val header = help.get("header")
                    fun header(now: Int, min: Int, max: Int) = page.applyPlaceholder(header, PageSelector(name, now, min, max))

                    private val footer = help.get("footer")
                    fun footer(now: Int, min: Int, max: Int) = page.applyPlaceholder(footer, PageSelector(name, now, min, max))

                    private val selector = help.get("selector")
                    private val matchers = arrayOf("[!prev]", "[!now]", "[!next]")

                    private val name = help.get("name")

                    fun selector(command: String, now: Int, min: Int, max: Int) =
                        page.selector(command, selector, matchers, PageSelector(name, now, min, max))
                }

                private fun selector(command: String, template: String, matchers: Array<String>, selector: PageSelector): BaseComponent {
                    val component = TextComponent()

                    var i = 0
                    val length = template.length
                    val buffer = StringBuffer()

                    while (i < length) {
                        val j = match(template, matchers, i, command, selector, buffer, component)

                        if (j > 0) {
                            i += j
                        } else if (j < 0) {
                            buffer.append(template.substring(i, length))
                            break
                        } else {
                            buffer.append(template[i])
                            i++
                        }
                    }
                    if (buffer.isNotEmpty()) component.addExtra(
                        applyPlaceholder(buffer.toString(), selector)
                    )
                    return component
                }

                private fun match(
                    matching: String,
                    matchers: Array<String>,
                    i: Int,
                    command: String,
                    selector: PageSelector,
                    buffer: StringBuffer,
                    component: BaseComponent
                ): Int {
                    val now = selector.now
                    val length = matching.length

                    for (matcher in matchers) {
                        val matcherLength = matcher.length

                        var j = 0
                        while (matcher[j] == matching[i + j]) {
                            j++
                            if (j >= matcherLength) {
                                if (buffer.isNotEmpty()) {
                                    component.addExtra(applyPlaceholder(buffer.toString(), selector))
                                    buffer.setLength(0)
                                }

                                when (matcher) {
                                    "[!prev]" -> component.addExtra(prev("$command ${now - 1}", now, selector.min))
                                    "[!now]" -> component.addExtra(now(now))
                                    "[!next]" -> component.addExtra(next("$command ${now + 1}", now, selector.max))
                                }
                                return matcherLength
                            }
                            if (i + j >= length) return -1
                        }
                    }
                    return 0
                }

                fun applyPlaceholder(string: String, selector: PageSelector): String {
                    return string
                        .replace("[!name]", selector.name)
                        .replace("[!now]", selector.now.toString())
                        .replace("[!min", selector.min.toString())
                        .replace("[!max]", selector.max.toString())
                }
            }
        }

        @Section("errors")
        class Errors(errors: TranslatableSetting, val format: Format) {
            val effect = Effect(errors.getSection(Effect::class))
            val gui = GUI(errors.getSection(GUI::class))

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

            @Section("effect")
            class Effect(effect: TranslatableSetting) {
                val noActiveEffect = effect.get("no_active_effect")
            }

            @Section("gui")
            class GUI(gui: TranslatableSetting) {
                val badItem = gui.get("bad_item")
            }
        }

        @Section("gui")
        class GUI(gui: TranslatableSetting) {
            val equipment = Equipment(gui.getSection(Equipment::class))

            @Section("equipment")
            class Equipment(lang: TranslatableSetting) {
                val name = lang.get("name")
            }
        }

        @Section("commands")
        class Commands(commands: TranslatableSetting) {
            val feedback = Feedback(commands.getSection(Feedback::class))

            @Section("feedback")
            class Feedback(feedback: TranslatableSetting) {
                val get = feedback.get("get")
                val give = feedback.get("give")
                val unid = feedback.get("unid")
                val effect = feedback.get("effect")
                val debug = feedback.get("debug")
            }
        }

        @Section("messages")
        class Messages(messages: TranslatableSetting, commands: Commands) {
            val help = Help(commands)
            val effect = Effect(messages.getSection(Effect::class))

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

            @Section("effect")
            class Effect(effect: TranslatableSetting) {
                val cleared = effect.get("cleared")
                val applied = effect.get("applied")
            }
        }

        @Section("health_indicator")
        class HealthIndicator(healthIndicator: TranslatableSetting) {
            val title = healthIndicator.get("title")
        }

        @Section("attribute")
        class Attribute(attribute: TranslatableSetting) {
            val item = Item(attribute.getSection(Item::class))
            val stat = Stat(attribute.getSection(Stat::class))

            @Section("item")
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

            @Section("stat")
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

        //plugin.logger.info("Loading item settings...")
        //ItemConfig.load(cb.loadToMap("Items", "ID"), cb.loadToMap("Unidentified", "ID"))

        //plugin.logger.info("Loading MythicMobs settings...")
        //MobConfig.load(cb.loadToSectionMap("MythicMobs"))

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