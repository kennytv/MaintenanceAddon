package eu.kennytv.maintenance.addon.bungee;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import eu.kennytv.maintenance.addon.MaintenanceChannel;
import eu.kennytv.maintenance.addon.MessageSender;
import eu.kennytv.maintenance.api.proxy.MaintenanceProxy;
import eu.kennytv.maintenance.core.config.Config;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.MiniMessage;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.Collection;
import java.util.Map;

public final class VelocityMessageSender extends MessageSender {
    public static final MinecraftChannelIdentifier CHANNEL_IDENTIFIER = MinecraftChannelIdentifier.from(MaintenanceChannel.DATA_CHANNEL_ID);
    private final ProxyServer server;
    private final MaintenanceProxy maintenance;
    private final Config config;

    public VelocityMessageSender(final ProxyServer server, final MaintenanceProxy maintenance, final Config config) {
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
        for (final RegisteredServer server : server.getAllServers()) {
            server.sendPluginMessage(CHANNEL_IDENTIFIER, data);
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

    @Override
    protected String yuckifyRichMessage(String s) {
        final Component component = MiniMessage.miniMessage().deserialize(s);
        return LegacyComponentSerializer.legacySection().serialize(component);
    }
}
