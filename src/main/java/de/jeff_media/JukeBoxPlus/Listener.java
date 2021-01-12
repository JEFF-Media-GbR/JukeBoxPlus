package de.jeff_media.JukeBoxPlus;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Jukebox;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.block.Block;

public class Listener implements org.bukkit.event.Listener {
    private final Main main;

    public Listener(Main main) {
        this.main = main;
    }





    @EventHandler
    public void onJukeboxInteract(PlayerInteractEvent e) {

        if(e.useInteractedBlock() == Event.Result.DENY) {
            return;
        }

        if (e.getAction() != Action.RIGHT_CLICK_BLOCK
                && e.getAction() != Action.LEFT_CLICK_BLOCK) return;
        if (!(e.getClickedBlock().getState() instanceof Jukebox)) return;

        if(!e.getPlayer().hasPermission(Permissions.ALLOW_USE)) {
            if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                e.setCancelled(true);
            }
            return;
        }

        if (e.getHand() != EquipmentSlot.HAND) return;
        Player p = e.getPlayer();
        Jukebox jb = (Jukebox) e.getClickedBlock().getState();

        if(e.getAction() == Action.LEFT_CLICK_BLOCK ) {
            main.debug("Left-Click Jukebox");
            if(p.getGameMode()== GameMode.CREATIVE) {
                main.debug("Ignoring Left-Click in Creative Mode");
                return;
            }

            // Shuffle: Next random song
            // Loop: Restart song
            // Normal: Next song
            Block block = e.getClickedBlock();
            if (!main.jukeboxes.containsKey(block)) {
                main.debug("This Jukebox is not registered yet");
                return;
            }
            JukeboxData jd = main.jukeboxes.get(block);
            if (!p.isSneaking()) {

                // Not sneaking, start
                jd.stopJukebox(jb, false);

                if (jd.loop) {
                    jd.startJukebox(jb, p);
                    return;
                }
                if (jd.shuffle) {
                    jd.randomRecord();
                    jd.startJukebox(jb, p);
                    return;
                }
                jd.nextRecord();
                jd.startJukebox(jb, p);
                return;
            } else {
                // Sneaking, stop

                jd.stopJukebox(jb,false);
                //if(jd.loop) jd.toggleLoop(main,null);
                //if(jd.shuffle) jd.toggleShuffle(main,null);
                return;

            }
        }

        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        e.setCancelled(true);

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
        main.debug("BlockBreak");
        if(e.isCancelled()) {
            main.debug("E: Cancelled");
            return;
        }

        if(e.getBlock().getType() != Material.JUKEBOX) {
            main.debug("E: Material");
            return;
        }
        if(!(e.getBlock().getState() instanceof Jukebox)) {
            main.debug("E: State");
            return;
        }

        Jukebox jb = (Jukebox) e.getBlock().getState();
        JukeboxData jd = main.jukeboxes.get(e.getBlock());
        jd.stopJukebox(jb,true);
        jd.destroy(e.getBlock());
        main.jukeboxes.remove(e.getBlock());

        /*Bukkit.getScheduler().scheduleSyncDelayedTask(main,() -> {
            jd.stopJukebox(jb,true);
        },1l);*/

    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        for(Block block : event.blockList()) {
            if(main.jukeboxes.containsKey(block)) {
                event.blockList().remove(block);
            }
        }
    }

    @EventHandler
    public void onExplode(BlockExplodeEvent event) {
        for(Block block : event.blockList()) {
            if(main.jukeboxes.containsKey(block)) {
                event.blockList().remove(block);
            }
        }
    }

}
