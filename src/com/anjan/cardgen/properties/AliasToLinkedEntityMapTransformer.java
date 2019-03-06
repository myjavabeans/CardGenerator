package com.anjan.cardgen.properties;

import java.util.LinkedHashMap;
import java.util.Map;

import org.hibernate.transform.AliasedTupleSubsetResultTransformer;

public class AliasToLinkedEntityMapTransformer extends AliasedTupleSubsetResultTransformer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final AliasToLinkedEntityMapTransformer INSTANCE = new AliasToLinkedEntityMapTransformer();

	/**
	 * Disallow instantiation of AliasToEntityMapResultTransformer.
	 */
	private AliasToLinkedEntityMapTransformer() {
	}

	@Override
	public Object transformTuple(Object[] tuple, String[] aliases) {
		Map<String, Object> result = new LinkedHashMap<String, Object>(tuple.length);
		for ( int i=0; i<tuple.length; i++ ) {
			String alias = aliases[i];
			if ( alias!=null ) {
				result.put( alias, tuple[i] );
			}
		}
		return result;
	}

	@Override
	public boolean isTransformedValueATupleElement(String[] aliases, int tupleLength) {
		return false;
	}

	/**
	 * Serialization hook for ensuring singleton uniqueing.
	 *
	 * @return The singleton instance : {@link #INSTANCE}
	 */
	private Object readResolve() {
		return INSTANCE;
	}
	
}
