package io.github.mg138.tsbook.item.attribute

enum class DefaultIdentifier(val identifier: String) {
    ITEM("item"),
    UNID("unid");

    override fun toString() = identifier
}