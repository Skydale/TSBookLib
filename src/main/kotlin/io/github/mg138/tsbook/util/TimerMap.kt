package io.github.mg138.tsbook.util

class TimerMap<T> {
    private val map: MutableMap<T, Long> = HashMap()

    operator fun get(key: T, period: Long): Boolean {
        val now = System.currentTimeMillis()
        val last = map.getOrDefault(key, 0L)

        if ((now - last) <= period) return false

        map[key] = now
        return true
    }

    inline operator fun invoke(key: T, period: Long, action: (T) -> Unit) {
        if (get(key, period)) {
            action(key)
        }
    }

    fun remove(key: T) = map.remove(key)

    fun clear() = map.clear()
}