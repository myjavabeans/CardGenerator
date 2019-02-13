package com.anjan.cardgen.properties;

public class CardGenProperties {
	
	public static String limit;
	public static String clearCard;
	public static String filePath;

	public static void setLimit(String limit) {
		CardGenProperties.limit = limit;
	}

	public static void setClearCard(String clearCard) {
		CardGenProperties.clearCard = clearCard;
	}

	public static void setFilePath(String filePath) {
		CardGenProperties.filePath = filePath;
	}

}
