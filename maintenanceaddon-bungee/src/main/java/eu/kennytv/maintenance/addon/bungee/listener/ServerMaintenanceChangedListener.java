package eu.kennytv.maintenance.addon.bungee.listener;

import eu.kennytv.maintenance.addon.bungee.MaintenanceBungeeAddon;
import eu.kennytv.maintenance.api.event.manager.EventListener;
import eu.kennytv.maintenance.api.event.proxy.ServerMaintenanceChangedEvent;

public final class ServerMaintenanceChangedListener extends EventListener<ServerMaintenanceChangedEvent> {
    private final MaintenanceBungeeAddon plugin;

    public ServerMaintenanceChangedListener(final MaintenanceBungeeAddon plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEvent(final ServerMaintenanceChangedEvent event) {
        plugin.sendPluginMessage("server", event.getServer().getName().toLowerCase(), String.valueOf(event.isMaintenance()));
    }
}
