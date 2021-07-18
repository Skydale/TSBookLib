package io.github.mg138.tsbook.data

class DataMap : MutableIterable<MutableMap.MutableEntry<String, MutableMap<String, Data>>> {
    private val map: MutableMap<String, MutableMap<String, Data>> = HashMap()

    inline operator fun <reified T: Data> get(key: String, value: String): T? {
        return this[key]?.get(value) as? T
    }

    inline operator fun <reified T: Data> get(id: Identifier): T? {
        return this[id.key, id.value]
    }

    operator fun get(key: String) = map[key]

    operator fun set(key: String, value: String, data: Data) {
        this[key]?.set(value, data)
    }

    operator fun set(id: Identifier, data: Data) {
        this[id.key, id.value] = data
    }

    fun add(data: NamedData) {
        val id = data.getDataId()

        map.putIfAbsent(id.key, HashMap())

        this[id] = data
    }

    operator fun plusAssign(data: NamedData) = this.add(data)

    override fun iterator() = map.iterator()
}