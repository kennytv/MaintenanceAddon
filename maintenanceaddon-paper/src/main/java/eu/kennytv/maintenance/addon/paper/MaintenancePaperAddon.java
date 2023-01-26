package eu.kennytv.maintenance.addon.paper;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import eu.kennytv.maintenance.addon.MaintenanceChannel;
import eu.kennytv.maintenance.addon.paper.command.MaintenanceAddonCommand;
import eu.kennytv.maintenance.addon.paper.listener.MessagingListener;
import eu.kennytv.maintenance.addon.paper.listener.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
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
        new MaintenancePlaceholder(this).register();

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
            final String packageName = Bukkit.getServer().getClass().getPackage().getName();
            final String version = packageName.substring(packageName.lastIndexOf('.') + 1);
            Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer")
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
}
