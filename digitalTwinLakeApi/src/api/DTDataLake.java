package api;

import java.io.Closeable;

/**
 * @author Daniel Pérez - University of Málaga
 * API to access the Data Lake.
 */
@SuppressWarnings("unused")
public class DTDataLake implements Closeable {

    private static final String DT_OUTPUT_SNAPSHOT = "DTOutputSnapshot";
    private static final String PT_OUTPUT_SNAPSHOT = "PTOutputSnapshot";

    DTDataLake() {
        // TODO
    }

    @Override
    public void close() {
        // TODO
    }

    /**
     * Performs a ping.
     * @return True if the ping was answered with PONG.
     */
    public boolean ping() {
        // TODO
        return false;
    }

    /**
     * Gets the current time for the Physical Twin.
     * @return The value of the Physical Twin's clock.
     */
    public int getPTTime() {
        // TODO
        return 0;
    }

    /**
     * Gets the current time for the Digital Twin.
     * @return The value of the Digital Twin's clock.
     */
    public int getDTTime() {
        // TODO
        return 0;
    }

    /**
     * Advances the Digital Twin's time.
     * @param amount The number of milliseconds to advance.
     */
    public void advanceDTTime(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount must be non-negative");
        }
        // TODO
    }

    /**
     * Returns the ID of the current execution.
     * @return The ID of the current execution.
     */
    public String getCurrentExecutionId() {
        // TODO
        return null;
    }

    /**
     * Returns the current value of the command counter.
     * @return The current value of the command counter.
     */
    public int getCommandCounter() {
        // TODO
        return 0;
    }

    /**
     * Generates and returns a DLTwin object to perform queries on a specific twin system
     * in the current executionId.
     * @param twinId The ID of the twin to query.
     * @return A DLTwin object to perform queries on the specified twin.
     */
    public DLTwin forTwin(String twinId) {
        return new DLTwin(this, twinId);
    }

}
