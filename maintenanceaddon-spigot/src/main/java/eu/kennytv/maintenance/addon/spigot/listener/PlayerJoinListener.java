package eu.kennytv.maintenance.addon.spigot.listener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import eu.kennytv.maintenance.addon.spigot.MaintenanceSpigotAddon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoinListener implements Listener {
    private final MaintenanceSpigotAddon plugin;

    public PlayerJoinListener(final MaintenanceSpigotAddon plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerJoin(final PlayerJoinEvent event) {
        // Request data from Bungee when the first player joins, as the messaging requires players
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("maintenance:inforequest");
        event.getPlayer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());

        // We don't need this listener anymore
        event.getHandlers().unregister(this);
    }
}
