package io.github.mg138.tsbook.util.translate

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import kotlin.reflect.full.findAnnotation

open class TranslatableSetting(private val setting: ConfigurationSection, val placeholders: MutableMap<String, String> = HashMap()) {
    val name = setting.name

    fun getKeys(deep: Boolean = false): Set<String> = setting.getKeys(deep)

    inline fun <reified T : Any> getSectionAnnotation() = T::class.findAnnotation<Section>()
        ?: throw IllegalArgumentException("Class doesn't have ${Section::class.simpleName} annotation.")

    fun getSection(path: String): TranslatableSetting {
        return setting.getConfigurationSection(path)?.let {
            TranslatableSetting(it, placeholders)
        } ?: throw IllegalArgumentException(TranslateUtil.defaultErrorMsg(setting, path))
    }

    fun getSection(section: Section) = this.getSection(section.name)

    inline fun <reified T : Any> getSection() = this.getSection(this.getSectionAnnotation<T>())

    operator fun get(path: String, player: OfflinePlayer? = null) =
        TranslateUtil.get(setting, path, player, placeholders)

    fun get(path: String, player: OfflinePlayer? = null, action: (String) -> Unit) =
        TranslateUtil.get(setting, path, player, placeholders, action)

    fun getSafe(path: String, player: OfflinePlayer? = null) =
        TranslateUtil.getSafe(setting, path, player, placeholders)

    fun getSafe(path: String, player: OfflinePlayer? = null, action: (String) -> Unit) =
        TranslateUtil.getSafe(setting, path, player, placeholders, action)

    fun getList(path: String, player: OfflinePlayer? = null) =
        TranslateUtil.getList(setting, path, player, placeholders)

    override fun toString() = setting.toString()
}