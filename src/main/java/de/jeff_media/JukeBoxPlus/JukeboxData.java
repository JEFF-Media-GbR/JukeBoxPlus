package de.jeff_media.JukeBoxPlus;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.HashMap;

public class JukeboxData {

    UUID world;
    int x,y,z;
    ArrayList<Material> records = new ArrayList<>();
    boolean loop = false;
    ArrayList<Material> playlist = new ArrayList<>();

    JukeboxData(UUID world, int x, int y, int z) {
        this.world=world;
        this.x=x;
        this.y=y;
        this.z=z;
    }

    JukeboxData(UUID world, int x, int y, int z, File file) {
        this.world=world;
        this.x=x;
        this.y=y;
        this.z=z;
    }



    void save(File file) {
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.set("records",recordsToStringList());
        try {
            yaml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void loadRecords(File file) {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        for(String s : yaml.getStringList("records")) {
            records.add(Material.getMaterial(s));
        }
    }

    ArrayList<String> recordsToStringList() {
        ArrayList<String> list = new ArrayList<String>();
        for(Material record : records) {
            list.add(record.name());
        }
        return list;
    }

    boolean add(ItemStack is) {
        if(records.contains(is.getType())) return false;
        records.add(is.getType());
        System.out.println("Added disc "+is.getType().name());
        return true;
    }



}
