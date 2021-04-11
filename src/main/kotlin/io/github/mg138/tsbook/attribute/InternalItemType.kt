package io.github.mg138.tsbook.attribute

enum class InternalItemType(val string: String) {
    ITEM("item"),
    UNID("unid");

    override fun toString() = string

    companion object {
        fun of(string: String): InternalItemType? {
            values().forEach {
                if (it.string == string) {
                    return it
                }
            }
            return null
        }
    }
}