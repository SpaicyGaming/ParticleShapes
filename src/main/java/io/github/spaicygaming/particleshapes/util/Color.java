package io.github.spaicygaming.particleshapes.util;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public final class Color {

    private Color() {
    }

    /**
     * Colors the string using '&'
     *
     * @param string The string to color
     * @return the colored string
     */
    public static String format(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Colors the list of strings using '&'
     *
     * @param list The list of strings to color
     * @return the colored list
     */
    public static List<String> format(List<String> list) {
        List<String> result = new ArrayList<>();
        for (String string : list) {
            result.add(Color.format(string));
        }
        return result;
    }
}
