package eu.kennytv.maintenance.addon.bungee.listener;

import eu.kennytv.maintenance.addon.bungee.MaintenanceBungeeAddon;
import eu.kennytv.maintenance.api.event.MaintenanceReloadedEvent;
import eu.kennytv.maintenance.api.event.manager.EventListener;

public final class MaintenanceReloadedListener extends EventListener<MaintenanceReloadedEvent> {
    private final MaintenanceBungeeAddon plugin;

    public MaintenanceReloadedListener(final MaintenanceBungeeAddon plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEvent(final MaintenanceReloadedEvent event) {
        plugin.messageSender().sendAllServers();
    }
}
