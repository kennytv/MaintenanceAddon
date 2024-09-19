package eu.kennytv.maintenance.addon.paper.listener;

import eu.kennytv.maintenance.addon.paper.MaintenancePaperAddon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoinListener implements Listener {
    private final MaintenancePaperAddon plugin;

    public PlayerJoinListener(final MaintenancePaperAddon plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerJoin(final PlayerJoinEvent event) {
        plugin.sendInitialRequest(event.getPlayer());

        // We don't need this listener anymore
        event.getHandlers().unregister(this);
    }
}
