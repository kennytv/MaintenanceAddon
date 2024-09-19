package eu.kennytv.maintenance.addon.paper;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import eu.kennytv.maintenance.addon.MaintenanceChannel;
import eu.kennytv.maintenance.addon.paper.command.MaintenanceAddonCommand;
import eu.kennytv.maintenance.addon.paper.expansion.MaintenanceMiniPlaceholdersExpansion;
import eu.kennytv.maintenance.addon.paper.expansion.MaintenancePAPIExpansion;
import eu.kennytv.maintenance.addon.paper.listener.MessagingListener;
import eu.kennytv.maintenance.addon.paper.listener.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class MaintenancePaperAddon extends JavaPlugin {
    private final Set<String> maintenanceServers = new HashSet<>();
    private final Map<String, String> messages = new HashMap<>();
    private boolean maintenance;

    @Override
    public void onEnable() {
        registerExpansions();

        getServer().getMessenger().registerOutgoingPluginChannel(this, MaintenanceChannel.REQUEST_CHANNEL_ID);
        getServer().getMessenger().registerIncomingPluginChannel(this, MaintenanceChannel.DATA_CHANNEL_ID, new MessagingListener(this));

        final Collection<? extends Player> players = getServer().getOnlinePlayers();
        if (players.isEmpty()) {
            getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        } else {
            sendInitialRequest(players.stream().findAny().orElse(null));
        }

        // Debug command
        final PluginCommand command = getCommand("maintenanceaddon");
        if (command != null) {
            command.setExecutor(new MaintenanceAddonCommand());
        }
    }

    public void sendInitialRequest(final Player player) {
        // Because the messaging sucks
        try {
            final String className = Bukkit.getServer().getClass().getName();
            final String craftBukkitPackage = className.substring(0, className.lastIndexOf('.'));
            Class.forName(craftBukkitPackage + ".entity.CraftPlayer")
                    .getMethod("addChannel", String.class).invoke(player, MaintenanceChannel.REQUEST_CHANNEL_ID);
        } catch (final IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        // Request data from Bungee when the first player joins, as the messaging requires players
        final ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
        dataOutput.writeBoolean(true);
        player.sendPluginMessage(this, MaintenanceChannel.REQUEST_CHANNEL_ID, dataOutput.toByteArray());
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

    private void registerExpansions() {
        PluginManager manager = getServer().getPluginManager();

        boolean expansionRegistered = false;
        if (manager.isPluginEnabled("MiniPlaceholders")) {
            new MaintenanceMiniPlaceholdersExpansion(this).register();
            getLogger().info("Registered MiniPlaceholders expansion");
            expansionRegistered = true;
        }
        if (manager.isPluginEnabled("PlaceholderAPI")) {
            new MaintenancePAPIExpansion(this).register();
            getLogger().info("Registered PlaceholderAPI expansion");
            expansionRegistered = true;
        }

        if (!expansionRegistered) {
            getLogger().warning("Neither MiniPlaceholders or PlaceholderAPI was found. Placeholders won't be registered.");
        }
    }
}
