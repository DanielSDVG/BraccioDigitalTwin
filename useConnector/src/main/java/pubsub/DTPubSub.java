package pubsub;

import digital.twin.CommandsManager;
import digital.twin.InputSnapshotsManager;
import digital.twin.OutputSnapshotsManager;
import org.tzi.use.api.UseApiException;
import org.tzi.use.api.UseSystemApi;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import utils.DTLogger;

/**
 *
 * @author Paula Muñoz - University of Málaga
 *
 */
public class DTPubSub extends JedisPubSub {

    public static final String DT_OUT_CHANNEL = "DTOutChannel";
    public static final String DT_IN_CHANNEL = "DTInChannel";
    public static final String COMMAND_OUT_CHANNEL = "CommandOutChannel";
    private final UseSystemApi api;
    private final Jedis jedis;
    private final OutputSnapshotsManager dtOutSnapshotsManager;
    private final CommandsManager commandsManager;

    /**
     * Default constructor
     *
     * @param api   USE system API instance to interact with the currently displayed object diagram.
     * @param jedis An instance of the Jedis client to access the data lake.
     */
    public DTPubSub(UseSystemApi api, Jedis jedis) {
        this.api = api;
        this.jedis = jedis;
        dtOutSnapshotsManager = new OutputSnapshotsManager();
        commandsManager = new CommandsManager();
    }

    /**
     * This method is called every time a message is received through an specific channel
     *
     * @param channel Channel from which the message was received
     * @param message Message received
     */
    @Override
    public void onMessage(String channel, String message) {
        switch (channel) {

            case DT_IN_CHANNEL: // Info entering USE
                try {
                    InputSnapshotsManager.saveSnapshots(api, jedis);
                    DTLogger.info("New Input Snapshots saved");
                } catch (UseApiException ex) {
                    ex.printStackTrace();
                }
                break;

            case DT_OUT_CHANNEL: // Info leaving USE
                try {
                    this.dtOutSnapshotsManager.saveObjects(api, jedis);
                    DTLogger.info("New Output Snapshots saved");
                } catch (UseApiException ex) {
                    ex.printStackTrace();
                }
                break;
            case COMMAND_OUT_CHANNEL:
                try {
                    this.commandsManager.saveObjects(api, jedis);
                    DTLogger.info("New Commands saved");
                } catch (UseApiException ex) {
                    ex.printStackTrace();
                }
                break;

            default:
                DTLogger.warn("Received message in unknown channel: " + channel);
                break;

        }
    }

    /**
     * This method is called every time a process subscribes to one of the channels
     *
     * @param channel            Channel to which to subscribe
     * @param subscribedChannels Channel identifier
     */
    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        DTLogger.info("Client is subscribed to channel: " + channel);
    }

}
