package de.jeff_media.JukeBoxPlus;

public class Config {

    Main main;

    Config(Main main) {
        this.main = main;

        main.getConfig().addDefault("ticks-per-bossbar-message", 80);
        main.getConfig().addDefault("max-jukebox-radius", 512);
        main.getConfig().addDefault("gui-title", "§4§l[§c§lJukeboxPlus§4§l]");
        main.getConfig().addDefault("tick-delay", 40);

        main.getConfig().addDefault("button-loop-enabled", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmM4ZGVmNjdhMTI2MjJlYWQxZGVjZDNkODkzNjQyNTdiNTMxODk2ZDg3ZTQ2OTgxMzEzMWNhMjM1YjVjNyJ9fX0=");
        main.getConfig().addDefault("button-loop-disabled", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTUzZGQ0NTc5ZWRjMmE2ZjIwMzJmOTViMWMxODk4MTI5MWI2YzdjMTFlYjM0YjZhOGVkMzZhZmJmYmNlZmZmYiJ9fX0=");
        main.getConfig().addDefault("button-shuffle-enabled", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzY1Y2IxODVjNjQxY2JlNzRlNzBiY2U2ZTZhMWVkOTBhMTgwZWMxYTQyMDM0ZDVjNGFlZDU3YWY1NjBmYzgzYSJ9fX0==");
        main.getConfig().addDefault("button-shuffle-disabled", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTU1MTE1M2ExNTE5MzU3YjYyNDFhYjFkZGNhZTgzMWRmZjA4MDA3OWMwYjI5NjA3OTdjNzAyZGQ5MjI2NjgzNSJ9fX0==");
        main.getConfig().addDefault("button-stop", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjBkMzI3NTJkYzRiNWIxMjgyOTE4NmJjZjlkM2RjNDhiYmYxN2U1ODhhZGZhNDRiODkyNTFkYzVhYjAyY2JkZiJ9fX0=");
        main.getConfig().addDefault("button-radius-plus", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzIzMzJiNzcwYTQ4NzQ2OTg4NjI4NTVkYTViM2ZlNDdmMTlhYjI5MWRmNzY2YjYwODNiNWY5YTBjM2M2ODQ3ZSJ9fX0=");
        main.getConfig().addDefault("button-radius-minus", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzJjYmRjOWQ0YzU5MGVhYzI4NWE0NTQ0ZjJiMWUwNjhiZDI3ZmQ1MjE3M2FjOGQ3Njc5MDEzODIzY2JhYjk1YSJ9fX0=");
    }
}
