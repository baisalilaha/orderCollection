package com.order.classes;

public class ConfigMapper{

	private static String DbHost;
	private static int DbPort;
	private static String DbUserName;
	private static String DbPassWord;
	private static int DbPoolSize;
	

	public static String getDbHost() {
		return DbHost;
	}
	public static void setDbHost(String dbHost) {
		DbHost = dbHost;
	}
	public static int getDbPort() {
		return DbPort;
	}
	public static void setDbPort(int dbPort) {
		DbPort = dbPort;
	}
	public static String getDbUserName() {
		return DbUserName;
	}
	public static void setDbUserName(String dbUserName) {
		DbUserName = dbUserName;
	}
	public static String getDbPassWord() {
		return DbPassWord;
	}
	public static void setDbPassWord(String dbPassWord) {
		DbPassWord = dbPassWord;
	}
	public static int getDbPoolSize() {
		return DbPoolSize;
	}
	public static void setDbPoolSize(int dbPoolSize) {
		DbPoolSize = dbPoolSize;
	}
}