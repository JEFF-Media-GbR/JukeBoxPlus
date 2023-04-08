package de.jeff_media.JukeBoxPlus;

import com.jeff_media.jefflib.internal.glowenchantment.GlowEnchantmentFactory;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.naming.Name;
import java.util.ArrayList;

public class RecordUtils {

    private static final Main main = Main.getInstance();

    public static boolean itemStackEquals(ItemStack item1, ItemStack item2) {
        //System.out.println("Checking Equals: " + item1 + ", " + item2);
        //if(item1 == null) //System.out.println("Item1 == null");
        //if(item2 == null) //System.out.println("Item2 == null");
        if(item1 == null && item2 != null) {
            //System.out.println("No: 1");
            return false;
        }
        if(item2 == null && item1 != null) {
            //System.out.println("No: 2");
            return false;
        }
        ////System.out.println("Checking whether " + item1 + " equals " + item2);
        if(item1 == null || item2 == null) {
            //System.out.println("One is null!");
            return false;
        }
        item1 = item1.clone();
        item2 = item2.clone();
        item1 = getDiscItem(item1);
        item2 = getDiscItem(item2);
        item1.removeEnchantment(GlowEnchantmentFactory.getInstance());
        item2.removeEnchantment(GlowEnchantmentFactory.getInstance());
        if(item1.equals(item2)) {
            //System.out.println("Yes!");
            return true;
        }
        //System.out.println("No!");
        return false;
    }

    public static ItemStack getDiscItem(ItemStack disc) {
        return disc;
        /*ItemMeta meta = disc.getItemMeta();
        meta.setDisplayName(main.getConfig().getString(Config.DISC_NAME).replaceAll("\\{NAME}",main.songUtils.getName(disc)));
        ArrayList<String> lore = new ArrayList<>();
        for(String line : main.getConfig().getString(Config.DISC_LORE).split("\n")) {
            lore.add(line.replaceAll("\\{DURATION}",main.songUtils.getFormattedDuration(disc))
                    .replaceAll("\\{NAME}",main.songUtils.getName(disc)));
        }
        meta.setLore(lore);
        disc.setItemMeta(meta);
        return disc;*/
    }
}
