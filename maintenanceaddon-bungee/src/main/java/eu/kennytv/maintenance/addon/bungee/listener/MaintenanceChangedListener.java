package eu.kennytv.maintenance.addon.bungee.listener;

import eu.kennytv.maintenance.addon.bungee.MaintenanceBungeeAddon;
import eu.kennytv.maintenance.api.event.MaintenanceChangedEvent;
import eu.kennytv.maintenance.api.event.manager.EventListener;

public final class MaintenanceChangedListener extends EventListener<MaintenanceChangedEvent> {
    private final MaintenanceBungeeAddon plugin;

    public MaintenanceChangedListener(final MaintenanceBungeeAddon plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEvent(final MaintenanceChangedEvent event) {
        plugin.sendPluginMessage(event.isMaintenance());
    }
}
