package eu.kennytv.maintenance.addon;

public enum MaintenanceChannel {

    /**
     * Sends a list of configured messages.
     */
    MESSAGES,
    /**
     * Sends the name and status of a single server.
     */
    SERVER,
    /**
     * Sends a full list of servers with maintenance *enabled* with the very first entry being the global status.
     * This also resets the currently saved list of servers before adding the incoming ones.
     */
    SERVERS,
    /**
     * Sends a boolean for the global maintenance status.
     */
    GLOBAL_STATUS;

    private static final MaintenanceChannel[] VALUES = values();

    /**
     * @return cached array of the enum array
     */
    public static MaintenanceChannel[] getValues() {
        return VALUES;
    }
}
