package de.jeff_media.JukeBoxPlus;

import io.th0rgal.protectionlib.ProtectionLib;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class ProtectionLibWrapper {

    public static boolean canUse(Player player, Block block) {
        try {
            boolean result = ProtectionLib.canUse(player, block.getLocation()) && ProtectionLib.canInteract(player, block.getLocation());
            System.out.println(result);
            return result;
        } catch (Throwable ignored) {
            ignored.printStackTrace();
            return true;
        }
    }

}
