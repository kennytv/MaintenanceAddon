package eu.kennytv.maintenance.addon.spigot.listener;

import eu.kennytv.maintenance.addon.spigot.MaintenanceSpigotAddon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoinListener implements Listener {
    private final MaintenanceSpigotAddon plugin;

    public PlayerJoinListener(final MaintenanceSpigotAddon plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerJoin(final PlayerJoinEvent event) {
        plugin.sendInitialRequest(event.getPlayer());

        // We don't need this listener anymore
        event.getHandlers().unregister(this);
    }
}
