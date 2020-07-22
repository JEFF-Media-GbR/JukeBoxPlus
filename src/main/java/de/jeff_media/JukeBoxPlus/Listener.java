package de.jeff_media.JukeBoxPlus;

import org.bukkit.Material;
import org.bukkit.block.Jukebox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class Listener implements org.bukkit.event.Listener {
    private final Main main;

    public Listener(Main main) {
        this.main = main;
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJukeboxGUI(InventoryMoveItemEvent e) {
        main.debug("InventoryMoveItemEvent");
        if (e.getDestination() == null) return;
        if (e.getDestination().getHolder() == null) return;
        if (!(e.getDestination().getHolder() instanceof JukeboxGUI)) return;
        e.setCancelled(true);

        // JukeboxGUI gui = (JukeboxGUI) e.getDestination().getHolder();


    }

    @EventHandler
    public void onJukeboxGUI(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player) e.getWhoClicked();
        if (!(e.getClickedInventory() != null
                && e.getClickedInventory().getHolder() != null
                && e.getClickedInventory().getHolder() instanceof JukeboxGUI)) {
            return;
        }

        JukeboxGUI gui = (JukeboxGUI) e.getClickedInventory().getHolder();

        JukeboxData jd = ((JukeboxGUI) e.getClickedInventory().getHolder()).jd;
        Jukebox jb = ((JukeboxGUI) e.getClickedInventory().getHolder()).jb;

        e.setCancelled(true);

        //main.debug(e.getCursor());
        if (JukeboxUtils.isRecord(e.getCursor())) {
            if (!gui.jd.records.contains(e.getCursor().getType())) {
                gui.jd.records.add(e.getCursor().getType());
                e.getCursor().setAmount(e.getCursor().getAmount() - 1);
                gui.open(p);
                return;
            }
        }

        // Contains the clicked item
        ItemStack clicked = e.getCurrentItem();
        if (clicked == null) {
            return;
        }
        int slot = e.getSlot();

        if (JukeboxUtils.isRecord(clicked)) {

            boolean remove = e.isRightClick();

            if (remove) {
                if (jd.record == clicked.getType()) {
                    jd.stopJukebox(jb);
                }
                jd.records.remove(clicked.getType());
                //gui.getInventory().setItem(e.getSlot(),null);
                p.getInventory().addItem(new ItemStack(clicked.getType()));
            } else {
                jd.startJukebox(jb, clicked.getType());
            }


        } else {

            switch (slot) {
                case 4 * 9 + 1:
                    main.debug("Toggle Loop");
                    if (jd.shuffle) jd.toggleShuffle(main);
                    jd.toggleLoop(main);
                    break;
                case 4 * 9 + 7:
                    main.debug("Stop");
                    if (jd.loop) jd.toggleLoop(main);
                    jd.stopJukebox(jb);
                    break;
                case 4 * 9 + 2:
                    main.debug("Toggle Shuffle");
                    if (jd.loop) jd.toggleLoop(main);
                    jd.toggleShuffle(main);
                    break;
                default:
                    break;
            }
        }

        //JukeboxGUI gui = new JukeboxGUI(jb,jd,main);


        gui.open((Player) e.getWhoClicked());

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
            if (main.jukeboxUtils.getJukeboxData(e.getClickedBlock()).add(e.getItem())) {
                e.getItem().setAmount(e.getItem().getAmount() - 1);
                return;
            }
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
