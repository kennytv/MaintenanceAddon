package eu.kennytv.maintenance.addon.bungee.listener;

import eu.kennytv.maintenance.addon.MaintenanceChannel;
import eu.kennytv.maintenance.addon.bungee.MaintenanceBungeeAddon;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public final class MessagingListener implements Listener {
    private final MaintenanceBungeeAddon plugin;

    public MessagingListener(final MaintenanceBungeeAddon plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void pluginMessage(final PluginMessageEvent event) {
        if (event.getTag().equals(MaintenanceChannel.REQUEST_CHANNEL_ID)) {
            plugin.messageSender().sendMessages();
            plugin.messageSender().sendAllServers();
        }
    }
}
