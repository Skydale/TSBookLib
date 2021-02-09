package io.github.mg138.tsbook.entities.effect.data.effect;

import io.github.mg138.tsbook.entities.effect.EffectHandler;
import io.github.mg138.tsbook.entities.effect.data.EntityStatusEffect;
import io.github.mg138.tsbook.entities.effect.data.StatusEffect;
import io.github.mg138.tsbook.items.data.stat.StatType;
import io.github.mg138.tsbook.listener.event.damage.utils.DamageHandler;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public class Burning {
    public static final Consumer<EntityStatusEffect> burning = effect -> {
        if (!(effect.target instanceof LivingEntity)) return;
        LivingEntity target = (LivingEntity) effect.target;
        StatusEffect statusEffect = effect.statusEffect;
        int period = 10;

        BukkitRunnable runnable = new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (!DamageHandler.simpleDamage(target, statusEffect.power, StatType.DAMAGE_IGNIS, true) || i > statusEffect.ticks) {
                    cancel();
                    return;
                }

                target.getWorld().spawnParticle(Particle.LAVA, target.getLocation(), 6);
                i += period;
            }

            @Override
            public void cancel() {
                super.cancel();
                EffectHandler.remove(target, statusEffect.type);
            }
        };

        EffectHandler.applyRawEffect(target, statusEffect, runnable, 0, period);
    };
}
