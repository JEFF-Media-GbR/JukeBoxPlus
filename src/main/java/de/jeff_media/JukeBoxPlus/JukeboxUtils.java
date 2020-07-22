package de.jeff_media.JukeBoxPlus;

import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;


public class JukeboxUtils {

    Main main;

    JukeboxUtils(Main main) {
        this.main = main;
    }

    @Nullable
    static Jukebox getJukebox(Block block) {
        return block.getState() instanceof Jukebox ? ((Jukebox) block.getState()) : null;
    }

    static boolean isRecord(ItemStack is) {
        if (is == null) return false;
        return is.getType().name().startsWith("MUSIC_DISC_");
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
}
