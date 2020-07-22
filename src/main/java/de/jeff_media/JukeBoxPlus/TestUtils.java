package de.jeff_media.JukeBoxPlus;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class TestUtils {

    static void spawnPillar(Location loc) {
        Block block = loc.getBlock();

        for(int i = 0; i < 30; i++) {
            block.setType(Material.GOLD_BLOCK);
            block = block.getRelative(BlockFace.UP);
        }

    }
}
