package de.jeff_media.JukeBoxPlus;

import org.bukkit.block.Block;

import java.io.File;

public class Utils {
    static File block2file(Block block,Main main) {
        String uuid = block.getWorld().getUID().toString();
        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();
        String fileName = String.format("%s_%d_%d_%d");
        return new File(main.getDataFolder()+File.separator+"jukeboxes"+File.separator+fileName);
    }


}
