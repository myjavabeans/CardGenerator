package com.anjan.bean;

import java.util.List;

/**
 * Card Number Bean
 * @author Anjan
 *
 */
public class CardNumberBean {
	
	private List<String> cardNumbers;
	private String message;
	private String beginRange;
	private String endRange;
	private String fileLink;
	
	public List<String> getCardNumbers() {
		return cardNumbers;
	}
	public void setCardNumbers(List<String> cardNumbers) {
		this.cardNumbers = cardNumbers;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getBeginRange() {
		return beginRange;
	}
	public void setBeginRange(String beginRange) {
		this.beginRange = beginRange;
	}
	public String getEndRange() {
		return endRange;
	}
	public void setEndRange(String endRange) {
		this.endRange = endRange;
	}
	public String getFileLink() {
		return fileLink;
	}
	public void setFileLink(String fileLink) {
		this.fileLink = fileLink;
	}

}
