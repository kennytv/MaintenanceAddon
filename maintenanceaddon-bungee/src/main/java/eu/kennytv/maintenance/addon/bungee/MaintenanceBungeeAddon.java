package eu.kennytv.maintenance.addon.bungee;

import eu.kennytv.maintenance.addon.DataOutputStreamConsumer;
import eu.kennytv.maintenance.addon.MaintenanceChannel;
import eu.kennytv.maintenance.addon.bungee.listener.MaintenanceChangedListener;
import eu.kennytv.maintenance.addon.bungee.listener.MaintenanceReloadedListener;
import eu.kennytv.maintenance.addon.bungee.listener.MessagingListener;
import eu.kennytv.maintenance.addon.bungee.listener.ServerMaintenanceChangedListener;
import eu.kennytv.maintenance.api.bungee.MaintenanceBungeeAPI;
import eu.kennytv.maintenance.api.event.MaintenanceChangedEvent;
import eu.kennytv.maintenance.api.event.MaintenanceReloadedEvent;
import eu.kennytv.maintenance.api.event.manager.IEventManager;
import eu.kennytv.maintenance.api.event.proxy.ServerMaintenanceChangedEvent;
import eu.kennytv.maintenance.core.config.Config;
import eu.kennytv.maintenance.core.proxy.MaintenanceProxyPlugin;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Map;
import java.util.Set;

public final class MaintenanceBungeeAddon extends Plugin {
    private MaintenanceProxyPlugin maintenanceApi;
    private Config config;

    @Override
    public void onEnable() {
        maintenanceApi = (MaintenanceProxyPlugin) MaintenanceBungeeAPI.getAPI();

        final File file = new File(maintenanceApi.getDataFolder(), "addon.yml");
        if (!file.exists()) {
            try (final InputStream in = getResourceAsStream("addon.yml")) {
                Files.copy(in, file.toPath());
            } catch (final IOException e) {
                throw new RuntimeException("Unable to create addon.yml file for Maintenance!", e);
            }
        }

        config = new Config(new File(maintenanceApi.getDataFolder(), "addon.yml"));
        try {
            config.load();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        getProxy().getPluginManager().registerListener(this, new MessagingListener(this));
        final IEventManager eventManager = maintenanceApi.getEventManager();
        eventManager.registerListener(new MaintenanceChangedListener(this), MaintenanceChangedEvent.class);
        eventManager.registerListener(new ServerMaintenanceChangedListener(this), ServerMaintenanceChangedEvent.class);
        eventManager.registerListener(new MaintenanceReloadedListener(this), MaintenanceReloadedEvent.class);

        sendMessages();
        sendAllServers();
    }

    public void sendMessages() {
        sendPluginMessage(MaintenanceChannel.MESSAGES, out -> {
            out.writeInt(config.getValues().size());
            for (final Map.Entry<String, Object> entry : config.getValues().entrySet()) {
                out.writeUTF(entry.getKey());
                out.writeUTF(ChatColor.translateAlternateColorCodes('&', String.valueOf(entry.getValue())));
            }
        });
    }

    public void sendAllServers() {
        sendPluginMessage(MaintenanceChannel.SERVERS, out -> {
            // Global status
            out.writeBoolean(maintenanceApi.isMaintenance());

            // Single servers with maintenance enabled
            final Set<String> servers = maintenanceApi.getMaintenanceServers();
            out.writeInt(servers.size());
            for (final String server : servers) {
                out.writeUTF(server.toLowerCase());
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
            e.printStackTrace();
            return;
        }

        final byte[] data = stream.toByteArray();
        for (final ServerInfo server : getProxy().getServers().values()) {
            server.sendData("maintenance:return", data);
        }
    }
}
