package io.github.mg138.tsbook.setting.stat

import io.github.mg138.tsbook.stat.StatType
import io.github.mg138.tsbook.util.LanguageFile
import java.util.*

object StatDisplay {
    val name: MutableMap<StatType, String> = EnumMap(StatType::class.java)
    val format: MutableMap<StatType, String> = EnumMap(StatType::class.java)
    val indicator: MutableMap<StatType, String> = EnumMap(StatType::class.java)

    fun load(languageFile: LanguageFile) {
        StatType.values().forEach { type ->
            languageFile.getUnsafe("stat.name.${type.name}")?.let {
                name[type] = it
            }
            languageFile.getUnsafe("stat.format.${type.name}")?.let {
                format[type] = it
            }
            languageFile.getUnsafe("indicator.${type.name}")?.let {
                indicator[type] = it
            }
        }
    }
}