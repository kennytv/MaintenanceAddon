package eu.kennytv.maintenance.addon.spigot.listener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import eu.kennytv.maintenance.addon.spigot.MaintenanceSpigotAddon;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.lang.reflect.InvocationTargetException;

public final class PlayerJoinListener implements Listener {
    private final MaintenanceSpigotAddon plugin;

    public PlayerJoinListener(final MaintenanceSpigotAddon plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerJoin(final PlayerJoinEvent event) {
        // Because the messaging sucks
        try {
            final String packageName = Bukkit.getServer().getClass().getPackage().getName();
            final String version = packageName.substring(packageName.lastIndexOf('.') + 1);
            Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer")
                    .getMethod("addChannel", String.class).invoke(event.getPlayer(), "maintenance:request");
        } catch (final IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        // Request data from Bungee when the first player joins, as the messaging requires players
        final ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
        dataOutput.writeBoolean(true);
        event.getPlayer().sendPluginMessage(plugin, "maintenance:request", dataOutput.toByteArray());

        // We don't need this listener anymore
        event.getHandlers().unregister(this);
    }
}
