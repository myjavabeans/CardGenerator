package com.anjan.bo;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.anjan.dao.EmployeeDaoImpl;

@Service
public class EmployeeBeanBOImpl {
	
	EmployeeDaoImpl employeeDao;
	
	public void setEmployeeDao(EmployeeDaoImpl employeeDao) {
		this.employeeDao = employeeDao;
	}

	public List<Map<String, Object>> getAllEmployees(){
		return employeeDao.getAllEmployees();
	}

}
