package com.anjan.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.anjan.cardgen.properties.AliasToLinkedEntityMapTransformer;

@Repository
public class IssuerDetailsDaoImpl {

	SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public List<Map<String, Object>> getCalloutDetails(String bankdirname){
		
		Session session = sessionFactory.openSession();
		
		Query query = session.getNamedQuery("SQL_GET_CALLOUT_BY_BANKDIRNAME");
		query.setString("bankdirname", bankdirname);
		
		query.setResultTransformer(AliasToLinkedEntityMapTransformer.INSTANCE);
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = query.list();
		
		session.close();
		
		return list;
		
	}
	
	public List<Map<String, Object>> getRADetails(String bankdirname){
		
		Session session = sessionFactory.openSession();
		
		Query query = session.getNamedQuery("SQL_GET_RA_BY_BANKDIRNAME");
		query.setString("bankdirname", bankdirname);
		
		query.setResultTransformer(AliasToLinkedEntityMapTransformer.INSTANCE);
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = query.list();
		
		session.close();
		
		return list;
		
	}
	
	public List<Map<String, Object>> getCAPDetails(String bankdirname){
		
		Session session = sessionFactory.openSession();
		
		Query query = session.getNamedQuery("SQL_GET_CAP_BY_BANKDIRNAME");
		query.setString("bankdirname", bankdirname);
		
		query.setResultTransformer(AliasToLinkedEntityMapTransformer.INSTANCE);
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = query.list();
		
		session.close();
		
		return list;
		
	}
	
	public List<Map<String, Object>> getContentDetails(String bankdirname){
		
		Session session = sessionFactory.openSession();
		
		Query query = session.getNamedQuery("SQL_GET_CONTENT_BY_BANKDIRNAME");
		query.setString("bankdirname", bankdirname);
		
		query.setResultTransformer(AliasToLinkedEntityMapTransformer.INSTANCE);
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = query.list();
		
		session.close();
		
		return list;
		
	}
	
}
