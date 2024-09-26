package eu.kennytv.maintenance.addon.paper.expansion;

import eu.kennytv.maintenance.addon.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.addon.lib.kyori.adventure.text.minimessage.MiniMessage;
import eu.kennytv.maintenance.addon.lib.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import eu.kennytv.maintenance.addon.paper.MaintenancePaperAddon;
import java.util.Locale;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public final class MaintenancePAPIExpansion extends PlaceholderExpansion {
	private static final String SERVER_PREFIX = "server_";
	private static final int SERVER_PREFIX_LENGTH = SERVER_PREFIX.length();
	private final MaintenancePaperAddon plugin;

	public MaintenancePAPIExpansion(final MaintenancePaperAddon plugin) {
		this.plugin = plugin;
	}

	@Override
	public String onRequest(final OfflinePlayer player, final String identifier) {
		if (identifier.equals("status")) {
			String message = plugin.getMessages().get(plugin.isMaintenance() ? "maintenance-on" : "maintenance-off");
			return yuckifyRichMessage(message);
		} else if (identifier.startsWith(SERVER_PREFIX)) {
			String serverName = identifier.substring(SERVER_PREFIX_LENGTH).toLowerCase(Locale.ROOT);
			boolean maintenanceEnabled = plugin.getMaintenanceServers().contains(serverName);
			String message = plugin.getMessages().get(maintenanceEnabled ? "single-maintenance-on" : "single-maintenance-off");
			return yuckifyRichMessage(message);
		}
		return null;
	}

	@Override
	public @NotNull String getIdentifier() {
		return "maintenance";
	}

	@Override
	public @NotNull String getAuthor() {
		return "kennytv";
	}

	@Override
	public @NotNull String getVersion() {
		return plugin.getDescription().getVersion();
	}

	@Override
	public boolean persist() {
		return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
	}

	private String yuckifyRichMessage(String s) {
		if (s == null) {
			return "";
		}

		final Component component = MiniMessage.miniMessage().deserialize(s);
		return LegacyComponentSerializer.legacySection().serialize(component);
	}
}
