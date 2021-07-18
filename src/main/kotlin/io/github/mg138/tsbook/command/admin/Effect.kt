package io.github.mg138.tsbook.command.admin

/*
import dev.reactant.reactant.core.component.Component
import io.github.mg138.tsbook.command.util.error.CommandError
import io.github.mg138.tsbook.entity.effect.api.EffectManager
import io.github.mg138.tsbook.entity.effect.data.EffectProperty
import io.github.mg138.tsbook.player.toPlayer
import io.github.mg138.tsbook.config.BookConfig
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.lang.IllegalArgumentException

@Component
class Effect(
    private val effectManager: EffectManager
) {
    fun call(sender: CommandSender): Boolean {
        sender.sendMessage(BookConfig.language.commands.feedback.effect)
        return true
    }

    fun call(sender: CommandSender, player: String, id: String, power: String, ticks: String) = try {
        player.toPlayer(sender)?.let {
            call(sender, it, id, power.toDouble(), ticks.toLong())
        } ?: false
    } catch (e: NumberFormatException) {
        CommandError.shouldPutInteger(sender)
        false
    } catch (e: IllegalArgumentException) {
        CommandError.noSuchOption(sender)
        false
    }

    fun call(sender: CommandSender, player: Player, type: EffectType, power: Double, ticks: Long): Boolean {
        effectManager.activate(type, EffectProperty(player, power, ticks))
        sender.sendMessage(BookConfig.language.messages.effect.applied)
        return true
    }

    fun clear(sender: CommandSender, playerName: String) =
        playerName.toPlayer(sender)?.let {
            clear(sender, it)
        } ?: false

    fun clear(sender: CommandSender, player: Player): Boolean {
        effectManager.removeAll(player)
        sender.sendMessage(BookConfig.language.messages.effect.cleared)
        return true
    }
}
*/