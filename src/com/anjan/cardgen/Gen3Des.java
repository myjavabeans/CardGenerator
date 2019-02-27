package com.anjan.cardgen;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import com.anjan.bean.Gen3DesBean;
import com.anjan.cardgen.properties.CardGenProperties;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class Gen3Des {
	
	private static Logger logger = Logger.getLogger(Gen3Des.class);
	
	private static String keyName = "";
	private static String keyPin = "";
	private static String deviceType = "";
	private static String inputString = "";
	private static String encDecType = "";
	private static String arcotHome = CardGenProperties.arcotHome;
	
	private static String convertDeviceType(String deviceType){
		
		if(deviceType != null && !deviceType.isEmpty()){
			
			if(deviceType.equals("hardware")){
				return "htest";
			}else if(deviceType.equals("software")){
				return "stest";
			}
			
		}
		
		return "";
		
	}
	
	private static String validateBean(Gen3DesBean bean){
		
		keyName = bean.getKeyName();
		keyPin = bean.getKeyPin();
		deviceType = bean.getDeviceType();
		inputString = bean.getInputString();
		encDecType = bean.getEncDecType();
		
		if(keyName != null && keyName.isEmpty()){
			logger.warn("Key Name cannot be Empty");
			return "Please Enter Key Name";
		}
		
		if(deviceType == null || (deviceType != null && deviceType.isEmpty())){
			logger.warn("Device Type cannot be Empty");
			return "Please select Device Type";
		}
		
		if(inputString != null && inputString.isEmpty()){
			logger.warn("Input String cannot be Empty");
			return "Please select Input String";
		}
		
		return "";
		
	}

	
	private static String executeCommand(String command){
		
		String output = "";
	
		JSch jSch = new JSch();
		try {
			Session session = jSch.getSession(CardGenProperties.userName, CardGenProperties.hostName, 22);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(CardGenProperties.password);
			session.connect();
			
			logger.info("Connected to HostName : "+CardGenProperties.hostName);
			
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			channel.setInputStream(null);
			((ChannelExec) channel).setErrStream(System.err);
			
			InputStream in = channel.getInputStream();
			channel.connect();
			
			byte[] tmp = new byte[4096];
			
			while(true){
				while(in.available() > 0){
					int i = in.read(tmp);
					
					if(i < 0)
						break;
					
					output = new String(tmp, 0, i); 
					logger.info("Output : "+output);
				}
				
				if(channel.isClosed()){
					logger.info("Channel is closed with Exit Status "+channel.getExitStatus());
					break;
				}
				
				try{Thread.sleep(1000);}catch(Exception ee){return ee.getMessage();}
				
			}
			
			channel.disconnect();
			session.disconnect();
			
			logger.info("Connection Closed...");
			
		} catch (JSchException e) {
			logger.error("JSch Exception ", e);
			return e.getMessage();
		} catch (IOException e) {
			logger.error("IOException ", e);
			return e.getMessage();
		}
		
		
		return output;
	}
	
	public static String decrypt(Gen3DesBean bean){
		
		logger.info("[KEYNAME - "+bean.getKeyName()+", PIN - "+bean.getKeyPin()+", DEVICE - "+bean.getDeviceType()+"]");
		
		String str = validateBean(bean);
		
		if(str != null && !str.isEmpty()){
			return str;
		}
		
		String command = CardGenProperties.arcotHome+"/bin/gen3des "+keyName+" "+keyPin+" -"+convertDeviceType(deviceType)+" -"+encDecType+" "+inputString;
		
		logger.info("Command Execution Started");
		
		String output = executeCommand(command);
		
		logger.info("Command Executed");
		
		return output;
	}
	
	public static String encrypt(Gen3DesBean bean){
		
		logger.info("[KEYNAME - "+bean.getKeyName()+", PIN - "+bean.getKeyPin()+", DEVICE - "+bean.getDeviceType()+"]");
		
		String str = validateBean(bean);
		
		if(str != null && !str.isEmpty()){
			return str;
		}

		String command = CardGenProperties.arcotHome+"/bin/gen3des "+keyName+" "+keyPin+" -"+convertDeviceType(deviceType)+" -"+encDecType+" "+inputString;
		
		logger.info("Command Execution Started");
		
		String output = executeCommand(command);
		
		logger.info("Command Executed");
		
		
		return output;
	}

}
