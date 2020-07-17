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
        jb.setRecord(new ItemStack(r));
        jb.update();
    }

    void stopJukebox(Jukebox jb) {
        jb.stopPlaying();
        jb.setRecord(null);
        jb.update();
    }
}
