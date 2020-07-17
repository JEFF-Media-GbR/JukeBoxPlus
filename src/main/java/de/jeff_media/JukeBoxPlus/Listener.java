package de.jeff_media.JukeBoxPlus;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Jukebox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Listener implements org.bukkit.event.Listener {
    private final Main main;

    public Listener(Main main) {
        this.main=main;
    }

    @EventHandler
    public void onJukeboxInteract(PlayerInteractEvent e) {
        if(e.getAction()!=Action.RIGHT_CLICK_BLOCK) return;
        if(!(e.getClickedBlock().getState() instanceof Jukebox)) return;
        e.setCancelled(true);

        System.out.println("GUI");

    }

}
