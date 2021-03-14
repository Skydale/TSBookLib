package io.github.mg138.tsbook.command.admin

import io.github.mg138.tsbook.Book.Companion.setting
import io.github.mg138.tsbook.command.util.error.CommandError
import io.github.mg138.tsbook.command.util.CommandUtil
import io.github.mg138.tsbook.command.util.error.CommandEffectError
import io.github.mg138.tsbook.entities.effect.data.StatusEffectType
import io.github.mg138.tsbook.entities.effect.EffectHandler
import java.lang.NumberFormatException
import java.lang.NullPointerException
import org.bukkit.command.CommandSender

object Effect {
    fun call(sender: CommandSender): Boolean {
        sender.sendMessage(setting.translate.translate("commands.feedback.effect"))
        return true
    }

    fun call(
        sender: CommandSender,
        playerName: String,
        effectName: String,
        literalPower: String,
        literalTicks: String
    ): Boolean {
        val player = CommandUtil.getPlayerByName(playerName, sender) ?: return false

        return try {
            val type = StatusEffectType.valueOf(effectName.toUpperCase())
            val power = literalPower.toDouble()
            val ticks = literalTicks.toInt()
            EffectHandler.apply(type, player, power, ticks)
            sender.sendMessage(setting.translate.translate("messages.effect.applied"))
            true
        } catch (e: NumberFormatException) {
            CommandError.shouldPutNumber(sender)
            false
        }
    }

    fun clear(sender: CommandSender, playerName: String): Boolean {
        val player = CommandUtil.getPlayerByName(playerName, sender) ?: return false

        return try {
            EffectHandler.remove(player)
            sender.sendMessage(setting.translate.translate("messages.effect.cleared"))
            true
        } catch (e: NullPointerException) {
            CommandEffectError.noActiveEffect(sender)
            false
        }
    }
}