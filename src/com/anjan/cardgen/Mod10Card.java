package com.anjan.cardgen;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.anjan.bean.CardNumberBean;
import com.anjan.cardgen.properties.CardGenProperties;

public class Mod10Card {
	
	private static Logger logger = Logger.getLogger(Mod10Card.class);
	private static boolean clearCard = Boolean.parseBoolean(CardGenProperties.clearCard);
	private static int MIN_LEN = 12;
	private static int MAX_LEN = 19;

	private static String maskCardNumber(String cardNumber){
		
		if(cardNumber != null && !cardNumber.isEmpty()){
			cardNumber = cardNumber.substring(0, 4)+cardNumber.substring(4, cardNumber.length()-2).replaceAll("\\S", "*")+cardNumber.substring(cardNumber.length()-2);
			logger.info("Masked Card Number - "+cardNumber);
		}
		
		return cardNumber;
		
	}
	
	private static int[] getCardDigits(String cardNumber){
		
		int[] ints = new int[cardNumber.length()];
		for (int i = 0; i < cardNumber.length(); i++) {
			ints[i] = Integer.parseInt(cardNumber.substring(i, i + 1));
		}
		
		return ints;
		
	}
	
	private static int[] getDoubleCardDigits(int[] intC){
		
		for (int i = intC.length - 2; i >= 0; i = i - 2) {
			int j = intC[i];
			j = j * 2;
			if (j > 9) {
				j = j % 10 + 1;
			}
			intC[i] = j;
		}
		
		return intC;
		
	}
	
	private static int findSum(int[] intC){
		
		int sum = 0;
		for (int i = 0; i < intC.length; i++) {
			sum += intC[i];
		}
		
		return sum;
		
	}
	
	public static CardNumberBean validateCardNumber(String cardNumber){
		
		CardNumberBean bean = new CardNumberBean();
		
		String displayCard = cardNumber;
		
		if((cardNumber != null && cardNumber.isEmpty()) || cardNumber == null){
			bean.setMessage("Card Number cannot be Empty");
			logger.warn("Card Number cannot be Empty");
			return bean;
		}
		
		if(cardNumber != null && cardNumber.length()<MIN_LEN){
			bean.setMessage("Minimum length of Card Number should be "+MIN_LEN);
			logger.warn("Minimum length of Card Number should be "+MIN_LEN);
			return bean;
		}
		
		if(cardNumber != null && cardNumber.length()>MAX_LEN){
			bean.setMessage("Maximum length of Card Number should be "+MAX_LEN);
			logger.warn("Maximum length of Card Number should be "+MAX_LEN);
			return bean;
		}
		
		if(!clearCard){
			displayCard = maskCardNumber(cardNumber);
		}
		
		
		try{
			Long.parseLong(cardNumber);
		}catch(Exception e){
			bean.setMessage("Unable to parse Card Number - "+displayCard);
			logger.warn("Unable to parse Card Number - "+displayCard, e);
			return bean;
		}
		
		boolean flag = valCardNumber(cardNumber);
		
		if(flag){
			bean.setMessage("<b>"+displayCard+"</b> is a Valid Card Number");
			logger.warn(displayCard+" is a Valid Card Number");
		}else {
			bean.setMessage("<b>"+displayCard+"</b> is not a Valid Card Number");
			logger.warn(displayCard+" is not a Valid Card Number");
		}
	
		return bean;
	}
	
	private static boolean valCardNumber(String cardNumber){
		
		int[] cardNumArray = getCardDigits(cardNumber);
		cardNumArray = getDoubleCardDigits(cardNumArray);
		int sum = findSum(cardNumArray);
		
		if(sum % 10 == 0){
			return true;
		}else {
			return false;
		}
	
	}
	
	public static CardNumberBean getValidCardNumbers(String beginCardNumber, String endCardNumber){
		
		CardNumberBean bean = new CardNumberBean();
		List<String> cardNumbers = new ArrayList<String>();
		
		if(beginCardNumber == null || (beginCardNumber != null && beginCardNumber.isEmpty()) || endCardNumber == null || (endCardNumber != null && endCardNumber.isEmpty())){
			bean.setMessage("Begin Range or End Range cannot be Empty");
			logger.warn("Begin Range or End Range cannot be Empty");
			bean.setCardNumbers(cardNumbers);
			return bean;
		}
		
		if(beginCardNumber != null && endCardNumber != null && (beginCardNumber.length()<MIN_LEN || endCardNumber.length()<MIN_LEN)){
			bean.setMessage("Minimum length of Card Number should be "+MIN_LEN);
			logger.warn("Minimum length of Card Number should be "+MIN_LEN);
			bean.setCardNumbers(cardNumbers);
			return bean;
		}
		
		if(beginCardNumber != null && endCardNumber != null && beginCardNumber.length()>MAX_LEN || endCardNumber.length()>MAX_LEN){
			bean.setMessage("Maximum length of Card Number should be "+MAX_LEN);
			logger.warn("Maximum length of Card Number should be "+MAX_LEN);
			bean.setCardNumbers(cardNumbers);
			return bean;
		}
		
		int limit = Integer.parseInt(CardGenProperties.limit);
		
		logger.info("Limit of Card Generator - "+limit);
		
		String displayBeginCard = beginCardNumber;
		String displayEndCard = endCardNumber;
		long bCN = 0;
		long eCN = 0;
		
		if(!clearCard){
			displayBeginCard = maskCardNumber(beginCardNumber);
			displayEndCard = maskCardNumber(endCardNumber);
		}
		
		try{
			bCN = Long.parseLong(beginCardNumber);
		}catch(Exception e){
			bean.setMessage("Unable to parse Begin Range - "+displayBeginCard);
			logger.warn("Unable to parse Begin Range - "+displayBeginCard, e);
			bean.setCardNumbers(cardNumbers);
			return bean;
		}
		
		try{
			eCN = Long.parseLong(endCardNumber);
		}catch(Exception e){
			bean.setMessage("Unable to parse End Range - "+displayEndCard);
			logger.warn("Unable to parse End Range - "+displayEndCard, e);
			bean.setCardNumbers(cardNumbers);
			return bean;
		}
		
		if(eCN < bCN){
			bean.setMessage("End Range should be greater than Begin Range");
			logger.warn("End Range should be greater than Begin Range");
			bean.setCardNumbers(cardNumbers);
			return bean;
			
		}
		
		if((eCN - bCN) > limit){
			bean.setMessage("Range is more than allowed limit - "+limit);
			logger.warn("Range is more than allowed limit. Allowed Limit is "+limit);
			bean.setCardNumbers(cardNumbers);
			return bean;
		}
		
		for(long i = bCN; i<=eCN; i++){
			
			boolean flag = valCardNumber(i+"");
			
			if(flag){
				cardNumbers.add(i+"");
			}
			
		}
		
		if(cardNumbers.size() < 1){
			bean.setMessage("No Valid Card Number Found!!!");
			logger.warn("No Valid Card Number Found");
		}else{
			bean.setMessage("");
		}
		
		bean.setCardNumbers(cardNumbers);
		
		return bean;
	}
	
}
