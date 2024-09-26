package eu.kennytv.maintenance.addon.bungee.listener;

import eu.kennytv.maintenance.addon.bungee.MaintenanceBungeeAddon;
import eu.kennytv.maintenance.api.event.manager.EventListener;
import eu.kennytv.maintenance.api.event.proxy.ServerMaintenanceChangedEvent;
import java.util.Locale;

public final class ServerMaintenanceChangedListener extends EventListener<ServerMaintenanceChangedEvent> {
    private final MaintenanceBungeeAddon plugin;

    public ServerMaintenanceChangedListener(final MaintenanceBungeeAddon plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEvent(final ServerMaintenanceChangedEvent event) {
        final String serverName = event.getServer().getName().toLowerCase(Locale.ROOT);
        plugin.messageSender().sendPluginMessage(serverName, event.isMaintenance());
    }
}
