package io.github.mg138.tsbook.listener.event.damage

//TODO make it prettier :/
/*
class DamageHandler(
    private val eventService: EventService,
    mythicMobService: MythicMobService
) : LifeCycleHook {
    override fun onDisable() {
        damageCD.clear()
    }

    private val mythicHelper = mythicMobService.mythicMobHelper
    private val rand = Random()
    private val damageCD: MutableMap<Player, Long> = HashMap()

    private fun debug(player: Player, message: String) {
        if (DebugMode.has(player, DebugMode.DebugOption.ON_DAMAGE)) {
            player.sendMessage(message)
        }
    }

    fun remove(player: Player) {
        damageCD.remove(player)
    }

    /*
    fun damagedByEntity(event: EntityDamageByEntityEvent, defense: StatMap) {
        val damager = event.damager

        when {
            mythicHelper.isMythicMob(damager) -> {
                MobConfig[mythicHelper.getMythicMobInstance(damager).type.internalName]?.let {
                    complexDamage(event, it.stats, defense)
                }
            }
            damager is Player -> {
                if (System.currentTimeMillis() - damageCD.getOrDefault(damager, 0L) <= 500) {
                    event.isCancelled = true
                    return
                }
                damageCD[damager] = System.currentTimeMillis()

                val identifiedStats: MutableList<IdentifiedStat> = LinkedList()
                damager.equipment?.itemInMainHand?.let { item ->
                    //ItemUtil.getInstByItem(item)?.itemStat?.let { identifiedStats.add(it) }
                }
                ArcticGlobalDataService.inst.getData<PlayerData>(damager, PlayerData::class)
                //?.equipment?.forEach { _, armor -> armor.itemStat?.let { identifiedStats.add(it) } }

                complexDamage(event, StatUtil.combine(identifiedStats), defense)
            }
            damager is Arrow -> {
                val identifiedStats: MutableList<IdentifiedStat> = LinkedList()
                damager.persistentDataContainer[ItemUtil.UUID_ARRAY_KEY, UUIDArrayTag]
                    ?.forEach { uuid ->
                        ItemUtil.ITEM_CACHE[uuid]?.let { inst ->
                            //inst.itemStat?.let { identifiedStats.add(it) }
                        }
                    }
                complexDamage(event, StatUtil.combine(identifiedStats), defense)
            }
        }
    }

    fun complexDamage(event: EntityDamageByEntityEvent, stats: StatMap, defense: StatMap) {
        val entity = event.entity as LivingEntity

        val damager = when (val entityDamager = event.damager) {
            is LivingEntity -> entityDamager
            is Projectile -> entityDamager.shooter.let { it as? LivingEntity ?: return }
            else -> return
        }

        val customEvent = CustomDamageEvent(entity, damager)

        var usedModifier = 0.0
        StatUtil.getModifier(stats).forEach { (type, modifier) ->
            when (type) {
                StatType.MODIFIER_HELL -> if (MobTypeUtil.isHellish(entity.type)) usedModifier += modifier.getStat() / 100
                StatType.MODIFIER_MOBS -> if (MobTypeUtil.isMob(entity.type)) usedModifier += modifier.getStat() / 100
                StatType.MODIFIER_PLAYER -> if (entity.type == EntityType.PLAYER) usedModifier += modifier.getStat() / 100
                StatType.MODIFIER_ARTHROPOD -> if (MobTypeUtil.isArthropod(entity.type)) usedModifier += modifier.getStat() / 100
                StatType.MODIFIER_UNDERWATER -> if (MobTypeUtil.isWatery(entity.type)) usedModifier += modifier.getStat() / 100
                StatType.MODIFIER_UNDEAD -> if (MobTypeUtil.isUndead(entity.type)) usedModifier += modifier.getStat() / 100
                else -> Unit
            }
        }

        if (damager is Player) {
            debug(
                damager, "&eModifier: &f$usedModifier\n".toChatColor()
            )
        }

        val damageSum = damage(damager, stats, defense, usedModifier, customEvent)
        elementalEffect(damager, entity, stats, usedModifier)
        effect(damager, entity, stats, damageSum)

        if (damager is Player) {
            debug(
                damager,
                ("&cDamageSum: &f$damageSum" +
                        "\n  &cDamages: &f${customEvent.getDamages()}").toChatColor()
            )
        }

        event.damage = damageSum
        entity.maximumNoDamageTicks = 0
        entity.noDamageTicks = 0
        Bukkit.getPluginManager().callEvent(customEvent)
    }

    private fun roll(chance: Double): Boolean = rand.nextDouble() < chance

    private fun roundChance(chance: Double): Double = chance % 1

    private fun getStrikes(chance: Double): Int {
        return when {
            chance < 1 -> 0
            else -> chance.toInt()
        }
    }

    private fun damage(
        damager: LivingEntity,
        stats: StatMap,
        defense: StatMap,
        modifier: Double,
        event: CustomDamageEvent
    ): Double {
        val damages = StatUtil.getDamage(stats)
        if (damages.isEmpty()) return 0.0

        val critDamage = stats.getStatOut(StatType.POWER_CRITICAL).div(100)
        val critChance: Double
        val certainStrikes: Int
        stats.getStatOut(StatType.CHANCE_CRITICAL).div(100).let {
            critChance = roundChance(it)
            certainStrikes = getStrikes(it)
        }

        if (damager is Player) {
            debug(
                damager,
                "&9CritChance: &f$critChance &7/ &cCritDamage: &f$critDamage &7/ &aCertainCritStrikes: &f$certainStrikes\n".toChatColor()
            )
        }

        var damageSum = 0.0
        damages.forEach { (damageType, rawDamage) ->
            val damage = when (damageType) {
                StatType.DAMAGE_TRUE -> {
                    StatUtil.calculateTrueDamage(
                        damage = rawDamage.getStat(),
                        defense = defense.getStatOut(StatType.DEFENSE_TRUE),
                        modifier = 1 + modifier
                    )
                }
                else -> {
                    StatUtil.calculateDamage(
                        damage = rawDamage.getStat(),
                        defense = defense.getStatOut(StatTables.damageToDefense[damageType]!!),
                        modifier = 1 + modifier + (critDamage * (certainStrikes + if (roll(critChance)) 1 else 0))
                    )
                }
            }
            event.addDamage(damageType, damage)
            damageSum += damage
        }
        return damageSum
    }

    private fun effect(damager: LivingEntity, entity: LivingEntity, stats: StatMap, damageSum: Double) {
        val effectChance = StatUtil.getEffectChance(stats)
        if (effectChance.isEmpty()) return

        val effectPower = StatUtil.getEffectPower(stats)

        effectChance.forEach { (type, rawChance) ->
            val chance: Double
            val strikes: Int
            rawChance.getStat().div(100).let {
                chance = roundChance(it)
                strikes = getStrikes(it) + if (roll(chance)) 1 else 0
            }

            if (damager is Player) {
                debug(
                    damager,
                    "&b$type: &7{ &9EffectChance: &f$chance &7/ &aCertainEffectStrikes: &f$strikes &7}\n".toChatColor()
                )
            }

            if (strikes > 0) {
                when (type) {
                    StatType.CHANCE_DRAIN -> {
                        val rawPower = effectPower.getStatOut(type)
                        if (rawPower > 0) {
                            val result = damager.health + damageSum * min(rawPower / 100, 0.0)
                            val maxHealth = damager.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue
                            damager.health = min(result, maxHealth)
                        }
                    }
                    StatType.CHANCE_SLOWNESS -> {
                        val rawPower = effectPower.getStatOut(type)
                        EffectManager.apply(
                            entity,
                            EffectType.SLOWNESS,
                            strikes * rawPower / 100,
                            (strikes * rawPower * 90).toLong()
                        )
                    }
                    StatType.CHANCE_LEVITATION -> {
                        val ticks = 20 * strikes * effectPower.getStatOut(type)
                        if (ticks >= 20) EffectManager.apply(entity, EffectType.LEVITATION, 0.0, ticks.toLong())
                    }
                    StatType.CHANCE_NAUSEOUS -> {
                        val ticks = 20 * strikes * effectPower.getStatOut(type)
                        if (ticks >= 20) EffectManager.apply(entity, EffectType.NAUSEOUS, 0.0, ticks.toLong())
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun elementalEffect(damager: LivingEntity, entity: LivingEntity, stats: StatMap, modifier: Double) {
        val elementalDamages = StatUtil.getElementalDamage(stats)
        if (elementalDamages.isEmpty()) return

        var certainStrike: Int
        val chance: Double
        (25 + stats.getStatOut(StatType.AFFINITY_ELEMENT)).div(100).let {
            chance = roundChance(it)
            certainStrike = getStrikes(it)
        }

        elementalDamages.forEach { (type, rawDamage) ->
            val damage = StatUtil.calculateModifier(rawDamage.getStat(), modifier)
            val strikes = (certainStrike + if (roll(chance)) 1 else 0)

            if (damager is Player) {
                debug(
                    damager,
                    "&b$type: &7{ &cElementDamage: &f$damage &7/ &9ElementChance: &f$chance &7/ &aCertainElementStrikes: &f$strikes &7}\n".toChatColor()
                )
            }

            when (type) {
                StatType.DAMAGE_IGNIS -> {
                    val power = (damage / 8) * strikes
                    val tick = (damage / 6).toLong()
                    if (power > 20 && tick > 10) {
                        EffectManager.apply(entity, EffectType.BURNING, power, tick)
                    }
                }
                StatType.DAMAGE_PHYSICAL -> {
                    val power = (damage / 12) * strikes
                    val tick = (damage / 14).toLong()
                    if (power > 20 && tick > 10) {
                        EffectManager.apply(entity, EffectType.BLEEDING, power, tick)
                    }
                }
                StatType.DAMAGE_TEMPUS -> {
                    val power = (damage / 8) * strikes
                    val tick = (600 * (damage / (damage + 1000))).toLong()
                    if (power > 20 && tick > 60) {
                        EffectManager.apply(entity, EffectType.PARALYSIS, power, tick)
                    }
                }
                else -> Unit
            }
        }
    }
     */

    fun simpleDamage(entity: Entity, damage: Double) = simpleDamage(entity, damage, StatType.DAMAGE_NONE, false)

    fun simpleDamage(
        entity: Entity,
        damage: Double,
        damageType: StatType,
        display: Boolean = true
    ): Boolean {
        if (entity.isDead) return false
        if (entity !is LivingEntity) return false
        if (damage == 0.0) return true

        entity.damage(damage)

        entity.maximumNoDamageTicks = 0
        entity.noDamageTicks = 0

        if (display && StatTypes.damages.contains(damageType)) {
            DamageIndicator.displayDamage(damage, damageType, entity.location)
        }
        return true
    }
}
 */