package eu.kennytv.maintenance.addon.paper.expansion;

import eu.kennytv.maintenance.addon.paper.MaintenancePaperAddon;
import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.utils.Tags;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;

public final class MaintenanceMiniPlaceholdersExpansion {
	private final MiniMessage miniMessage = MiniMessage.miniMessage();
	private final Expansion expansion;

	public MaintenanceMiniPlaceholdersExpansion(MaintenancePaperAddon plugin) {
		expansion = Expansion.builder("maintenance")
				.author("kennytv")
				.version("3.0.0-SNAPSHOT")
				.globalPlaceholder("status", (queue, ctx) -> {
					String status;
					if (!queue.hasNext()) {
						status = plugin.getMessages().get(plugin.isMaintenance() ? "maintenance-on" : "maintenance-off");
					} else {
						String serverName = queue.pop().lowerValue();
						boolean maintenanceEnabled = plugin.getMaintenanceServers().contains(serverName);
						status = plugin.getMessages().get(maintenanceEnabled ? "single-maintenance-on" : "single-maintenance-off");
					}

					if (status == null) {
						return Tags.EMPTY_TAG;
					}

					return Tag.selfClosingInserting(miniMessage.deserialize(status));
				}).build();
	}

	public void register() {
		expansion.register();
	}
}
