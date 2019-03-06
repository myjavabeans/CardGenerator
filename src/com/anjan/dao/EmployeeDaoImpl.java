package com.anjan.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.anjan.cardgen.properties.AliasToLinkedEntityMapTransformer;

@Repository
public class EmployeeDaoImpl {
	
	SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public List<Map<String, Object>> getAllEmployees(){
		
		Session session = sessionFactory.openSession();
		
		Query query = session.getNamedQuery("SQL_GET_ALL_EMPLOYEE");
		query.setResultTransformer(AliasToLinkedEntityMapTransformer.INSTANCE);
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = query.list();
		
		session.close();
		
		return list;
		
	}

}
