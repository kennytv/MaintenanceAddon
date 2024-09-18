package eu.kennytv.maintenance.addon.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import eu.kennytv.maintenance.addon.MaintenanceChannel;
import eu.kennytv.maintenance.addon.velocity.MaintenanceVelocityAddon;

public final class MessagingListener {
    private final MaintenanceVelocityAddon plugin;

    public MessagingListener(final MaintenanceVelocityAddon plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void execute(final PluginMessageEvent event) {
        if (event.getIdentifier().getId().equals(MaintenanceChannel.REQUEST_CHANNEL_ID)) {
            plugin.messageSender().sendMessages();
            plugin.messageSender().sendAllServers();
        }
    }
}
