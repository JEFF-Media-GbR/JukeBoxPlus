package de.jeff_media.JukeBoxPlus;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandReload implements CommandExecutor {

    private final Main main;

    CommandReload(Main main) {
        this.main=main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(!commandSender.hasPermission(Permissions.ALLOW_RELOAD)) {
            commandSender.sendMessage(command.getPermissionMessage());
            return true;
        }

        main.onEnable(true);
        commandSender.sendMessage(ChatColor.GREEN+"JukeBoxPlus has been reloaded.");
        //commandSender.sendMessage(ChatColor.GRAY+"Restarted all looped Jukeboxes.");

        return true;
    }
}
