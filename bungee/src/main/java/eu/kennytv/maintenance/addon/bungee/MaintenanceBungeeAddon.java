package eu.kennytv.maintenance.addon.bungee;

import eu.kennytv.maintenance.addon.MessageSender;
import eu.kennytv.maintenance.addon.bungee.listener.MaintenanceChangedListener;
import eu.kennytv.maintenance.addon.bungee.listener.MaintenanceReloadedListener;
import eu.kennytv.maintenance.addon.bungee.listener.MessagingListener;
import eu.kennytv.maintenance.addon.bungee.listener.ServerMaintenanceChangedListener;
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
import net.md_5.bungee.api.plugin.Plugin;

public final class MaintenanceBungeeAddon extends Plugin {
    private MessageSender messageSender;

    @Override
    public void onEnable() {
        final MaintenanceProxyPlugin maintenance = (MaintenanceProxyPlugin) MaintenanceProvider.get();
        final File file = new File(maintenance.getDataFolder(), "addon.yml");
        if (!file.exists()) {
            try (final InputStream in = getResourceAsStream("addon.yml")) {
                Files.copy(in, file.toPath());
            } catch (final IOException e) {
                throw new RuntimeException("Unable to create addon.yml file for Maintenance!", e);
            }
        }

        final Config config = new Config(new File(maintenance.getDataFolder(), "addon.yml"));
        try {
            config.load();
        } catch (final IOException e) {
            throw new RuntimeException("Error loading Maintenance addon.yml", e);
        }

        messageSender = new BungeeMessageSender(getProxy(), maintenance, config);

        getProxy().getPluginManager().registerListener(this, new MessagingListener(this));
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
