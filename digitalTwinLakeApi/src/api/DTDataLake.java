package api;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author Daniel Pérez - University of Málaga
 * API to access the Data Lake.
 */
@SuppressWarnings("unused")
public class DTDataLake implements Closeable {

    private final Jedis jedis;

    DTDataLake(JedisPool pool) {
        jedis = pool.getResource();
    }

    @Override
    public void close() {
        jedis.close();
    }

    public boolean ping() {
        return jedis.ping().equalsIgnoreCase("PONG");
    }

    /**
     * Gets the current time for the Physical Twin.
     * @return The value of the Physical Twin's clock.
     */
    public int getPTTime() {
        if (jedis.exists("PTnow")) {
            return Integer.parseInt(jedis.get("PTnow"));
        } else {
            return 0;
        }
    }

    /**
     * Gets the current time for the Digital Twin.
     * @return The value of the Digital Twin's clock.
     */
    public int getDTTime() {
        if (jedis.exists("DTnow")) {
            return Integer.parseInt(jedis.get("DTnow"));
        } else {
            return 0;
        }
    }

    /**
     * Advances the Digital Twin's time.
     * @param amount The number of milliseconds to advance.
     */
    public void advanceDTTime(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount must be non-negative");
        }
        jedis.incrBy("DTnow", amount);
    }

    /**
     * Returns the ID of the current execution.
     * @return The ID of the current execution.
     */
    public String getCurrentExecutionId() {
        if (jedis.exists("executionId")) {
            return jedis.get("executionId");
        } else {
            return null;
        }
    }

    /**
     * Returns the current value of the command counter.
     * @return The current value of the command counter.
     */
    public int getCommandCounter() {
        if (jedis.exists("commandCounter")) {
            return Integer.parseInt(jedis.get("commandCounter"));
        } else {
            return 0;
        }
    }

    /**
     * Puts a command in the Data Lake.
     * @param twinId The ID of the twin the command refers to.
     * @param target Whether to target the Physical Twin, the Digital Twin, or both.
     * @param command The name of the command to send.
     * @param args The arguments to send.
     * @return The ID of the new command.
     */
    public int putCommand(String twinId, CommandTarget target, String command, String[] args) {
        Map<String, String> hash = new HashMap<>();
        incrCommandCounter();
        StringJoiner argJoiner = new StringJoiner(" ", "", "");
        for (String arg : args) {
            argJoiner.add(arg);
        }
        String execId = getCurrentExecutionId();
        int commandId = getCommandCounter();
        String objectId = twinId + ":" + execId + ":" + commandId;

        hash.put("twinId", twinId);
        hash.put("executionId", getCurrentExecutionId());
        hash.put("name", command);
        hash.put("arguments", argJoiner.toString());
        hash.put("commandId", commandId + "");

        if (target.isPhysical) {
            jedis.hset("PTCommand:" + objectId, hash);
            jedis.zadd("PTCommand_UNPROCESSED", commandId, "PTCommand:" + objectId);
        }
        if (target.isDigital) {
            jedis.hset("DTCommand:" + objectId, hash);
            jedis.zadd("DTCommand_UNPROCESSED", commandId, "DTCommand:" + objectId);
        }
        return commandId;
    }

    private void incrCommandCounter() {
        jedis.incr("commandCounter");
    }

}
