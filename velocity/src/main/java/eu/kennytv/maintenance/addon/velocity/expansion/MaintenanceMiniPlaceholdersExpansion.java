package eu.kennytv.maintenance.addon.velocity.expansion;

import eu.kennytv.maintenance.api.proxy.MaintenanceProxy;
import eu.kennytv.maintenance.core.config.Config;
import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.utils.TagsUtils;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class MaintenanceMiniPlaceholdersExpansion {
	private final MiniMessage miniMessage = MiniMessage.miniMessage();
	private final Expansion expansion;

	public MaintenanceMiniPlaceholdersExpansion(MaintenanceProxy maintenance, Config config) {
		expansion = Expansion.builder("maintenance")
				.globalPlaceholder("status", (queue, ctx) -> {
					String status;
					if (!queue.hasNext()) {
						status = config.getString(maintenance.isMaintenance() ? "maintenance-on" : "maintenance-off");
					} else {
						String serverName = queue.pop().lowerValue();
						status = config.getString(maintenance.getMaintenanceServers().contains(serverName) ? "single-maintenance-on" : "single-maintenance-off");
					}

					if (status == null) {
						return TagsUtils.EMPTY_TAG;
					}

					return TagsUtils.staticTag(miniMessage.deserialize(status));
				}).build();
	}

	public void register() {
		expansion.register();
	}
}
