package digital.twin;

import digital.twin.attributes.AttributeType;
import org.tzi.use.uml.sys.MObjectState;
import pubsub.DTPubSub;
import redis.clients.jedis.Jedis;
import utils.StringUtils;

import java.util.Map;

/**
 * @author Paula Muñoz, Daniel Pérez - University of Málaga
 * OutputManager that retrieves all Command instances and serializes them for storage in the data lake.
 */
public class CommandResultManager extends OutputManager {

    private int commandCounter;

    public CommandResultManager(DTUseFacade useApi) {
        super(useApi, DTPubSub.COMMAND_OUT_CHANNEL, "CommandResult", "DTCommandResult");
        attributeSpecification.set("twinId", AttributeType.STRING);
        attributeSpecification.set("executionId", AttributeType.STRING);
        attributeSpecification.set("commandName", AttributeType.STRING);
        attributeSpecification.set("commandArguments", AttributeType.STRING);
        attributeSpecification.set("commandTimestamp", AttributeType.INTEGER);
        attributeSpecification.set("return", AttributeType.STRING);
        commandCounter = 0;
    }

    protected String getObjectId(MObjectState objstate) {
        String twinId = useApi.getStringAttribute(objstate, "twinId");
        String executionId = useApi.getStringAttribute(objstate, "executionId");
        return StringUtils.removeQuotes(twinId) + ":" + StringUtils.removeQuotes(executionId)
                + ":" + ++commandCounter;
    }

    protected void addObjectQueryRegisters(
            Jedis jedis, String objectTypeAndId, Map<String, String> values) { }

    protected void addAttributeQueryRegisters(
            Jedis jedis, String objectTypeAndId, String attributeName,
            AttributeType type, String attributeValue) { }

}
