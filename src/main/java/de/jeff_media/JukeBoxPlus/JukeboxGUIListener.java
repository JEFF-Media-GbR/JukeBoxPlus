package de.jeff_media.JukeBoxPlus;

import com.jeff_media.jefflib.PDCUtils;
import org.bukkit.Material;
import org.bukkit.block.Jukebox;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Iterator;

public class JukeboxGUIListener implements Listener {

    final Main main;

    JukeboxGUIListener(Main main) {
        this.main = main;
    }

    /*@EventHandler(priority = EventPriority.HIGHEST)
    public void onJukeboxGUI(InventoryMoveItemEvent e) {
        main.debug("InventoryMoveItemEvent");
        if (e.getDestination() == null) return;
        if (e.getDestination().getHolder() == null) return;
        if (!(e.getDestination().getHolder() instanceof JukeboxGUI)) return;
        e.setCancelled(true);

    }*/

    @EventHandler
    public void onJukeboxGUI(InventoryClickEvent e) {
        EventDebugger.debug(e, main);

        if (!(e.getWhoClicked() instanceof Player)) return;

        // Don't do anything if none of the inventories is a JukeboxGUI
        if (!JukeboxGUI.isJukeboxGUI(e.getClickedInventory())
                && !JukeboxGUI.isJukeboxGUI(e.getInventory())) {
            main.debug("None of those inventories is a JukeboxGUI");
            return;
        }

        // Cancels all events where a JukeboxGUI is involved
        e.setCancelled(true);

        Player p = (Player) e.getWhoClicked();

        // Only do stuff if the clicked inventory is a JukeboxGUI or when adding discs
        if (!JukeboxGUI.isJukeboxGUI(e.getClickedInventory())) {
            if (JukeboxUtils.isRecord(e.getCurrentItem())) {
                JukeboxGUI gui = (JukeboxGUI) e.getView().getTopInventory().getHolder();
                if (gui.jd.addRecord(e.getCurrentItem(), p)) {
                    e.getCurrentItem().setAmount(e.getCurrentItem().getAmount() - 1);
                    gui.update();
                    return;
                }
            }
            return;
        }


        JukeboxGUI gui = (JukeboxGUI) e.getClickedInventory().getHolder();
        JukeboxData jd = ((JukeboxGUI) e.getClickedInventory().getHolder()).jd;
        Jukebox jb = ((JukeboxGUI) e.getClickedInventory().getHolder()).jb;


        // Contains the clicked item
        ItemStack clicked = e.getCurrentItem();
        if (clicked == null) {
            return;
        }
        int slot = e.getSlot();

        if (JukeboxUtils.isRecord(clicked)) {

            boolean remove = e.isRightClick();

            if (remove) {
                clicked.removeEnchantment(Enchantment.DURABILITY);
                //System.out.println(1);
                //System.out.println(clicked);
                if (jd.record != null && RecordUtils.itemStackEquals(jd.record, clicked)) {
                    //System.out.println(2);
                    // TODO: Avoid removed discs from being able to be looped after being removed
                    if (jd.shuffle) {
                        //System.out.println(3);
                        jd.stopJukebox(jb, false);
                        jd.randomRecord();
                        jd.startJukebox();
                    } else {
                        //System.out.println(4);
                        jd.stopJukebox(jb, true);
                    }
                }
                //System.out.println(5);
                ItemStack clone = clicked.clone();
                clone.removeEnchantment(Enchantment.DURABILITY);
                Iterator<ItemStack> iterator = jd.records.iterator();
                while (iterator.hasNext()) {
                    //System.out.println(6);
                    ItemStack current = iterator.next();
                    if (!RecordUtils.itemStackEquals(current, clicked)) continue;
                    //System.out.println("Removing disc " + current);
                    if (clicked == null || current == null) continue;
                    p.getInventory().addItem(clone);
                    iterator.remove();
                    jd.saveAsync();
                    break;
                }
                /*if(jd.records.contains(clicked)) {

                }*/
                gui.update();
            } else {
                jd.startJukebox(jb, clicked, p);
                //System.out.println(8);
            }


        } else {
            //System.out.println(9);

            switch (slot) {
                case ButtonSlot.LOOP:
                    if (p.hasPermission(Permissions.ALLOW_LOOP)) {
                        //System.out.println(1);
                        main.debug("Toggle Loop");
                        if (jd.shuffle) {
                            //System.out.println(2);
                            jd.toggleShuffle(p);
                        }
                        //System.out.println(3);
                        jd.toggleLoop(main, p);
                    }
                    break;
                case ButtonSlot.SHUFFLE:
                    if (p.hasPermission(Permissions.ALLOW_SHUFFLE)) {
                        main.debug("Toggle Shuffle");
                        if (jd.loop) {
                            jd.toggleLoop(main, null);
                            //jd.stopJukebox(jb,false);
                        }
                        jd.toggleShuffle(null);
                    }
                    break;
                case ButtonSlot.AUTOSTART:
                    if (p.hasPermission(Permissions.ALLOW_AUTOSTART)) {
                        main.debug("Toggle Autostart");
                        jd.autostart = !jd.autostart;
                    }
                    break;
                case ButtonSlot.RADIUS_MINUS:
                    if (p.hasPermission(Permissions.ALLOW_CHANGE_RADIUS)) {
                        main.debug("Radius minus");
                        jd.radiusMinus();
                    }
                    break;
                case ButtonSlot.RADIUS_PLUS:
                    if (p.hasPermission(Permissions.ALLOW_CHANGE_RADIUS)) {
                        main.debug("Radius plus");
                        jd.radiusPlus();
                    }
                    break;
                case ButtonSlot.STOP:
                    main.debug("Stop");
                    if (jd.loop) jd.toggleLoop(main, null);
                    jd.stopJukebox(jb, true);
                    break;

                case ButtonSlot.NEXT_PAGE:
                    main.debug("Next page");
                    if(clicked != null && clicked.getType() == Material.PLAYER_HEAD && PDCUtils.has(clicked, PDC.CURRENT_PAGE, PersistentDataType.INTEGER)) {
                        jd.currentPage++;
                    } else {
                        main.debug("Next page -> no, empty");
                    }
                    break;

                case ButtonSlot.PREVIOUS_PAGE:
                    main.debug("Previous page");
                    if(clicked != null && clicked.getType() == Material.PLAYER_HEAD && PDCUtils.has(clicked, PDC.CURRENT_PAGE, PersistentDataType.INTEGER)) {
                        jd.currentPage--;
                    }
                    break;

                default:
                    break;
            }
        }

        gui.open((Player) e.getWhoClicked());
        for (HumanEntity entity : gui.getInventory().getViewers()) {
            if (entity instanceof Player) {
                Player viewer = (Player) entity;
                gui.open(viewer);
            }
        }

    }
}
