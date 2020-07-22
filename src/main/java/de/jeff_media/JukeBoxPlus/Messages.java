package de.jeff_media.JukeBoxPlus;

public class Messages {

    final String ADDED_DISK,
            ALREADY_ADDED;
    Main main;

    Messages(Main main) {
        this.main = main;

        ADDED_DISK = "Added %s to this Jukebox";
        ALREADY_ADDED = "This Jukebox already contains %s.";
    }
}
