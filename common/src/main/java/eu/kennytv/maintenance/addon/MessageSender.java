package eu.kennytv.maintenance.addon;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

public abstract class MessageSender {

    public void sendMessages() {
        sendPluginMessage(MaintenanceChannel.MESSAGES, out -> {
            final Map<String, Object> values = configValues();
            out.writeByte(values.size());
            for (final Map.Entry<String, Object> entry : values.entrySet()) {
                out.writeUTF(entry.getKey());
                out.writeUTF(String.valueOf(entry.getValue()));
            }
        });
    }

    public void sendAllServers() {
        sendPluginMessage(MaintenanceChannel.SERVERS, out -> {
            // Global status
            out.writeBoolean(isGlobalMaintenance());

            // Single servers with maintenance enabled
            final Collection<String> maintenanceServers = maintenanceServers();
            out.writeShort(maintenanceServers.size());
            for (final String server : maintenanceServers) {
                out.writeUTF(server.toLowerCase(Locale.ROOT));
            }
        });
    }

    public void sendPluginMessage(final boolean maintenance) {
        sendPluginMessage(MaintenanceChannel.GLOBAL_STATUS, out -> out.writeBoolean(maintenance));
    }

    public void sendPluginMessage(final String server, final boolean maintenance) {
        sendPluginMessage(MaintenanceChannel.SERVER, out -> {
            out.writeUTF(server);
            out.writeBoolean(maintenance);
        });
    }

    private void sendPluginMessage(final MaintenanceChannel channel, final DataOutputStreamConsumer consumer) {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        final DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeByte(channel.ordinal());
            consumer.accept(out);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        final byte[] data = stream.toByteArray();
        sendToAllServers(data);
    }

    protected abstract Map<String, Object> configValues();

    protected abstract void sendToAllServers(final byte[] data);

    protected abstract boolean isGlobalMaintenance();

    protected abstract Collection<String> maintenanceServers();
}
