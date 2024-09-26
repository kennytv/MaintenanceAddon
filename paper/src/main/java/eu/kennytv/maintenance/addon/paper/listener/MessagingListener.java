package eu.kennytv.maintenance.addon.paper.listener;

import eu.kennytv.maintenance.addon.MaintenanceChannel;
import eu.kennytv.maintenance.addon.paper.MaintenancePaperAddon;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public final class MessagingListener implements PluginMessageListener {
    private final MaintenancePaperAddon plugin;

    public MessagingListener(final MaintenancePaperAddon plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPluginMessageReceived(final @NotNull String s, final @NotNull Player player, final byte[] bytes) {
        try {
            read(bytes);
        } catch (final IOException ignored) {
        }
    }

    private void read(final byte[] bytes) throws IOException {
        final DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
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

                final Set<String> maintenanceServers = plugin.getMaintenanceServers();
                maintenanceServers.clear();

                final int length = in.readShort();
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
    }
}