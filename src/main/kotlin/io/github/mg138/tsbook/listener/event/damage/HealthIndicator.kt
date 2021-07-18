package io.github.mg138.tsbook.listener.event.damage

import io.github.mg138.tsbook.Book
import io.github.mg138.tsbook.event.BookDamageEvent
import io.github.mg138.tsbook.config.BookConfig
import io.github.mg138.tsbook.stat.type.StatType
import io.github.mg138.tsbook.util.RGBUtil.toChatColor
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
import kotlin.collections.HashMap
import kotlin.math.roundToInt

/* TODO
    Map<LivingEntity, BossBar> entities
    Map<Player, BossBar> players
    ---
    onEntityDeath:
    - get BossBar from entities and remove from all players after <some seconds>
    - remove Entity from entities
    ---
    onDamage:
    - take BossBar in entities with Entity and update
    - if damager is Player:
       - if players has player:
          - get BossBar from players with player and remove player from it
       - add player to BossBar
       - put player and BossBar into players
    Also: show blank if player doesn't have a BossBar displayed
 */

// TODO doesn't really need multiple indicators..
/*
object HealthIndicator : Listener {
    private val indicators: MutableMap<LivingEntity, BossBar> = HashMap()
    private val removingQueue: MutableMap<Player, BukkitRunnable> = HashMap()
    private val showingQueue: MutableMap<Player, BukkitRunnable> = HashMap()

    fun unload() {
        showingQueue.forEach { (_, runnable) -> runnable.cancel() }
        showingQueue.clear()
        removingQueue.forEach { (_, runnable) -> runnable.cancel() }
        removingQueue.clear()
        indicators.forEach { (_, bossBar) -> bossBar.removeAll() }
        indicators.clear()
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
            damageSum += damage
            types.append(type.getIndicator())
        }

        damageSum = -1 * ((damageSum * 10).roundToInt() / 10.0)
        val result = when {
            damageSum > 0 -> "&a+".toChatColor() + damageSum
            damageSum < 0 -> maxType.getIndicator() + damageSum + " &r&9- ".toChatColor() + types
            else -> ""
        }
        types.setLength(0)
        return result
    }

    private fun update(entity: LivingEntity, damages: Map<StatType, Double>): BossBar {
        val name = entity.customName ?: entity.name
        val maxHealth = (entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue * 10).roundToInt() / 10.0
        val health = (entity.health * 10).roundToInt() / 10.0

        val title = BookConfig.language.healthIndicator.title
            .replace("[name]", name)
            .replace("[health]", health.toString())
            .replace("[max_health]", maxHealth.toString())
            .replace("[damages]", translateDamages(damages))

        val healthPercentage = health / maxHealth
        val color = when {
            healthPercentage > 0.6 -> BarColor.GREEN
            healthPercentage > 0.3 -> BarColor.YELLOW
            else -> BarColor.RED
        }

        val bar = indicators[entity]?.let { bar ->
            bar.color = color
            bar.setTitle(title)
            bar
        } ?: Bukkit.createBossBar(title, color, BarStyle.SOLID)
        bar.progress = healthPercentage
        indicators[entity] = bar
        return bar
    }

    private fun showToPlayer(entity: Entity, player: Player, damages: Map<StatType, Double>) {
        if (entity !is LivingEntity) return

        removingQueue[player]?.cancel()
        removingQueue.remove(player)

        showingQueue[player]?.cancel()
        showingQueue.remove(player)

        val showing = object : BukkitRunnable() {
            override fun run() {
                val bar = update(entity, damages)
                bar.addPlayer(player)

                val remove = object : BukkitRunnable() {
                    override fun run() {
                        bar.removePlayer(player)
                    }
                }
                removingQueue[player] = remove
                remove.runTaskLater(Book.inst, 50)
            }
        }
        showingQueue[player] = showing
        showing.runTaskLater(Book.inst, 1)
    }

    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        val entity = event.entity
        val remove = object : BukkitRunnable() {
            override fun run() {
                indicators[entity]?.removeAll()
                indicators.remove(entity)
            }
        }
        remove.runTaskLater(Book.inst, 20)
    }

    @EventHandler
    fun onEntityDamage(event: BookDamageEvent) {
        val damager = event.damager
        if (damager is Player) showToPlayer(event.entity, damager, event.getDamages())
    }
}
 */