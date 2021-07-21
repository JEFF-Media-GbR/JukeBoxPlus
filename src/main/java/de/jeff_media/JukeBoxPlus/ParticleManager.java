package de.jeff_media.JukeBoxPlus;

import com.google.common.base.Enums;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.Map;

public class ParticleManager {

    private static final Main main = Main.getInstance();
    private static BukkitRunnable runnable;
    private final Particle particle = Enums.getIfPresent(Particle.class, main.getConfig().getString(Config.PARTICLE_TYPE)).orNull();
    private final double speed = main.getConfig().getDouble(Config.PARTICLE_SPEED);
    private final int count = main.getConfig().getInt(Config.PARTICLE_COUNT);
    private final double offset = main.getConfig().getDouble(Config.PARTICLE_OFFSET);
    private final double height = main.getConfig().getDouble(Config.PARTICLE_HEIGHT);

    public ParticleManager() {
        if (runnable != null) {
            runnable.cancel();
        }
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                long time = new Date().getTime();

                for (Map.Entry<Block, JukeboxData> entry : main.jukeboxes.entrySet()) {
                    Block block = entry.getKey();
                    JukeboxData jd = entry.getValue();

                    if (!block.getChunk().isLoaded()) {
                        continue;
                    }
                    if (jd.endTime > 0) {
                        if (time >= jd.endTime) {
                            continue;
                        }
                        playParticles(jd);
                    }
                }
            }
        };
        runnable.runTaskTimer(main,0,main.getConfig().getInt(Config.PARTICLE_DELAY));
    }

            public void playParticles(JukeboxData jd) {
                if (particle == null) {
                    return;
                }
                Location location = jd.getBlock().getLocation().add(0.5, height, 0.5);
                for (Entity entity : location.getWorld().getNearbyEntities(location, 64, 64, 64, entity -> entity instanceof Player)) {
                    Player player = (Player) entity;
                    player.spawnParticle(particle, location, main.getConfig().getInt(Config.PARTICLE_COUNT), offset, 0, offset, speed);
                }
            }


        }