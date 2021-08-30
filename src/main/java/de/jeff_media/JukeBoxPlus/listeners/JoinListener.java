package de.jeff_media.JukeBoxPlus.listeners;

import de.jeff_media.JukeBoxPlus.Config;
import de.jeff_media.JukeBoxPlus.JukeboxData;
import de.jeff_media.JukeBoxPlus.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Map;

public class JoinListener implements Listener {

    private static final Main main = Main.getInstance();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        playToNewPlayer(player);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        playToNewPlayer(player);
    }

    private void playToNewPlayer(Player player) {
        if(!main.getConfig().getBoolean(Config.RESTART_SONG_TO_NEW_PLAYERS)) return;
        Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
            for (Map.Entry<Block, JukeboxData> entry : main.getJukeboxes().entrySet()) {
                JukeboxData jd = entry.getValue();
                if(jd.endTime <= 0) continue;
                Block block = entry.getKey();

                if (!block.getWorld().equals(player.getWorld())) continue;
                jd.play(block.getLocation(), player);
            }
        }, 5L);
    }
}
