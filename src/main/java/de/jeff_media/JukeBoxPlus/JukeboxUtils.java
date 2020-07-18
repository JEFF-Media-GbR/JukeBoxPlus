package de.jeff_media.JukeBoxPlus;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Jukebox;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;


public class JukeboxUtils {

    Main main;

    JukeboxUtils(Main main) {
        this.main=main;
    }

    void startJukebox(Jukebox jb, Material r) {
        stopJukebox(jb);
        jb.setRecord(new ItemStack(r));
        jb.update();
    }

    void stopJukebox(Jukebox jb) {
        jb.setRecord(null);
        jb.update();
    }

    static boolean isRecord(ItemStack is) {
        if(is==null) return false;
        return is.getType().name().startsWith("MUSIC_DISC_");
    }

    JukeboxData getJukebox(Block block) {
        if(main.jukeboxes.containsKey(block)) {
            System.out.println("This JB already is loaded");
            return main.jukeboxes.get(block);
        }

        // TODO: When Jukebox contains record on creation, add it to the record list
        System.out.println("This JB was new created");
        JukeboxData newJukebox = new JukeboxData(block);
        main.jukeboxes.put(block,newJukebox);

        return newJukebox;
    }
}
