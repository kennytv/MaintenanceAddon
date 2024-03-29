package eu.kennytv.maintenance.addon.paper.listener;

import eu.kennytv.maintenance.addon.MaintenanceChannel;
import eu.kennytv.maintenance.addon.paper.MaintenancePaperAddon;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public final class MessagingListener implements PluginMessageListener {
    private final MaintenancePaperAddon plugin;

    public MessagingListener(final MaintenancePaperAddon plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPluginMessageReceived(final @NotNull String s, final @NotNull Player player, final byte[] bytes) {
        final DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            final byte ordinal = in.readByte();
            if (ordinal < 0 || ordinal >= MaintenanceChannel.getValues().length) {
                return;
            }

            final MaintenanceChannel channel = MaintenanceChannel.getValues()[ordinal];
            switch (channel) {
                case MESSAGES: {
                    final int length = in.readByte();
                    for (int i = 0; i < length; i++) {
                        final String key = in.readUTF();
                        final String message = in.readUTF();
                        plugin.getMessages().put(key, message);
                    }
                    break;
                }
                case SERVER: {
                    final String server = in.readUTF();
                    if (in.readBoolean()) {
                        plugin.getMaintenanceServers().add(server);
                    } else {
                        plugin.getMaintenanceServers().remove(server);
                    }
                    break;
                }
                case SERVERS: {
                    plugin.setMaintenance(in.readBoolean());

                    final int length = in.readShort();
                    plugin.getMaintenanceServers().clear();
                    for (int i = 0; i < length; i++) {
                        plugin.getMaintenanceServers().add(in.readUTF());
                    }
                    break;
                }
                case GLOBAL_STATUS: {
                    plugin.setMaintenance(in.readBoolean());
                    break;
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}