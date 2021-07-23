package de.jeff_media.JukeBoxPlus;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.File;

public class CustomSong {

    private static final Main main = Main.getInstance();
    private static YamlConfiguration customSongs;

    public final int duration;
    public final String name, sound;

    public static void init() {
        customSongs = YamlConfiguration.loadConfiguration(new File(main.getDataFolder()+File.separator, "custom-discs.yml"));
        if(customSongs==null) customSongs = new YamlConfiguration();
        if(customSongs==null) {
            throw new IllegalStateException("WTF is happening");
        }
    }

    private CustomSong(String name, int duration, String sound) {
        this.name = name;
        this.duration = duration;
        this.sound = sound;
    }

    public static @Nullable CustomSong get(ItemStack itemStack) {
        if(customSongs==null) {
            throw new IllegalStateException("WTF is happening");
        }
        if(customSongs.isConfigurationSection(itemStack.getType().name())) {
            if(itemStack.hasItemMeta()) {
                if(itemStack.getItemMeta().hasCustomModelData()) {
                    if(customSongs.isConfigurationSection(itemStack.getType().name()+"."+itemStack.getItemMeta().getCustomModelData())) {
                        ConfigurationSection section = customSongs.getConfigurationSection(itemStack.getType().name()+"." + itemStack.getItemMeta().getCustomModelData());
                        String name = ChatColor.translateAlternateColorCodes('&',section.getString("name"));
                        int duration = section.getInt("duration");
                        String sound = section.getString("sound");
                        return new CustomSong(name, duration, sound);
                    }
                }
            }
        }
        return null;
    }

}
