package io.github.mg138.tsbook.players.util

import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.listener.event.damage.utils.CustomDamageEvent
import io.github.mg138.tsbook.setting.BookConfig
import io.github.mg138.tsbook.stat.StatType
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.scheduler.BukkitRunnable
import java.lang.StringBuilder
import java.util.*
import kotlin.math.roundToInt

// TODO UPDATE THIS PLZ
class HealthIndicator : Listener {
    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        val entity = event.entity
        val remove: BukkitRunnable = object : BukkitRunnable() {
            override fun run() {
                healthIndicators[entity]?.forEach { (_, bossBar) -> bossBar.removeAll() }
                healthIndicators.remove(entity)
            }
        }
        remove.runTaskLater(Book.inst, 20)
    }

    @EventHandler
    fun onEntityDamage(event: CustomDamageEvent) {
        val entity = event.entity
        val damager = event.damager
        if (damager is Player) {
            showToPlayer(entity, damager, event.getDamages())
        }
    }

    companion object {
        val healthIndicators: MutableMap<LivingEntity, MutableMap<Player, BossBar>> = HashMap()
        val removeHealthIndicator: MutableMap<Player, MutableMap<LivingEntity, BukkitRunnable>> = HashMap()
        private val showingQueue: MutableMap<Player, BukkitRunnable> = HashMap()

        fun unload() {
            showingQueue.forEach { (_, runnable) -> runnable.cancel() }
            showingQueue.clear()
            healthIndicators.forEach { (_, map) -> map.forEach { (_, bossBar) -> bossBar.removeAll() } }
            healthIndicators.clear()
            removeHealthIndicator.clear()
        }

        private fun translateDamages(damages: Map<StatType, Double>): String {
            val types = StringBuilder()
            var maxType = StatType.DAMAGE_NONE
            var maxDamage = 0.0

            var damageSum = 0.0

            damages.forEach { (type, damage) ->
                if (damage > maxDamage) {
                    maxDamage = damage
                    maxType = type
                }
                damageSum += (damage * 10).roundToInt() / 10.0
                types.append(BookConfig.translate.translate("indicator.$type"))
            }
            damageSum *= -1
            return when {
                damageSum > 0 -> BookConfig.translate.translateString("&a+") + damageSum
                damageSum < 0 -> BookConfig.translate.translate("indicator.$maxType") + damageSum + " - " + types.toString()
                else -> {
                    types.setLength(0)
                    ""
                }
            }
        }

        fun updateHealth(entity: LivingEntity, player: Player, damages: Map<StatType, Double>): BossBar {
            val customName = entity.customName ?: entity.name
            val maxHealth = (entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue * 10).roundToInt() / 10.0
            val health = (entity.health * 10).roundToInt() / 10.0

            val title = BookConfig.translate.translate("health_indicator.title")
                .replace("[name]", customName)
                .replace("[health]", health.toString())
                .replace("[max_health]", maxHealth.toString())
                .replace("[damages]", translateDamages(damages))

            val healthPercentage = health / maxHealth
            val color = when {
                healthPercentage > 0.6 -> BarColor.GREEN
                healthPercentage > 0.3 -> BarColor.YELLOW
                else -> BarColor.RED
            }

            val bar: BossBar
            val players: MutableMap<Player, BossBar>

            if (healthIndicators.containsKey(entity)) {
                players = healthIndicators[entity]!!
                players[player]?.let {
                    it.color = color
                    it.setTitle(title)
                } ?: run {
                    bar = Bukkit.createBossBar(title, color, BarStyle.SOLID)
                }
            } else {
                players = HashMap()
                bar = Bukkit.createBossBar(
                    title, color, BarStyle.SOLID
                )
            }
            bar.progress = healthPercentage
            players[player] = bar
            healthIndicators[entity] = players
            return bar
        }

        fun showToPlayer(entity: Entity?, player: Player?, damages: Map<StatType, Double>) {
            if (entity !is LivingEntity) return
            if (removeHealthIndicator.containsKey(player)) {
                val runnables: Map<LivingEntity, BukkitRunnable?>? = removeHealthIndicator[player]
                if (runnables!!.containsKey(entity)) {
                    runnables[entity]!!.cancel()
                    runnables.remove(entity)
                }
            }
            if (showingQueue.containsKey(player)) {
                showingQueue[player]!!.cancel()
                showingQueue.remove(player)
            }
            val runnable: BukkitRunnable = object : BukkitRunnable() {
                override fun run() {
                    val bossBar = updateHealth(entity, player, damages)
                    bossBar!!.addPlayer(player!!)
                    val remove: BukkitRunnable = object : BukkitRunnable() {
                        override fun run() {
                            if (healthIndicators.containsKey(entity)) {
                                val players: Map<Player, BossBar?>? = healthIndicators[entity]
                                if (players!!.containsKey(player)) {
                                    players[player]!!.removeAll()
                                    players.(this)(player)
                                }
                            }
                        }
                    }
                    val runnables = removeHealthIndicator.getOrDefault(player, HashMap())
                    runnables[entity] = remove
                    removeHealthIndicator[player] = runnables
                    remove.runTaskLater(Book.inst, 50)
                }
            }
            showingQueue[player] = runnable
            runnable.runTaskLater(Book.inst, 1)
        }
    }
}