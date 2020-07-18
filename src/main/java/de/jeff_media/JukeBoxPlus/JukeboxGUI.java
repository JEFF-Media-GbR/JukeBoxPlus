package de.jeff_media.JukeBoxPlus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Jukebox;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class JukeboxGUI implements InventoryHolder {

    Jukebox jb;
    JukeboxData jd;
    Inventory inv;

    JukeboxGUI(Jukebox jb, JukeboxData jd) {
        this.jb=jb;
        this.jd=jd;
        this.inv=Bukkit.createInventory(this,54,"§6Jukebox");
        for(Material record : jd.records) {
            inv.addItem(new ItemStack(record));
        }
    }

    void addFrame() {
/*
GUI should look likes this:
XXXXXXXXX
XmmmmmmmX
XmmmmmmmX
XXXXXXXXX
XoptionsX
XXXXXXXXX
Where X is border, m is stored music discs, and options includes:
- Stop play
- Enable loop
- open playlist
- toggle global/only for player
*/
    }

    void open(Player p) {
        p.openInventory(inv);
        System.out.println("OPENED");
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
