package com.order.classes;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

/**
 * MongoDB connection class
 */

public class DbConfig{
		
	private String mongoDbUri = "mongodb://"+ ConfigMapper.getDbUserName() + ":" + ConfigMapper.getDbPassWord() + "@" + ConfigMapper.getDbHost() 
									+ "/?authSource=orders&maxPoolSize=" + ConfigMapper.getDbPoolSize();
	
	public static MongoClient mongoClient = null;
	
	public static void configLoader(){
		try {
			PropertiesConfiguration config = new PropertiesConfiguration(new File("/var/frengo/OrderCollection/config.properties"));
			//Db Configs
			ConfigMapper.setDbHost(config.getString("db_host"));
			ConfigMapper.setDbPort(config.getInt("db_port"));
			ConfigMapper.setDbPassWord(config.getString("db_password"));
			ConfigMapper.setDbUserName(config.getString("db_username"));
			ConfigMapper.setDbPoolSize(config.getInt("db_poolsize"));
			
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	private DbConfig(){
		configLoader();
		MongoClientURI uri = new MongoClientURI(mongoDbUri);
		mongoClient = new MongoClient(uri);
	}
	
	public static MongoClient getDbInstance(){
		if(mongoClient == null){
			new DbConfig();
		}
		return mongoClient;
	}
}