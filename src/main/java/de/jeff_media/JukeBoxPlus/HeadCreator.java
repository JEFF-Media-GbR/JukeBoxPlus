package de.jeff_media.JukeBoxPlus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

public class HeadCreator {

    static ItemStack getHead(String base64) {
        try {
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
            PlayerTextures textures = profile.getTextures();
            textures.setSkin(getUrlFromBase64(base64));
            profile.setTextures(textures);
            meta.setOwnerProfile(profile);
            head.setItemMeta(meta);
            return head;
        }catch (Exception e) {
            e.printStackTrace();
            ItemStack item = new ItemStack(Material.PLAYER_HEAD);
            return item;
        }

    }

    private static URL getUrlFromBase64(String base64) throws MalformedURLException {
        try {
            String decoded = new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);
            String prefix = "{\"textures\":{\"SKIN\":{\"url\":\"";
            String suffix = "\"}}}";
            return new URL(decoded.substring(prefix.length(), decoded.length() - suffix.length()));
        } catch (Throwable t) {
            throw new MalformedURLException("Invalid base64 skin texture");
        }
    }
}
