package com.meuge.geolocalisation;

import java.util.Comparator;

public class CollectionComparator implements Comparator<Object> {

	@Override
	public int compare(Object lhs, Object rhs) {
		if (lhs instanceof KmsCalcules && rhs instanceof KmsCalcules)
		{
			KmsCalcules tmp1 = (KmsCalcules) lhs;
			KmsCalcules tmp2 = (KmsCalcules) rhs;
			return tmp1.compareTo(tmp2);
		}
		return 0;
	}


}
