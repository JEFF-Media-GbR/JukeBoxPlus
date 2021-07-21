package de.jeff_media.JukeBoxPlus;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class CustomSong {

    private static final Main main = Main.getInstance();
    private static YamlConfiguration customSongs;

    public final int duration;
    public final String name, sound;

    public static void init() {
        customSongs = YamlConfiguration.loadConfiguration(new File(main.getDataFolder()+File.separator, "custom-discs.yml"));
    }

    private CustomSong(String name, int duration, String sound) {
        this.name = name;
        this.duration = duration;
        this.sound = sound;
    }

    public static @Nullable CustomSong get(ItemStack itemStack) {
        System.out.println("Checking whether "+itemStack+" is a custom song");
        if(customSongs.isConfigurationSection(itemStack.getType().name())) {
            System.out.println("in config");
            if(itemStack.hasItemMeta()) {
                System.out.println("has meta");
                if(itemStack.getItemMeta().hasCustomModelData()) {
                    System.out.println("has model data");
                    if(customSongs.isConfigurationSection(itemStack.getType().name()+"."+itemStack.getItemMeta().getCustomModelData())) {
                        System.out.println("is config section");
                        ConfigurationSection section = customSongs.getConfigurationSection(itemStack.getType().name()+"." + itemStack.getItemMeta().getCustomModelData());
                        String name = section.getString("name");
                        int duration = section.getInt("duration");
                        String sound = section.getString("sound");
                        System.out.println("is song " + name);
                        return new CustomSong(name, duration, sound);
                    }
                }
            }
        }
        return null;
    }

}
