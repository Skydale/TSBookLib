package io.github.mg138.tsbook.setting.stat

import io.github.mg138.tsbook.stat.StatType
import io.github.mg138.tsbook.util.Translate
import java.util.*

object StatDisplay {
    val name: MutableMap<StatType, String> = EnumMap(StatType::class.java)
    val format: MutableMap<StatType, String> = EnumMap(StatType::class.java)

    fun load(translate: Translate) {
        StatType.values().forEach { type ->
            translate.getUnsafe("stat.name.${type.name}")?.let {
                name[type] = it
            }
            translate.getUnsafe("stat.format.${type.name}")?.let {
                format[type] = it
            }
        }
    }
}