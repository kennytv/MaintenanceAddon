package eu.kennytv.maintenance.addon.bungee.listener;

import eu.kennytv.maintenance.addon.bungee.MaintenanceVelocityAddon;
import eu.kennytv.maintenance.api.event.manager.EventListener;
import eu.kennytv.maintenance.api.event.proxy.ServerMaintenanceChangedEvent;

public final class ServerMaintenanceChangedListener extends EventListener<ServerMaintenanceChangedEvent> {
    private final MaintenanceVelocityAddon plugin;

    public ServerMaintenanceChangedListener(final MaintenanceVelocityAddon plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEvent(final ServerMaintenanceChangedEvent event) {
        plugin.messageSender().sendPluginMessage(event.getServer().getName().toLowerCase(), event.isMaintenance());
    }
}
