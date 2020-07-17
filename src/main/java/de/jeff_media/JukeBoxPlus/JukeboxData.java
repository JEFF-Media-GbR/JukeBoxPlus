package de.jeff_media.JukeBoxPlus;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class JukeboxData {

    HashMap<Material,Boolean> records = new HashMap<>();

    JukeboxData() {
        for(Material mat:Material.values()) {
            if(mat.name().startsWith("MUSIC_DISC_")) {
                records.put(mat,false);
            }
        }
    }

    boolean save(ItemStack is) {
        if(records.containsKey(is.getType())) return false;
        if(records.get(is.getType())) return false;
        records.put(is.getType(),true);
        System.out.println("Saved disc "+is.getType().name());
        return true;
    }



}
