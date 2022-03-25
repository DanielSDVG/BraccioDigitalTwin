package pubsub;

import org.tzi.use.api.UseSystemApi;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import utils.DTLogger;

/**
 * 
 * @author Paula Muñoz - University of Málaga
 * 
 */
public class SubService implements Runnable {
	
	private final UseSystemApi api;
	private final JedisPool jedisPool;
	private final String subscribedChannel;
	
	/**
	 * Default constructor
	 * 
	 * @param api				USE system API instance to interact with the currently displayed object diagram.
	 * @param jedisPool			Jedis client pool, connected to the Data Lake
	 * @param subscribedChannel	Channel you want to subscribe to
	 */
	public SubService(UseSystemApi api, JedisPool jedisPool, String subscribedChannel) {
		this.api = api;
		this.jedisPool = jedisPool;
		this.subscribedChannel = subscribedChannel;
	}

	/**
	 * Subscribes to the publisher channel specified in the constructor.
	 */
	public void run() {
        try {
        	DTLogger.info("Subscribing to " + subscribedChannel);
        	Jedis jedisSubscriber = jedisPool.getResource();
        	Jedis jedisCrud = jedisPool.getResource();
        	jedisSubscriber.subscribe(new DTPubSub(api, jedisCrud), subscribedChannel);
		    jedisPool.returnResource(jedisSubscriber);
		    jedisPool.returnResource(jedisCrud);
			DTLogger.info("Subscription to " + subscribedChannel + " ended");
        } catch (Exception ex) {
            ex.printStackTrace();
        }    
    }

}
