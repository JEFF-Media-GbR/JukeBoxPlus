package de.jeff_media.JukeBoxPlus;

import org.bukkit.ChatColor;

public class Messages {

    final String ADDED_DISK,
            ALREADY_ADDED,
            NOW_PLAYING;

    final String ENABLED, DISABLED, LOOP, SHUFFLE, AUTOSTART, STOP, RADIUS;

    final Main main;

    Messages(Main main) {
        this.main = main;

        ADDED_DISK = getMsg(Config.MSG_ADDED_DISC,"Added {NAME} to this Jukebox");
        ALREADY_ADDED = getMsg(Config.MSG_ALREADY_ADDED,"This Jukebox already contains {NAME}.");
        NOW_PLAYING = getMsg(Config.MSG_NOW_PLAYING, "&aNow playing {NAME}");
        ENABLED = getMsg(Config.MSG_ENABLED,"&aEnabled");
        DISABLED = getMsg(Config.MSG_DISABLED,"&cDisabled");
        LOOP = getMsg(Config.MSG_LOOP, "&6Loop");
        SHUFFLE = getMsg(Config.MSG_SHUFFLE,"&6Shuffle");
        AUTOSTART = getMsg(Config.MSG_AUTOSTART, "&6Autostart");
        STOP = getMsg(Config.MSG_STOP,"&6Stop");
        RADIUS = getMsg(Config.MSG_RADIUS,"&6Radius");
    }

    private String getMsg(String path, String defaultText) {
        return ChatColor.translateAlternateColorCodes('&',main.getConfig().getString(path,defaultText));
    }
}
