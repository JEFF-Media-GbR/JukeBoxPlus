package de.jeff_media.JukeBoxPlus;

import com.jeff_media.jefflib.SkullUtils;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class HeadCreator {

    static ItemStack getHead(String base64) {
        try {
            return SkullUtils.getHead(base64);
        }catch (Exception e) {
            e.printStackTrace();
            ItemStack item = new ItemStack(Material.PLAYER_HEAD);
            return item;
        }

    }
}
