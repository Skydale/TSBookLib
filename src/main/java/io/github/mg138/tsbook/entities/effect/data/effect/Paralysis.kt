package io.github.mg138.tsbook.entities.effect.data.effect;

import com.comphenix.packetwrapper.WrapperPlayServerEntityLook;
import io.github.mg138.tsbook.entities.effect.EffectHandler;
import io.github.mg138.tsbook.entities.effect.data.EntityStatusEffect;
import io.github.mg138.tsbook.entities.effect.data.StatusEffect;
import io.github.mg138.tsbook.entities.effect.data.StatusEffectType;
import io.github.mg138.tsbook.items.data.stat.StatType;
import io.github.mg138.tsbook.listener.event.damage.DamageHandler;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.function.Consumer;

public class Paralysis {
    public static final Consumer<EntityStatusEffect> paralysis = effect -> {
        if (!(effect.target instanceof LivingEntity)) return;
        LivingEntity target = (LivingEntity) effect.target;
        StatusEffect statusEffect = effect.statusEffect;
        int period = 1;

        Location location = target.getLocation();
        Objects.requireNonNull(location.getWorld()).strikeLightningEffect(location);
        EffectHandler.apply(StatusEffectType.SLOWNESS, target, 100, (statusEffect.ticks / 20));

        WrapperPlayServerEntityLook look = new WrapperPlayServerEntityLook();
        look.setEntityID(target.getEntityId());

        BukkitRunnable runnable = new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (i > statusEffect.ticks) {
                    cancel();
                    return;
                }

                if (i % 4 == 0) {
                    if (!DamageHandler.simpleDamage(target, statusEffect.power, StatType.DAMAGE_THUNDER, true)) {
                        cancel();
                        return;
                    }
                }

                Location loc = target.getLocation();
                float yaw = (loc.getYaw() + 0.5F + 360F) % 360;

                look.setYaw(yaw);
                look.setPitch(loc.getPitch());
                look.setOnGround(target.isOnGround());
                look.broadcastPacket();

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
