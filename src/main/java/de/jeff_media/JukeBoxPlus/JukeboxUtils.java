package de.jeff_media.JukeBoxPlus;

import de.jeff_media.daddy.Chicken;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;


public class JukeboxUtils {

    final Main main;

    JukeboxUtils(Main main) {
        this.main = main;
        Chicken.wing(main);
    }

    @Nullable
    static Jukebox getJukebox(Block block) {
        return block.getState() instanceof Jukebox ? ((Jukebox) block.getState()) : null;
    }

    static boolean isRecord(ItemStack is) {
        if (is == null) return false;
        if(is.getType().name().startsWith("MUSIC_DISC_")) return true;
        if(!is.getItemMeta().hasCustomModelData()) return false;
        int modelData = is.getItemMeta().getCustomModelData();
        return CustomSong.get(is) != null;
    }

    JukeboxData getJukeboxData(Block block) {
        if (main.jukeboxes.containsKey(block)) {
            main.debug("This JB already is loaded");
            return main.jukeboxes.get(block);
        }

        // TODO: When Jukebox contains record on creation, add it to the record list
        main.debug("This JB was new created");
        JukeboxData newJukebox = new JukeboxData(block, main);
        main.jukeboxes.put(block, newJukebox);

        return newJukebox;
    }

    static ArrayList<Location> getDistandSpeakers(Location loc) {
        int distance = 65;
        ArrayList<Location> locs = new ArrayList<>();
        for(int x = -1; x<=1; x++) {
            for(int z = -1; z<=1; z++) {
                if(x==0&&z==0) continue;
                locs.add(new Location(loc.getWorld(),loc.getBlockX()+x*distance,loc.getBlockY(),loc.getBlockZ()+z*distance));
            }
        }
        return locs;
    }
}
