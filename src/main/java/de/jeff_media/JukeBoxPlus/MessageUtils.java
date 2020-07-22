package de.jeff_media.JukeBoxPlus;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageUtils {

    Main main;

    MessageUtils(Main main) {
        this.main=main;
    }

    enum Sound {
        SUCCESS, ERROR
    }

    static void sound(Player p, Sound sound) {
        org.bukkit.Sound s = null;
        float volume = 1.0f;
        switch (sound) {
            case SUCCESS:
                s = org.bukkit.Sound.BLOCK_NOTE_BLOCK_PLING;
                volume = 1.0f;
                break;
            case ERROR:
                s = org.bukkit.Sound.BLOCK_ANVIL_PLACE;
                volume = 0.5f;
        }
        p.playSound(p.getLocation(),s,volume,1.0f);
    }

    void send(String text, boolean success, Player p, boolean sound) {
        if(sound) {
            sound(p,success ? Sound.SUCCESS : Sound.ERROR);
        }
        send(text,success,p);
    }

    void send(String text, boolean success, Player p) {
        send(text,success ? ChatColor.GREEN : ChatColor.RED, success ? BarColor.GREEN : BarColor.RED, p);
    }

    void send(String text, ChatColor textColor, BarColor color, Player p) {
        sendActionbar(text,textColor,p);
        sendBossbar(text,color,textColor,p);
    }

    static void sendActionbar(String text, ChatColor color, Player p) {
        TextComponent component = new TextComponent(text);
        component.setColor(color);
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
    }

    void removeBossbar(BossBar bar) {
        if(bar==null) return;
        bar.setProgress(0);
        bar.setVisible(false);
        bar.removeAll();
    }

    void sendBossbar(String text, BarColor color, ChatColor textColor, Player p) {
        UUID u = p.getUniqueId();
        removeBossbar(main.bossbars.get(p.getUniqueId()));


        final BossBar bar = Bukkit.createBossBar(textColor+text,color, BarStyle.SOLID);
        final int ticksPerMessage = main.getConfig().getInt("ticks-per-bossbar-message");
        final double amountPerTick = 1.0/ticksPerMessage;

        bar.setProgress(1.0);
        bar.addPlayer(p);
        bar.setVisible(true);

        main.bossbars.put(u,bar);

        final AtomicInteger task = new AtomicInteger(-1); // Lambda expressions must be effectively final
        task.set(Bukkit.getScheduler().scheduleSyncRepeatingTask(main,() -> {

            if(bar.getProgress()>amountPerTick) {

                bar.setProgress(bar.getProgress() - amountPerTick);

            } else {

                removeBossbar(bar);
                if(task.get() != -1)
                Bukkit.getScheduler().cancelTask(task.get());
            }
        },1l,1l));
        Bukkit.getScheduler().runTaskLater(main, () -> {
            removeBossbar(bar);
        },ticksPerMessage);
    }
}
