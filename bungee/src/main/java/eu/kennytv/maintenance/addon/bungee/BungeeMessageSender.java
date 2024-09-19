package eu.kennytv.maintenance.addon.bungee;

import eu.kennytv.maintenance.addon.MaintenanceChannel;
import eu.kennytv.maintenance.addon.MessageSender;
import eu.kennytv.maintenance.api.proxy.MaintenanceProxy;
import eu.kennytv.maintenance.core.config.Config;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.MiniMessage;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.Collection;
import java.util.Map;

public final class BungeeMessageSender extends MessageSender {
    private final ProxyServer server;
    private final MaintenanceProxy maintenance;
    private final Config config;

    public BungeeMessageSender(final ProxyServer server, final MaintenanceProxy maintenance, final Config config) {
        this.server = server;
        this.maintenance = maintenance;
        this.config = config;
    }

    @Override
    protected Map<String, Object> configValues() {
        return config.getValues();
    }

    @Override
    protected void sendToAllServers(final byte[] data) {
        for (final ServerInfo server : server.getServers().values()) {
            server.sendData(MaintenanceChannel.DATA_CHANNEL_ID, data);
        }
    }

    @Override
    protected boolean isGlobalMaintenance() {
        return maintenance.isMaintenance();
    }

    @Override
    protected Collection<String> maintenanceServers() {
        return maintenance.getMaintenanceServers();
    }
}
