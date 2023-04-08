package de.jeff_media.JukeBoxPlus;

import com.jeff_media.jefflib.internal.glowenchantment.GlowEnchantmentFactory;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collection;

public class Utils {

    final Main main;

    Utils(Main main) {
        this.main = main;
        registerGlow();
    }

    static Collection<Entity> getNearbyPlayers(Block block, int radius) {
        return block.getWorld().getNearbyEntities(block.getLocation(), radius, radius, radius, entity -> {
            if (entity instanceof Player) {
                return entity.getLocation().distance(block.getLocation()) <= radius;
            }
            return false;
        });
    }

    static File block2file(Block block, Main main) {
        String uuid = block.getWorld().getUID().toString();
        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();
        String fileName = String.format("%s_%d_%d_%d.yml", uuid, x, y, z);
        return new File(main.getDataFolder() + File.separator + "jukeboxes" + File.separator + fileName);
    }

    public void registerGlow() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        } catch (Exception ignored) {

        }
        try {
            NamespacedKey key = new NamespacedKey(main, main.getDescription().getName());

            Enchantment glow = GlowEnchantmentFactory.getInstance();
            Enchantment.registerEnchantment(glow);
        } catch (Exception ignored) {

        }
    }

    void updateInventoryViews(String reason) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(p.getOpenInventory()!=null
                    && p.getOpenInventory().getTopInventory()!=null
                    && p.getOpenInventory().getTopInventory().getHolder()!=null
                    && p.getOpenInventory().getTopInventory().getHolder() instanceof JukeboxGUI) {
                JukeboxGUI gui = (JukeboxGUI) p.getOpenInventory().getTopInventory().getHolder();
                gui.open(p);
            }
        }

        main.debug("Update Inventory: "+reason);

    }

    static void renameFileInPluginDir(Main plugin,String oldName, String newName) {
        File oldFile = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + oldName);
        File newFile = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + newName);
        oldFile.getAbsoluteFile().renameTo(newFile.getAbsoluteFile());
    }


}
