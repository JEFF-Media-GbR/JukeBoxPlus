package de.jeff_media.JukeBoxPlus;

import org.bstats.bukkit.Metrics;

import java.io.IOException;

public class Config {

    final Main main;
    public static final String MAX_JUKEBOX_RADIUS = "max-jukebox-radius";
    public static final String DEFAULT_JUKEBOX_RADIUS = "default-jukebox-radius";
    public static final String RADIUS_CHANGE_INTERVAL = "radius-change-interval";
    public static final String DISC_NAME = "disc-name";
    public static final String DISC_LORE = "disc-lore";


    public static final String MSG_ALREADY_ADDED = "message-already-added";
    public static final String MSG_ADDED_DISC = "message-added-disc";

    public static final String MSG_ENABLED = "message-enabled";
    public static final String MSG_DISABLED = "message-disabled";
    public static final String MSG_LOOP = "message-loop";
    public static final String MSG_SHUFFLE = "message-shuffle";
    public static final String MSG_AUTOSTART = "message-autostart";
    public static final String MSG_STOP = "message-stop";
    public static final String MSG_RADIUS = "message-radius";

    public static final String CHECK_FOR_UPDATES = "check-for-updates";
    public static final String UPDATE_CHECK_INTERVAL = "check-for-updates-interval";
    public static final String CONFIG_VERSION = "config-version";
    public static final String CONFIG_PLUGIN_VERSION = "plugin-version";

    Config(Main main) {
        this.main = main;

        main.getConfig().addDefault(CHECK_FOR_UPDATES, "true");
        main.getConfig().addDefault(UPDATE_CHECK_INTERVAL,4);

        main.getConfig().addDefault("ticks-per-bossbar-message", 80);
        main.getConfig().addDefault(MAX_JUKEBOX_RADIUS, 512);
        main.getConfig().addDefault("gui-title", "§4§l[§c§lJukeboxPlus§4§l]");
        main.getConfig().addDefault("tick-delay", 40);

        main.getConfig().addDefault("button-loop-enabled", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmM4ZGVmNjdhMTI2MjJlYWQxZGVjZDNkODkzNjQyNTdiNTMxODk2ZDg3ZTQ2OTgxMzEzMWNhMjM1YjVjNyJ9fX0=");
        main.getConfig().addDefault("button-loop-disabled", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTUzZGQ0NTc5ZWRjMmE2ZjIwMzJmOTViMWMxODk4MTI5MWI2YzdjMTFlYjM0YjZhOGVkMzZhZmJmYmNlZmZmYiJ9fX0=");
        main.getConfig().addDefault("button-shuffle-enabled", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzY1Y2IxODVjNjQxY2JlNzRlNzBiY2U2ZTZhMWVkOTBhMTgwZWMxYTQyMDM0ZDVjNGFlZDU3YWY1NjBmYzgzYSJ9fX0==");
        main.getConfig().addDefault("button-shuffle-disabled", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTU1MTE1M2ExNTE5MzU3YjYyNDFhYjFkZGNhZTgzMWRmZjA4MDA3OWMwYjI5NjA3OTdjNzAyZGQ5MjI2NjgzNSJ9fX0==");
        main.getConfig().addDefault("button-autostart-enabled", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjVhODRlNjM5NGJhZjhiZDc5NWZlNzQ3ZWZjNTgyY2RlOTQxNGZjY2YyZjFjODYwOGYxYmUxOGMwZTA3OTEzOCJ9fX0=");
        main.getConfig().addDefault("button-autostart-disabled", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTc5NmRlNjAxYjUxYjk4Y2VhNWI4OTk5NDRiMWE0ODcxODQ4NDAxNzRkODQ4ZjhjNTdmZTBiNDRjODQ5Y2U0MCJ9fX0=");
        main.getConfig().addDefault("button-stop", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjBkMzI3NTJkYzRiNWIxMjgyOTE4NmJjZjlkM2RjNDhiYmYxN2U1ODhhZGZhNDRiODkyNTFkYzVhYjAyY2JkZiJ9fX0=");
        main.getConfig().addDefault("button-radius-plus", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzIzMzJiNzcwYTQ4NzQ2OTg4NjI4NTVkYTViM2ZlNDdmMTlhYjI5MWRmNzY2YjYwODNiNWY5YTBjM2M2ODQ3ZSJ9fX0=");
        main.getConfig().addDefault("button-radius-minus", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzJjYmRjOWQ0YzU5MGVhYzI4NWE0NTQ0ZjJiMWUwNjhiZDI3ZmQ1MjE3M2FjOGQ3Njc5MDEzODIzY2JhYjk1YSJ9fX0=");
    }

}
