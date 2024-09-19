package eu.kennytv.maintenance.addon.velocity;

public final class MaintenanceAddonVersion {
	public static final String VERSION = "{{ version }}";

	public static String getVersion() {
		return VERSION;
	}
}
