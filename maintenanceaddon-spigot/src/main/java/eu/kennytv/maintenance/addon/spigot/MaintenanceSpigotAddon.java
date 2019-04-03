package eu.kennytv.maintenance.addon.spigot;

import eu.kennytv.maintenance.addon.spigot.listener.MessagingListener;
import eu.kennytv.maintenance.addon.spigot.listener.PlayerJoinListener;
import me.clip.placeholderapi.PlaceholderAPI;
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
        getServer().getMessenger().registerIncomingPluginChannel(this, "Return", new MessagingListener(this));
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        PlaceholderAPI.registerPlaceholderHook("maintenance", new MaintenancePlaceholder(this));
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
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
