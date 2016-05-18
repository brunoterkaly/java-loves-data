// Copyright 2016, Bruno Terkaly, All Rights Reserved
// Open sourcing my code for my O'Reilly course.
// Cloud-Based Provisioning, Storage, and Data Retrieval with Java and Linux
// Learn more here http://bit.ly/bruno-does-linux-data-java
package com.terkaly.JavaRedis;

import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;


public class App 
{
    // Our connection to the Redis Cache in Azure
    private static JedisPool pool = new JedisPool(new JedisPoolConfig(),
            "azurecourse.redis.cache.windows.net",
                6379,
                Protocol.DEFAULT_TIMEOUT,
                "u6TCrcJTw8A3xD0U6OAWhuFcw3LX6LVFSr2MM/g1zjo=");
    // Jedis is a reference to the cache at runtime
    private static Jedis jedis = null;
    // This is a string that we wish to cache
    private static String firstName = null;


    public static void main( String[] args )
    {
          try {
	          // Connect to cache
	          jedis = pool.getResource();
	          
	          // Run this to startover  --->   jedis.flushAll();
	          // check cache
	          // if found, continue with lookup
	          // if not found, do physical lookup and put in
	          //   cache for fast future lookups
	          if(InCache("firstName") == false) {
	              String lookupValue = GetFirstNameFromDatabase();
	              System.out.println(
	                 "Data from time-consuming lookup, firstName = "
	                                                      + firstName);
	              PutInCache("firstName", lookupValue);
	          }
	          else {
	              System.out.println("Data from cache, firstName = "
	                                                     + firstName);
	          }
	          // when closing your application:
	          pool.destroy();          
          
          }
          catch (Exception e) {
              e.printStackTrace();
          }
          finally {
          }

    }
    private static boolean InCache(String key) {
        if(key == "firstName") {
            // Read from cache. Return true or false depending
            // if in cache
            firstName = jedis.get("firstName");
            return firstName != null;
        }
         return false;
    }
    // Simulates a time-consuming database call
    private static String GetFirstNameFromDatabase() {
        return "Bruno";
    }
    private static void PutInCache(String key, String value) {
        jedis.set(key, value);
    }    
}
