package eu.kennytv.maintenance.addon.velocity.listener;

import eu.kennytv.maintenance.addon.velocity.MaintenanceVelocityAddon;
import eu.kennytv.maintenance.api.event.manager.EventListener;
import eu.kennytv.maintenance.api.event.proxy.ServerMaintenanceChangedEvent;
import java.util.Locale;

public final class ServerMaintenanceChangedListener extends EventListener<ServerMaintenanceChangedEvent> {
    private final MaintenanceVelocityAddon plugin;

    public ServerMaintenanceChangedListener(final MaintenanceVelocityAddon plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEvent(final ServerMaintenanceChangedEvent event) {
        final String serverName = event.getServer().getName().toLowerCase(Locale.ROOT);
        plugin.messageSender().sendPluginMessage(serverName, event.isMaintenance());
    }
}
