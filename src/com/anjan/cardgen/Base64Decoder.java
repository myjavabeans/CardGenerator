package com.anjan.cardgen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.anjan.bean.CardNumberBean;
import com.anjan.cardgen.properties.CardGenProperties;

public class Base64Decoder {
	
	private static Logger logger = Logger.getLogger(Base64Decoder.class);
	
	private static String getRandomFileName(int n) 
    { 
  
        // chose a Character random from this String 
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                    + "0123456789"
                                    + "abcdefghijklmnopqrstuvxyz"; 
  
        // create StringBuffer size of AlphaNumericString 
        StringBuilder sb = new StringBuilder(n); 
  
        for (int i = 0; i < n; i++) { 
  
            // generate a random number between 
            // 0 to AlphaNumericString variable length 
            int index 
                = (int)(AlphaNumericString.length() 
                        * Math.random()); 
  
            // add Character one by one in end of sb 
            sb.append(AlphaNumericString 
                          .charAt(index)); 
        } 
  
        return sb.toString(); 
    } 
	
	public static CardNumberBean decodeImage(String encStr, String extnType){
		
		CardNumberBean bean = new CardNumberBean();
		
		if(encStr==null || (encStr != null && encStr.isEmpty()) || (encStr != null && encStr.equalsIgnoreCase("null"))){
			logger.warn("Base64 String cannot be empty");
			bean.setMessage("Base64 String cannot be empty");
			bean.setFileLink("");
			return bean;
		}
		
		if(extnType==null || (extnType != null && extnType.isEmpty()) || (extnType != null && extnType.equalsIgnoreCase("null"))){
			logger.warn("File Extension cannot be empty");
			bean.setMessage("File Extension cannot be empty");
			bean.setFileLink("");
			return bean;
		}
		
		String filePath = CardGenProperties.filePath;
		
		File directory = new File(filePath);
		
		if(!directory.exists()){
			directory.mkdirs();
			logger.info("Directory Created - "+filePath);
		}else{
			logger.info("Directory already exists - "+filePath);
		}
		
		String fileLocation = filePath + "/" + getRandomFileName(8) + "." +extnType;
		logger.info("File Generated - "+fileLocation);
		
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(fileLocation);
			byte  byteArray[] = Base64.decodeBase64(encStr);
			//logger.info("Byte Array - "+new String(byteArray));
			fos.write(byteArray);
			fos.close();
		} catch (FileNotFoundException e) {
			logger.warn("File Not Found", e);
			bean.setMessage("File "+fileLocation+" not Found");
			bean.setFileLink("");
			return bean;
		} catch (IOException e) {
			logger.warn("IO Exception", e);
			bean.setMessage("IO Exception Occurred. For more information check Log file.");
			bean.setFileLink("");
			return bean;
		} 
		
		bean.setFileLink(fileLocation);
		bean.setMessage("");;
		return bean;
		
	}

}
