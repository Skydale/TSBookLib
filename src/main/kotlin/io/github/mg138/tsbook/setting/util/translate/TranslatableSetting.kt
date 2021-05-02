package io.github.mg138.tsbook.setting.util.translate

import io.github.mg138.tsbook.setting.util.Section
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

open class TranslatableSetting(private val setting: ConfigurationSection, val placeholders: MutableMap<String, String> = HashMap()) {
    private fun <T : Any> getSectionAnnotation(lang: KClass<T>) = lang.findAnnotation<Section>()
        ?: throw IllegalArgumentException("Class doesn't have ${Section::class.simpleName} annotation.")

    fun <T : Any> getSection(lang: KClass<T>) = this.getSection(this.getSectionAnnotation(lang))

    private fun getSection(section: Section) = this.getSection(section.name)

    fun getSection(path: String): TranslatableSetting {
        return try {
            TranslatableSetting(setting.getConfigurationSection(path)!!, placeholders)
        } catch (e: KotlinNullPointerException) {
            throw IllegalArgumentException("${setting.name} doesn't contain $path")
        }
    }

    fun getUnsafe(path: String, player: OfflinePlayer? = null): String? {
        return TranslateUtil.getUnsafe(path, player, setting, placeholders)
    }

    fun get(path: String, player: OfflinePlayer? = null): String {
        return TranslateUtil.get(path, player, setting, placeholders)
    }

    fun getList(path: String, player: OfflinePlayer? = null): List<String> {
        return TranslateUtil.getList(path, player, setting, placeholders)
    }

    override fun toString(): String {
        return setting.getKeys(false).toString()
    }
}