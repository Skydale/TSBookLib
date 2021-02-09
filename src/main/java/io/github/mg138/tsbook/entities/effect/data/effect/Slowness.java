package io.github.mg138.tsbook.entities.effect.data.effect;

import io.github.mg138.tsbook.entities.effect.EffectHandler;
import io.github.mg138.tsbook.entities.effect.data.EntityStatusEffect;
import io.github.mg138.tsbook.entities.effect.data.StatusEffect;
import io.github.mg138.tsbook.listener.event.damage.utils.StatCalculator;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.function.Consumer;

public class Slowness {
    public static final Consumer<EntityStatusEffect> slowness = effect -> {
        if (!(effect.target instanceof LivingEntity)) return;
        LivingEntity target = (LivingEntity) effect.target;
        StatusEffect statusEffect = effect.statusEffect;

        AttributeInstance attributeInstance = Objects.requireNonNull(target.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED));
        double old = attributeInstance.getBaseValue();
        attributeInstance.setBaseValue(StatCalculator.calculateModifier(old, -1 * statusEffect.power));

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                cancel();
            }

            @Override
            public void cancel() {
                super.cancel();
                attributeInstance.setBaseValue(old);
                EffectHandler.remove(target, statusEffect.type);
            }
        };

        EffectHandler.applyRawEffect(target, statusEffect, runnable, statusEffect.ticks, 0);
    };
}
