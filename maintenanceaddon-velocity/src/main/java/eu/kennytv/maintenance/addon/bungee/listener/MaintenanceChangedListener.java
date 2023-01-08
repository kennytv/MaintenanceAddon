package eu.kennytv.maintenance.addon.bungee.listener;

import eu.kennytv.maintenance.addon.bungee.MaintenanceVelocityAddon;
import eu.kennytv.maintenance.api.event.MaintenanceChangedEvent;
import eu.kennytv.maintenance.api.event.manager.EventListener;

public final class MaintenanceChangedListener extends EventListener<MaintenanceChangedEvent> {
    private final MaintenanceVelocityAddon plugin;

    public MaintenanceChangedListener(final MaintenanceVelocityAddon plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEvent(final MaintenanceChangedEvent event) {
        plugin.messageSender().sendPluginMessage(event.isMaintenance());
    }
}
