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
import org.bukkit.event.inventory.InventoryClickEvent;
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
    public void onJukeboxGUI(InventoryClickEvent e) {
        if(!(e.getClickedInventory()!=null
                && e.getClickedInventory().getHolder()!= null
                && e.getClickedInventory().getHolder() instanceof JukeboxGUI)) {
            return;
        }

        JukeboxData jd = ((JukeboxGUI)e.getClickedInventory().getHolder()).jd;
        Jukebox jb = ((JukeboxGUI)e.getClickedInventory().getHolder()).jb;

        e.setCancelled(true);

        // Contains the clicked item
        ItemStack clicked = e.getCurrentItem();
        if(clicked == null) {
            return;
        }

        if(JukeboxUtils.isRecord(clicked)) {
            main.jukeboxUtils.startJukebox(jb,clicked.getType());
        }


        System.out.println((clicked.getType().name()));

    }


    @EventHandler
    public void onJukeboxInteract(PlayerInteractEvent e) {
        if(e.getAction()!=Action.RIGHT_CLICK_BLOCK) return;
        if(!(e.getClickedBlock().getState() instanceof Jukebox)) return;
        if(e.getHand()!=EquipmentSlot.HAND) return;
        e.setCancelled(true);
        Player p = e.getPlayer();

        /*if(e.getItem()==null || e.getItem().getType()==Material.SAND) {
            main.jukeboxUtils.getJukebox(e.getClickedBlock()).records.forEach((m) -> {
                p.sendMessage(m.name());
            });
            return;
        }*/


        if(JukeboxUtils.isRecord(e.getItem())) {
            if(main.jukeboxUtils.getJukebox(e.getClickedBlock()).add(e.getItem())) {
                e.getItem().setAmount(e.getItem().getAmount()-1);
                return;
            }
        }
        JukeboxGUI gui = new JukeboxGUI((Jukebox) e.getClickedBlock().getState(),main.jukeboxes.get(e.getClickedBlock()));
        gui.open(p);

    }

}
