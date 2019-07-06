package eu.kennytv.maintenance.addon.spigot.command;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class MaintenanceAddonCommand implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String s, final String[] args) {
        if (!(sender instanceof Player)) return true;
        final String message = PlaceholderAPI.setPlaceholders((Player) sender, "Global status: %maintenance_status% §r- Status of Server1: %maintenance_server_server1%");
        sender.sendMessage(message);
        return true;
    }
}
