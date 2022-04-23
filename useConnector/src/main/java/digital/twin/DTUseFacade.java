package digital.twin;

import org.tzi.use.api.UseSystemApi;
import org.tzi.use.uml.sys.MObjectState;
import utils.UseFacade;

import java.util.Objects;

/**
 * @author Daniel Pérez - University of Málaga
 * A collection of utility methods specifically to manipulate the digital twin USE model.
 */
public class DTUseFacade extends UseFacade {

    /**
     * Sets the API instance to use for all subsequent calls to UseFacade methods.
     * @param api The USE API instance to interact with the currently displayed object diagram.
     */
    public DTUseFacade(UseSystemApi api) {
        super(api);
    }

    /**
     * Returns the current timestamp according to the model's clock.
     * @return The value of the "now" attribute in the Clock instance.
     */
    public int getCurrentTime() {
        MObjectState clock = Objects.requireNonNull(getAnyObjectOfClass("Clock"));
        return getIntegerAttribute(clock, "now");
    }

    /**
     * Sets the execution IDs of all robots in the model to the current POSIX timestamp
     * @return The current POSIX timestamp, as a string.
     */
    public long setExecutionIds() {
        long posixTime = System.currentTimeMillis();
        for (MObjectState clock : getObjectsOfClass("BraccioRobot")) {
            setAttribute(clock, "executionId", posixTime + "");
        }
        return posixTime;
    }

}
