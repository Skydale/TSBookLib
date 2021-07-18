package io.github.mg138.tsbook.config

import io.github.mg138.tsbook.item.attribute.ItemRarity
import io.github.mg138.tsbook.item.attribute.ItemType
import io.github.mg138.tsbook.stat.type.DamageType
import io.github.mg138.tsbook.stat.type.StatType
import io.github.mg138.tsbook.config.setting.BookSetting
import io.github.mg138.tsbook.util.translate.Section
import io.github.mg138.tsbook.config.gui.armor.ArmorGUIConfig
import io.github.mg138.tsbook.config.mob.MobConfig
import io.github.mg138.tsbook.config.util.ConfigBuilder
import io.github.mg138.tsbook.util.translate.TranslatableSetting
import io.github.mg138.tsbook.util.ComponentUtil
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.hover.content.Text
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*
import kotlin.collections.HashMap

object BookConfig {
    /*
    object Shared {
        @Section("rarity")
        class Rarity(rarity: TranslatableSetting) {
            val names: MutableMap<ItemRarity, String> = EnumMap(ItemRarity::class.java)

            init {
                ItemRarity.values().forEach { itemRarity ->
                    rarity[itemRarity.name]?.let {
                        this.names[itemRarity] = it
                    }
                }
            }
        }

        @Section("type")
        class Type(type: TranslatableSetting, itemTypes: Iterable<ItemType>) {
            val names: MutableMap<String, String> = HashMap()

            init {
                itemTypes.forEach { itemType ->
                    val id = itemType.getId()

                    type.get(id) {
                        this.names[id] = it
                    }
                }
            }
        }

        @Section("stat")
        class Stat(stat: TranslatableSetting, statTypes: Iterable<StatType>) {
            val name: MutableMap<StatType, String> = HashMap()
            val format: MutableMap<StatType, String> = HashMap()
            val indicator: MutableMap<DamageType, String> = HashMap()

            init {
                statTypes.forEach { statType ->
                    val id = statType.getIdentifier()

                    stat.getSafe("name.$id") {
                        name[statType] = it
                    }

                    stat.getSafe("format.$id") {
                        format[statType] = it
                    }
                }

                statTypes
                    .filterIsInstance<DamageType>()
                    .forEach { damageType ->
                        val id = damageType.getIdentifier()

                        stat.getSafe("indicator.$id") {
                            indicator[damageType] = it
                        }
                    }
            }
        }
    }

    class Language(lang: TranslatableSetting) {
        val prefix: String = lang.getSafe("prefix")
        val format: Format
        val errors: Errors
        val gui: GUI
        val commands: Commands
        val messages: Messages
        val healthIndicator: HealthIndicator
        val attribute: Attribute

        init {
            lang.placeholders["[!prefix]"] = prefix
            format = Format(lang.getSection<Format>())
            errors = Errors(lang.getSection<Errors>(), format)
            gui = GUI(lang.getSection<GUI>())
            commands = Commands(lang.getSection<Commands>())
            messages = Messages(lang.getSection<Messages>(), commands)
            healthIndicator = HealthIndicator(lang.getSection<HealthIndicator>())
            attribute = Attribute(lang.getSection<Attribute>())
        }

        @Section("format")
        class Format(format: TranslatableSetting) {
            val argumentTypes = ArgumentTypes(format.getSection<ArgumentTypes>())
            val page = Page(format.getSection<Page>())

            private val range = format.getSafe("range")
            fun range(min: Number, max: Number) = range
                .replace("[!min]", min.toString())
                .replace("[!max]", max.toString())

            @Section("argument_types")
            class ArgumentTypes(argument: TranslatableSetting) {
                val integer = argument.getSafe("integer")
            }

            @Section("page")
            class Page(page: TranslatableSetting) {
                val help = Help(page.getSection<Help>(), this)

                private fun number(string: String, command: String, number: Int): TextComponent {
                    return TextComponent(string.replace("[!number]", number.toString())).also {
                        it.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, command)
                        it.hoverEvent = HoverEvent(
                            HoverEvent.Action.SHOW_TEXT,
                            Text(hover.replace("[!page]", number.toString()))
                        )
                    }
                }

                private val notAvailable = TextComponent(page.getSafe("notAvailable"))

                private val prev = page.getSafe("prev")
                private fun prev(command: String, now: Int, min: Int) = when {
                    now > min -> number(prev, command, now - 1)
                    else -> notAvailable
                }

                private val now = page.getSafe("now")
                private fun now(number: Int) = now.replace("[!number]", number.toString())

                private val next = page.getSafe("next")
                private fun next(command: String, now: Int, max: Int) = when {
                    now < max -> number(next, command, now + 1)
                    else -> notAvailable
                }

                private val hover = page.getSafe("hover")

                enum class SelectorPos {
                    TOP, BEFORE_HELP, AFTER_HELP, BOTTOM;
                }

                class PageSelector(val name: String, val now: Int, val min: Int, val max: Int)

                @Section("help")
                class Help(help: TranslatableSetting, private val page: Page) {
                    val selectorPos = SelectorPos.valueOf(help.getSafe("selectorPos").toUpperCase())

                    private val header = help.getSafe("header")
                    fun header(now: Int, min: Int, max: Int) =
                        page.applyPlaceholder(header, PageSelector(name, now, min, max))

                    private val footer = help.getSafe("footer")
                    fun footer(now: Int, min: Int, max: Int) =
                        page.applyPlaceholder(footer, PageSelector(name, now, min, max))

                    private val selector = help.getSafe("selector")
                    private val matchers = arrayOf("[!prev]", "[!now]", "[!next]")

                    private val name = help.getSafe("name")

                    fun selector(command: String, now: Int, min: Int, max: Int) =
                        page.selector(command, selector, matchers, PageSelector(name, now, min, max))
                }

                private fun selector(
                    command: String,
                    string: String,
                    matchers: Array<String>,
                    selector: PageSelector
                ): BaseComponent {
                    val now = selector.now
                    val min = selector.min
                    val max = selector.max

                    return ComponentUtil.replace(
                        string,
                        matchers,
                        fallback = { component, s ->
                            component.addExtra(applyPlaceholder(s, selector))
                        }
                    ) { component, s ->
                        when (s) {
                            "[!prev]" -> component.addExtra(prev("$command ${now - 1}", now, min))
                            "[!now]" -> component.addExtra(now(now))
                            "[!next]" -> component.addExtra(next("$command ${now + 1}", now, max))
                        }
                    }
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
            val effect = Effect(errors.getSection<Effect>())
            val gui = GUI(errors.getSection<GUI>())

            val unknownCommand = errors.getSafe("unknown_command")
            val itemNotFound = errors.getSafe("item_not_found")
            val playerOnly = errors.getSafe("player_only")
            val playerNotFound = errors.getSafe("player_not_found")
            val handEmpty = errors.getSafe("hand_empty")

            private val shouldPutElse = errors.getSafe("should_put_else")
            fun shouldPutInteger() = shouldPutElse.replace("[!else]", format.argumentTypes.integer)

            val noSuchOption = errors.getSafe("no_such_option")

            private val notInRange = errors.getSafe("not_in_range")
            fun notInRange(min: Number, max: Number) = notInRange.replace("[!range]", format.range(min, max))

            @Section("effect")
            class Effect(effect: TranslatableSetting) {
                val noActiveEffect = effect.getSafe("no_active_effect")
            }

            @Section("gui")
            class GUI(gui: TranslatableSetting) {
                val badItem = gui.getSafe("bad_item")
            }
        }

        @Section("gui")
        class GUI(gui: TranslatableSetting) {
            val equipment = Equipment(gui.getSection<Equipment>())

            @Section("equipment")
            class Equipment(lang: TranslatableSetting) {
                val name = lang.getSafe("name")
            }
        }

        @Section("commands")
        class Commands(commands: TranslatableSetting) {
            val feedback = Feedback(commands.getSection<Feedback>())

            @Section("feedback")
            class Feedback(feedback: TranslatableSetting) {
                val get = feedback.getSafe("get")
                val give = feedback.getSafe("give")
                val unid = feedback.getSafe("unid")
                val effect = feedback.getSafe("effect")
                val debug = feedback.getSafe("debug")
            }
        }

        @Section("messages")
        class Messages(messages: TranslatableSetting, commands: Commands) {
            val help = Help(commands)
            val effect = Effect(messages.getSection<Effect>())

            val reload = messages.getSafe("reload")
            val reloaded = messages.getSafe("reloaded")
            private val get = messages.getSafe("get")
            fun get(playerName: String, itemName: String) = get
                .replace("[!player]", playerName)
                .replace("[!item]", itemName)

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
                val cleared = effect.getSafe("cleared")
                val applied = effect.getSafe("applied")
            }
        }

        @Section("health_indicator")
        class HealthIndicator(healthIndicator: TranslatableSetting) {
            val title = healthIndicator.getSafe("title")
        }

        @Section("attribute")
        class Attribute(attribute: TranslatableSetting) {
            val item = Item(attribute.getSection<Item>())
            val stat = Shared.Stat(attribute.getSection<Shared.Stat>(), StatType.Preset.types)

            @Section("item")
            class Item(item: TranslatableSetting) {
                val format = item.getSafe("format")
                val rarity = Shared.Rarity(item.getSection<Shared.Rarity>())
                val type = Shared.Type(item.getSection<Shared.Type>(), ItemType.Preset.types)
            }
        }
    }

     */

