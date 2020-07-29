package eu.kennytv.maintenance.addon.spigot;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

public final class MaintenancePlaceholder extends PlaceholderExpansion {
    private final MaintenanceSpigotAddon plugin;

    public MaintenancePlaceholder(final MaintenanceSpigotAddon plugin) {
        this.plugin = plugin;
    }

    @Override
    public String onRequest(final OfflinePlayer player, final String identifier) {
        if (identifier.equals("status")) {
            return plugin.getMessages().get(plugin.isMaintenance() ? "maintenance-on" : "maintenance-off");
        } else if (identifier.startsWith("server_")) {
            return plugin.getMessages().get(plugin.getMaintenanceServers().contains(identifier.substring(7).toLowerCase()) ? "single-maintenance-on" : "single-maintenance-off");
        }
        return null;
    }

    @Override
    public String getIdentifier() {
        return "maintenance";
    }

    @Override
    public String getAuthor() {
        return "KennyTV";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }
}
