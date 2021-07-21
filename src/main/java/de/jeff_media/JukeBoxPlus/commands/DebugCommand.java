package de.jeff_media.JukeBoxPlus.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import de.jeff_media.JukeBoxPlus.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@CommandAlias("jukebox|jukeboxplus")
public class DebugCommand extends BaseCommand {

    private static final Main main = Main.getInstance();

    @Subcommand("modeldata")
    @CommandPermission("jukebox.modeldata")
    public static void addModelData(Player player, String[] args) {



        ItemStack item = player.getInventory().getItemInMainHand();
        if(item != null && !item.getType().isAir()) {
            ItemMeta meta = item.getItemMeta();
            try {
                int data = Integer.parseInt(args[0]);
                meta.setCustomModelData(data);
                player.getInventory().getItemInMainHand().setItemMeta(meta);
                player.sendMessage("§6Set CustomModelData for " + player.getInventory().getItemInMainHand().getType().name() + " to " + data);
            }catch (Exception e) {
                player.sendMessage("§6CustomModelData for this " + player.getInventory().getItemInMainHand().getType().name() + " is " + player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData());
                return;
            }
        } else {
            player.sendMessage("§cYou must hold an item in your main hand.");
        }
    }

    @Subcommand("reload")
    @CommandPermission("jukebox.reload")
    public static void onReload(CommandSender sender, String[] args) {
        main.onEnable(true);
        sender.sendMessage(ChatColor.GREEN+"JukeBoxPlus has been reloaded.");
    }


}
