package de.jeff_media.JukeBoxPlus;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import javax.naming.Name;

public class RecordUtils {

    public static boolean itemStackEquals(ItemStack item1, ItemStack item2) {
        if(item1 == null) //System.out.println("Item1 == null");
        if(item2 == null) //System.out.println("Item2 == null");
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
        item1.removeEnchantment(new Glow(new NamespacedKey(Main.getInstance(),Main.getInstance().getDescription().getName())));
        item2.removeEnchantment(new Glow(new NamespacedKey(Main.getInstance(),Main.getInstance().getDescription().getName())));
        if(item1.equals(item2)) {
            //System.out.println("Yes!");
            return true;
        }
        //System.out.println("No!");
        return false;
    }
}
