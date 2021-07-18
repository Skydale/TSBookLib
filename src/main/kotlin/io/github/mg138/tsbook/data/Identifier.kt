package io.github.mg138.tsbook.data

class Identifier(val key: String, val value: String) {
    fun sameKey(key: String) = this.key == key

    override fun equals(other: Any?): Boolean {
        if (other !is Identifier) return false

        return (other.key == this.key) && (other.value == this.value)
    }

    override fun hashCode() = 31 * key.hashCode() + value.hashCode()

    override fun toString() = "$key:$value"

    companion object {
        fun of(string: String): Identifier? {
            val index = string.indexOf(':')
            if (index == -1) return null

            return Identifier(string.substring(0, index), string.substring(index + 1))
        }
    }

    object PresetKey {
        const val tsbook = "tsbook"
        const val item = "item"
        const val unid = "unid"

        fun tsbook(name: String) = Identifier(tsbook, name)
        fun item(name: String) = Identifier(item, name)
        fun unid(name: String) = Identifier(unid, name)
    }
}