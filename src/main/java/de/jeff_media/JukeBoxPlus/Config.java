package de.jeff_media.JukeBoxPlus;

public class Config {

    Main main;

    Config(Main main) {
        this.main=main;

        main.getConfig().addDefault("ticks-per-bossbar-message",60);
    }
}
