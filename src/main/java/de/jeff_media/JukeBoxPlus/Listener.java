package de.jeff_media.JukeBoxPlus;

import org.bukkit.Material;
import org.bukkit.block.Jukebox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class Listener implements org.bukkit.event.Listener {
    private final Main main;

    public Listener(Main main) {
        this.main = main;
    }





    @EventHandler
    public void onJukeboxInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (!(e.getClickedBlock().getState() instanceof Jukebox)) return;
        if (e.getHand() != EquipmentSlot.HAND) return;
        e.setCancelled(true);
        Player p = e.getPlayer();

        /*if(e.getItem()==null || e.getItem().getType()==Material.SAND) {
            main.jukeboxUtils.getJukebox(e.getClickedBlock()).records.forEach((m) -> {
                p.sendMessage(m.name());
            });
            return;
        }*/


        if (JukeboxUtils.isRecord(e.getItem())) {
            if (main.jukeboxUtils.getJukeboxData(e.getClickedBlock()).addRecord(e.getItem(),p)) {
                e.getItem().setAmount(e.getItem().getAmount() - 1);
            }
            return;
        }

        JukeboxGUI gui = new JukeboxGUI((Jukebox) e.getClickedBlock().getState(), main.jukeboxUtils.getJukeboxData(e.getClickedBlock()), main);
        gui.open(p);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBreak(BlockBreakEvent e) {
        if(e.isCancelled()) return;

        if(e.getBlock().getType() != Material.JUKEBOX) return;
        if(!(e.getBlock().getState() instanceof Jukebox)) return;

        Jukebox jb = (Jukebox) e.getBlock().getState();
        main.jukeboxes.get(e.getBlock()).destroy(e.getBlock());
        main.jukeboxes.remove(e.getBlock());

    }

}
