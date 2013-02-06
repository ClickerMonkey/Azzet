package org.magnos.asset.server.util;

import java.util.Comparator;

/**
 * A comparator that uses hashCode to compare objects. 
 * 
 * @author Philip Diffenderfer
 *
 */
public class HashcodeComparator implements Comparator<Object> 
{

	@Override
	public int compare(Object o1, Object o2) 
	{
		return o1.hashCode() - o2.hashCode();
	}

}
