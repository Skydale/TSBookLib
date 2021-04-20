package io.github.mg138.tsbook.item

import io.github.mg138.tsbook.setting.item.element.UnidentifiedSetting
import java.util.*

class Unidentified(unidSetting: UnidentifiedSetting, uuid: UUID): ItemBase(unidSetting, uuid) {
    val iden = unidSetting.iden
}