package tsp.hexchat.util;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RGBUtil {
    private static final Pattern pattern = Pattern.compile("(#[a-fA-F0-9]{6})");

    public static String format(String message) {
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, ChatColor.of(color).toString());
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }
}