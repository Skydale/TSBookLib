package io.github.mg138.tsbook.stat.data

import io.github.mg138.tsbook.stat.Stat
import io.github.mg138.tsbook.stat.type.StatType
import java.lang.IllegalArgumentException
import kotlin.collections.HashMap

open class StatMap(private val map: MutableMap<StatType, Stat> = defaultMap()) : MutableStated {
    companion object {
        fun defaultMap(): MutableMap<StatType, Stat> = HashMap()
    }

    constructor(stats: StatMap) : this(stats.map)

    open fun toMap(): MutableMap<StatType, Stat> = HashMap<StatType, Stat>()
        .also { it.putAll(this) }

    open fun computeIfPresent(type: StatType, action: (StatType, Stat) -> Stat) =
        this[type]?.let { old ->
            action(type, old)
                .also { this[type] = it }
        }

    open fun computeIfAbsent(type: StatType, action: (StatType) -> Stat) =
        this[type] ?: action(type)
            .also { this[type] = it }

    open fun computeIfPresent(type: StatType, action: (Stat) -> Stat) =
        this[type]?.let { old ->
            action(old)
                .also { this[type] = it }
        }

    open fun computeIfAbsent(type: StatType, action: () -> Stat) =
        this[type] ?: action()
            .also { this[type] = it }

    open fun containsType(type: StatType) = this.types().contains(type)
    open fun isEmpty() = map.isEmpty()

    open operator fun get(type: StatType) = this.getStat(type)
    open operator fun set(type: StatType, stat: Stat) = this.putStat(type, stat)

    // Stated

    override fun types() = map.keys
    override fun stats() = map.values
    override fun getStatResult(type: StatType) = this.getStat(type)?.result() ?: 0.0
    override fun getStat(type: StatType) = map[type]

    // MutableStated

    /**
     * Note that this gets its value from the internal map, skipping [getStat].
     *
     * This is due to calculation problems.
     * Say, we have an [io.github.mg138.tsbook.attribute.stat.identified.data.IdentifiedStat]
     *
     * Its contents are:
     * ```
     * {
     *      Stat: {
     *          DAMAGE_IGNIS: 100
     *      },
     *      Identification: {
     *          DAMAGE_IGNIS: 0.5
     *      }
     * }
     * ```
     *
     * If we use the overridden [io.github.mg138.tsbook.attribute.stat.identified.data.IdentifiedStat.getStat],
     *
     * the results would be 50 if we add {DAMAGE_IGNIS, 100} to it.
     *
     * Steps:
     * 1. getStat(DAMAGE_IGNIS)
     *    - 100 * 0.5 (Stat * Identification)
     *    - 50
     * 2. 50 + 50
     *    - 100
     * 3. putStat(DAMAGE_IGNIS, 100)
     *    - 100 * 0.5 (Stat * Identification)
     *    - 50
     */
    override fun addStat(type: StatType, stat: Stat) =
        stat.plus(map[type])
            .also { this[type] = it }

    override fun putStat(stat: Pair<StatType, Stat>) = this.putStat(stat.first, stat.second)
    override fun putStat(type: StatType, stat: Stat) = map.put(type, stat)
    override fun remove(type: StatType) = map.remove(type)

    // Translate

    open fun applyPlaceholder(string: String, type: StatType) =
        this.translate(type)?.let {
            string.replace(type.identifier, it)
        } ?: string

    open fun translate(type: StatType) =
        this.getStat(type)
            ?.applyPlaceholder(type.getFormat())
            ?.replace("[name]", type.toString())
            ?.replace("[percentage]", "100%")

    // Any

    override fun toString() = map.toString()
    override fun hashCode() = map.hashCode()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (other !is StatMap) return false

        if (map != other.map) return false

        return true
    }

    // Iterable

    override fun iterator() = object : Iterator<Pair<StatType, Stat>> {
        val it = this@StatMap
        val keys = it.map.keys.iterator()

        override fun hasNext() = keys.hasNext()

        override fun next() = keys.next().let { type ->
            it.getStat(type)?.let {
                Pair(type, it)
            } ?: throw IllegalArgumentException(
                """
                Error when iterating through ${it::class.simpleName}: it doesn't contain ${type.identifier}!
                Did the values change during iteration?
                Contents: $it
                """.trimMargin()
            )
        }
    }
}