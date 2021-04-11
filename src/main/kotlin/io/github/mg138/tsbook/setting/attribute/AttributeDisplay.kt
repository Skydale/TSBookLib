package io.github.mg138.tsbook.setting.attribute

import io.github.mg138.tsbook.attribute.stat.StatType
import io.github.mg138.tsbook.util.translate.TranslatableFile
import java.util.*

object AttributeDisplay {
    val statName: MutableMap<StatType, String> = EnumMap(StatType::class.java)
    val statFormat: MutableMap<StatType, String> = EnumMap(StatType::class.java)
    val statIndicator: MutableMap<StatType, String> = EnumMap(StatType::class.java)

    fun load(languageFile: TranslatableFile) {
        StatType.values().forEach { type ->
            languageFile.getUnsafe("stat.name.${type.name}")?.let {
                statName[type] = it
            }
            languageFile.getUnsafe("stat.format.${type.name}")?.let {
                statFormat[type] = it
            }
            languageFile.getUnsafe("indicator.${type.name}")?.let {
                statIndicator[type] = it
            }
        }
    }
}