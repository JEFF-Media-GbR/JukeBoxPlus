package de.jeff_media.JukeBoxPlus;

import org.bukkit.ChatColor;

public class Messages {

    final String ADDED_DISK,
            ALREADY_ADDED;
    Main main;

    Messages(Main main) {
        this.main = main;

        ADDED_DISK = getMsg(Config.MSG_ADDED_DISC,"Added {NAME} to this Jukebox");
        ALREADY_ADDED = getMsg(Config.MSG_ALREADY_ADDED,"This Jukebox already contains {NAME}.");
    }

    private String getMsg(String path, String defaultText) {
        return ChatColor.translateAlternateColorCodes('&',main.getConfig().getString(path,defaultText));
    }
}
