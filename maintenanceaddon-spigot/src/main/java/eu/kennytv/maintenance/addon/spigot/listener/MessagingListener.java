package eu.kennytv.maintenance.addon.spigot.listener;

import eu.kennytv.maintenance.addon.spigot.MaintenanceSpigotAddon;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public final class MessagingListener implements PluginMessageListener {
    private final MaintenanceSpigotAddon plugin;

    public MessagingListener(final MaintenanceSpigotAddon plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPluginMessageReceived(final String s, final Player player, final byte[] bytes) {
        final DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            final String channel = in.readUTF();
            if (channel.equals("maintenance:changed")) {
                plugin.setMaintenance(Boolean.parseBoolean(in.readUTF()));
            } else if (channel.equals("maintenance:server")) {
                final String server = in.readUTF();
                if (Boolean.parseBoolean(in.readUTF())) {
                    plugin.getMaintenanceServers().add(server);
                } else {
                    plugin.getMaintenanceServers().remove(server);
                }
            } else if (channel.equals("maintenance:messages")) {
                plugin.getMessages().put(in.readUTF(), in.readUTF());
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}