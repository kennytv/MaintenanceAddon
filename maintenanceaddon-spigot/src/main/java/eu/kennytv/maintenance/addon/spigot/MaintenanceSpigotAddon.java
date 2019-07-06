package eu.kennytv.maintenance.addon.spigot;

import eu.kennytv.maintenance.addon.spigot.command.MaintenanceAddonCommand;
import eu.kennytv.maintenance.addon.spigot.listener.MessagingListener;
import eu.kennytv.maintenance.addon.spigot.listener.PlayerJoinListener;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class MaintenanceSpigotAddon extends JavaPlugin {
    private final Set<String> maintenanceServers = new HashSet<>();
    private final Map<String, String> messages = new HashMap<>();
    private boolean maintenance;

    @Override
    public void onEnable() {
        PlaceholderAPI.registerPlaceholderHook("maintenance", new MaintenancePlaceholder(this));

        getServer().getMessenger().registerOutgoingPluginChannel(this, "maintenance:request");
        getServer().getMessenger().registerIncomingPluginChannel(this, "maintenance:return", new MessagingListener(this));
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        // Debug command
        final PluginCommand command = getCommand("maintenanceaddon");
        if (command != null)
            command.setExecutor(new MaintenanceAddonCommand());
    }

    public boolean isMaintenance() {
        return maintenance;
    }

    public void setMaintenance(final boolean maintenance) {
        this.maintenance = maintenance;
    }

    public Set<String> getMaintenanceServers() {
        return maintenanceServers;
    }

    public Map<String, String> getMessages() {
        return messages;
    }
}
