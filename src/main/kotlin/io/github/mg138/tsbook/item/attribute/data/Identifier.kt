package io.github.mg138.tsbook.item.attribute.data

class Identifier(val key: String, val name: String) {
    fun sameKey(key: String) = this.key == key

    override fun toString(): String {
        return "$key:$name"
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Identifier) return false

        return (other.key == this.key) && (other.name == this.name)
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }

    companion object {
        fun of(string: String): Identifier? {
            val index = string.indexOf(':')
            if (index == -1) return null

            return Identifier(string.substring(0, index), string.substring(index + 1))
        }
    }

    object PresetKey {
        val item = "item"
        val unid = "unid"

        fun item(name: String) = Identifier(item, name)
        fun unid(name: String) = Identifier(unid, name)
    }
}