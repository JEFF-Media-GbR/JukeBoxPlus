package de.jeff_media.JukeBoxPlus;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    JukeboxUtils jukeboxUtils;

    public void onEnable() {
        jukeboxUtils = new JukeboxUtils(this);
        this.getServer().getPluginManager().registerEvents(new Listener(this),this);
    }
}
