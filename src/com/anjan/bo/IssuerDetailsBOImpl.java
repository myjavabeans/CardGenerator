package com.anjan.bo;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.anjan.dao.IssuerDetailsDaoImpl;

@Service
public class IssuerDetailsBOImpl {
	
	private IssuerDetailsDaoImpl issuerDao;

	public void setIssuerDao(IssuerDetailsDaoImpl issuerDao) {
		this.issuerDao = issuerDao;
	}

	public List<Map<String, Object>> getCalloutDetails(String bankdirname){
		return issuerDao.getCalloutDetails(bankdirname);
	}
	
	public List<Map<String, Object>> getRADetails(String bankdirname){
		return issuerDao.getRADetails(bankdirname);
	}
	
	public List<Map<String, Object>> getCAPDetails(String bankdirname){
		return issuerDao.getCAPDetails(bankdirname);
	}
	
	public List<Map<String, Object>> getContentDetails(String bankdirname){
		return issuerDao.getContentDetails(bankdirname);
	}
}
