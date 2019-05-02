package eu.kennytv.maintenance.addon.spigot;

import me.clip.placeholderapi.PlaceholderHook;
import org.bukkit.entity.Player;

public final class MaintenancePlaceholder extends PlaceholderHook {
    private final MaintenanceSpigotAddon plugin;

    public MaintenancePlaceholder(final MaintenanceSpigotAddon plugin) {
        this.plugin = plugin;
    }

    @Override
    public String onPlaceholderRequest(final Player player, final String identifier) {
        if (identifier.equals("status")) {
            return plugin.getMessages().get(plugin.isMaintenance() ? "maintenance-on" : "maintenance-off");
        } else if (identifier.startsWith("server_")) {
            return plugin.getMessages().get(plugin.getMaintenanceServers().contains(identifier.substring(7).toLowerCase()) ? "single-maintenance-on" : "single-maintenance-off");
        }
        return null;
    }
}