    private lateinit var languageSetting: TranslatableSetting
    private lateinit var bookSetting: BookSetting
    //lateinit var language: Language

    fun load(plugin: JavaPlugin, jar: File) {
        val start = System.currentTimeMillis()

        val cb = ConfigBuilder(plugin, jar)

        plugin.logger.info("Loading configuration...")
        bookSetting = BookSetting(cb.create("", "config.yml"))

        plugin.logger.info("Loading language file: ${bookSetting.locale}...")
        languageSetting = TranslatableSetting(cb.loadDirectory("lang", "${bookSetting.locale}.yml"))
        //language = Language(languageSetting)

        //plugin.logger.info("Loading item settings...")
        //ItemConfig.load(cb.loadToMap("Items", "ID"), cb.loadToMap("Unidentified", "ID"))

        //plugin.logger.info("Loading MythicMobs settings...")
        //MobConfig.load(cb.loadToSectionMap("MythicMobs"))

        plugin.logger.info("Loading GUI settings...")
        ArmorGUIConfig.load(cb.create("GUI/", "Equipment.yml"))

        plugin.logger.info("Took me... [${System.currentTimeMillis() - start}ms] to load!")
    }

    fun unload() {
        //ItemConfig.unload()
        MobConfig.unload()
        ArmorGUIConfig.unload()
    }
}