package eu.kennytv.maintenance.addon.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import eu.kennytv.maintenance.addon.MaintenanceChannel;
import eu.kennytv.maintenance.addon.MessageSender;
import eu.kennytv.maintenance.addon.velocity.MaintenanceAddonVersion;
import eu.kennytv.maintenance.addon.velocity.expansion.MaintenanceMiniPlaceholdersExpansion;
import eu.kennytv.maintenance.addon.velocity.listener.MaintenanceChangedListener;
import eu.kennytv.maintenance.addon.velocity.listener.MaintenanceReloadedListener;
import eu.kennytv.maintenance.addon.velocity.listener.MessagingListener;
import eu.kennytv.maintenance.addon.velocity.listener.ServerMaintenanceChangedListener;
import eu.kennytv.maintenance.api.MaintenanceProvider;
import eu.kennytv.maintenance.api.event.MaintenanceChangedEvent;
import eu.kennytv.maintenance.api.event.MaintenanceReloadedEvent;
import eu.kennytv.maintenance.api.event.manager.EventManager;
import eu.kennytv.maintenance.api.event.proxy.ServerMaintenanceChangedEvent;
import eu.kennytv.maintenance.core.config.Config;
import eu.kennytv.maintenance.core.proxy.MaintenanceProxyPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@Plugin(id = "maintenanceaddon", name = "MaintenanceAddon", version = MaintenanceAddonVersion.VERSION, authors = "kennytv",
        url = "https://hangar.papermc.io/kennytv/MaintenanceAddon",
        dependencies = {@Dependency(id = "maintenance"), @Dependency(id = "miniplaceholders", optional = true)})
public final class MaintenanceVelocityAddon {
    private final ProxyServer server;
    private MessageSender messageSender;

    @Inject
    public MaintenanceVelocityAddon(final ProxyServer server) {
        this.server = server;
    }

    @Subscribe
    public void proxyInitialize(final ProxyInitializeEvent event) {
        final MaintenanceProxyPlugin maintenance = (MaintenanceProxyPlugin) MaintenanceProvider.get();
        final File file = new File(maintenance.getDataFolder(), "addon.yml");
        if (!file.exists()) {
            try (final InputStream in = MaintenanceVelocityAddon.class.getClassLoader().getResourceAsStream("addon.yml")) {
                Files.copy(in, file.toPath());
            } catch (final IOException e) {
                throw new RuntimeException("Unable to create addon.yml file for Maintenance!", e);
            }
        }

        final Config config = new Config(file);
        try {
            config.load();
        } catch (final IOException e) {
            throw new RuntimeException("Error loading Maintenance addon.yml", e);
        }

        if (server.getPluginManager().isLoaded("miniplaceholders")) {
            new MaintenanceMiniPlaceholdersExpansion(maintenance, config).register();
        }

        messageSender = new VelocityMessageSender(server, maintenance, config);

        server.getChannelRegistrar().register(MinecraftChannelIdentifier.from(MaintenanceChannel.REQUEST_CHANNEL_ID));

        server.getEventManager().register(this, new MessagingListener(this));

        final EventManager eventManager = maintenance.getEventManager();
        eventManager.registerListener(new MaintenanceChangedListener(this), MaintenanceChangedEvent.class);
        eventManager.registerListener(new ServerMaintenanceChangedListener(this), ServerMaintenanceChangedEvent.class);
        eventManager.registerListener(new MaintenanceReloadedListener(this), MaintenanceReloadedEvent.class);

        messageSender.sendMessages();
        messageSender.sendAllServers();
    }

    public MessageSender messageSender() {
        return messageSender;
    }
}
