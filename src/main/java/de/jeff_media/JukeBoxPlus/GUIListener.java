package de.jeff_media.JukeBoxPlus;

import org.bukkit.block.Jukebox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;

public class GUIListener implements Listener {

    final Main main;

    GUIListener(Main main) {
        this.main=main;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJukeboxGUI(InventoryMoveItemEvent e) {
        main.debug("InventoryMoveItemEvent");
        if (e.getDestination() == null) return;
        if (e.getDestination().getHolder() == null) return;
        if (!(e.getDestination().getHolder() instanceof JukeboxGUI)) return;
        e.setCancelled(true);

    }

    @EventHandler
    public void onJukeboxGUI(InventoryClickEvent e) {
        EventDebugger.debug(e,main);

        if (!(e.getWhoClicked() instanceof Player)) return;

        // Don't do anything if none of the inventories is a JukeboxGUI
        if(!JukeboxGUI.isJukeboxGUI(e.getClickedInventory())
            && !JukeboxGUI.isJukeboxGUI(e.getInventory())) return;

        // Cancels all events where a JukeboxGUI is involved
        e.setCancelled(true);

        // Only do stuff if the clicked inventory is a JukeboxGUI
        if(!JukeboxGUI.isJukeboxGUI(e.getClickedInventory())) return;

        Player p = (Player) e.getWhoClicked();
        JukeboxGUI gui = (JukeboxGUI) e.getClickedInventory().getHolder();
        JukeboxData jd = ((JukeboxGUI) e.getClickedInventory().getHolder()).jd;
        Jukebox jb = ((JukeboxGUI) e.getClickedInventory().getHolder()).jb;



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
                    // TODO: Only reset if played record is loop or standalone, but not when shuffle
                    jd.stopJukebox(jb,true);
                }
                jd.records.remove(clicked.getType());
                //gui.getInventory().setItem(e.getSlot(),null);
                p.getInventory().addItem(new ItemStack(clicked.getType()));
            } else {
                jd.startJukebox(jb, clicked.getType(),p);
            }


        } else {

            switch (slot) {
                case 4 * 9 + 1:
                    main.debug("Toggle Loop");
                    if (jd.shuffle) jd.toggleShuffle(main,p);
                    jd.toggleLoop(main,p);
                    break;
                case 4 * 9 + 7:
                    main.debug("Stop");
                    if (jd.loop) jd.toggleLoop(main,null);
                    jd.stopJukebox(jb,true);
                    break;
                case 4 * 9 + 2:
                    main.debug("Toggle Shuffle");
                    if (jd.loop) jd.toggleLoop(main,null);
                    jd.toggleShuffle(main,null);
                    break;
                case 4*9 + 4:
                    main.debug("Radius minus");
                    jd.radiusMinus();
                    break;
                case 4*9 + 5:
                    main.debug("Radius plus");
                    jd.radiusPlus();
                    break;
                default:
                    break;
            }
        }

        gui.open((Player) e.getWhoClicked());

    }
}
