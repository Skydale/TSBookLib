package io.github.mg138.tsbook.command;

import io.github.mg138.tsbook.Book;
import io.github.mg138.tsbook.entities.effect.EffectHandler;
import io.github.mg138.tsbook.entities.effect.data.StatusEffectType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Effect {
    public static boolean call(CommandSender sender) {
        sender.sendMessage(Book.getCfg().translate.translate("commands.feedback.effect"));
        return true;
    }

    public static boolean call(CommandSender sender, String playerName, String effectName, String literalPower, String literalTicks) {
        Player player = Book.getInst().getServer().getPlayer(playerName);
        if (player == null) {
            sender.sendMessage(Book.getCfg().translate.translate("errors.player_not_found"));
            return false;
        }
        try {
            StatusEffectType type = StatusEffectType.valueOf(effectName.toUpperCase());
            double power = Double.parseDouble(literalPower);
            int ticks = Integer.parseInt(literalTicks);
            EffectHandler.apply(type, player, power, ticks);
            sender.sendMessage(Book.getCfg().translate.translate("messages.effect.applied"));
            return true;
        } catch (NumberFormatException e) {
            sender.sendMessage(Book.getCfg().translate.translate("errors.should_put_number"));
            return false;
        }
    }

    public static boolean clear(CommandSender sender, String playerName) {
        Player player = Book.getInst().getServer().getPlayer(playerName);
        if (player == null) {
            sender.sendMessage(Book.getCfg().translate.translate("errors.player_not_found"));
            return false;
        }
        try {
            EffectHandler.remove(player);
            sender.sendMessage(Book.getCfg().translate.translate("messages.effect.cleared"));
            return true;
        } catch (NullPointerException e) {
            sender.sendMessage(Book.getCfg().translate.translate("errors.effect.no_active_effect"));
            return false;
        }
    }
}
