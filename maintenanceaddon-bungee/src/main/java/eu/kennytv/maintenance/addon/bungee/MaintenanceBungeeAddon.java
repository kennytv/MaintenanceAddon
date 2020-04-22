package eu.kennytv.maintenance.addon.bungee;

import eu.kennytv.maintenance.addon.bungee.listener.MaintenanceChangedListener;
import eu.kennytv.maintenance.addon.bungee.listener.MessagingListener;
import eu.kennytv.maintenance.addon.bungee.listener.ServerMaintenanceChangedListener;
import eu.kennytv.maintenance.api.bungee.MaintenanceBungeeAPI;
import eu.kennytv.maintenance.api.event.MaintenanceChangedEvent;
import eu.kennytv.maintenance.api.event.proxy.ServerMaintenanceChangedEvent;
import eu.kennytv.maintenance.core.config.Config;
import eu.kennytv.maintenance.core.proxy.MaintenanceProxyPlugin;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.*;
import java.nio.file.Files;

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
        maintenanceApi.getEventManager().registerListener(new MaintenanceChangedListener(this), MaintenanceChangedEvent.class);
        maintenanceApi.getEventManager().registerListener(new ServerMaintenanceChangedListener(this), ServerMaintenanceChangedEvent.class);
        sendInfo();
    }

    public void sendInfo() {
        config.getValues().forEach((key, value) -> sendPluginMessage("messages", key, ChatColor.translateAlternateColorCodes('&', String.valueOf(value))));
        maintenanceApi.getMaintenanceServers().forEach(server -> sendPluginMessage("server", server.toLowerCase(), "true"));
        sendPluginMessage("changed", String.valueOf(maintenanceApi.isMaintenance()));
    }

    public void sendPluginMessage(final String subchannel, final String... messages) {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        final DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeUTF(subchannel);
            for (final String message : messages) {
                out.writeUTF(message);
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }

        final byte[] data = stream.toByteArray();
        getProxy().getServers().values().forEach(server -> server.sendData("maintenance:return", data));
    }
}
