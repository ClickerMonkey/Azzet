/* 
 * NOTICE OF LICENSE
 * 
 * This source file is subject to the Open Software License (OSL 3.0) that is 
 * bundled with this package in the file LICENSE.txt. It is also available 
 * through the world-wide-web at http://opensource.org/licenses/osl-3.0.php
 * If you did not receive a copy of the license and are unable to obtain it 
 * through the world-wide-web, please send an email to magnos.software@gmail.com 
 * so we can send you a copy immediately. If you use any of this software please
 * notify me via our website or email, your feedback is much appreciated. 
 * 
 * @copyright   Copyright (c) 2011 Magnos Software (http://www.magnos.org)
 * @license     http://opensource.org/licenses/osl-3.0.php
 * 				Open Software License (OSL 3.0)
 */

package org.magnos.asset.ini;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Philip Diffenderfer
 *
 */
public class Section extends HashMap<String, String> 
{
	
	private static final long serialVersionUID = 1L;
	
	// A map of primitive classes to their Object counterparts.
	private static final Map<Class<?>, Class<?>> classMap;
	
	static {
		classMap = new HashMap<Class<?>, Class<?>>();
		classMap.put(boolean.class, Boolean.class);
		classMap.put(char.class, Character.class);
		classMap.put(byte.class, Byte.class);
		classMap.put(short.class, Short.class);
		classMap.put(int.class, Integer.class);
		classMap.put(long.class, Long.class);
		classMap.put(float.class, Float.class);
		classMap.put(double.class, Double.class);
	}
	
	
	//
	private final String name;

	/**
	 * 
	 * @param name
	 */
	public Section(String name)
	{
		this.name = name;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public boolean has(String name)
	{
		return containsKey(name);
	}
	
	/**
	 * Determines if the value in this cell can be represented as the
	 * given class. If true is returned then this cell's value is valid
	 * for the given class. Some classes however will always return true
	 * (i.e. Boolean) because of their implementation of the static valueOf
	 * method or the constructor of the class.
	 * 
	 * @param cls
	 * 		The class to to validate against.
	 * @return
	 * 		True if this cells value
	 */
	public boolean is(String name, Class<?> cls) 
	{
		return (get(name, cls) != null);
	}

	/**
	 * Parses this cells value and returns the requested class. If the value of
	 * the cell is not valid for the class given null will be returned.
	 * 
	 * This method uses reflection, which can hinder performance if called 
	 * thousands of times in a short period of time. On average only a few 
	 * milliseconds will accumulate over thousands of calls.
	 * 
	 * @param <T>
	 * 		The parsed type to return.
	 * @param cls
	 * 		The class to validate against.
	 * @return
	 * 		The parsed value or null if this cells value was invalid.
	 */
	public <T> T get(String name, Class<?> cls)
	{
		String x = get(name);
		
		// If its a primitive, convert it.
		if (cls.isPrimitive()) 
		{
			cls = classMap.get(cls);
		}
		
		// Try accessing the static valueOf method if one exists.
		try 
		{
			Method m = cls.getMethod("valueOf", String.class);
			Object result = m.invoke(null, x);
			
			if (result != null) 
			{
				return (T)result;
			}
		}
		// Ignore exceptions
		catch (Exception e) { }
		
		// Try a constructor that accepts the cells value.
		try 
		{
			Constructor<?> c = cls.getConstructor(String.class);
			Object result = c.newInstance(x);
			
			if (result != null) 
			{
				return (T)result;
			}
		}
		// Ignore exceptions
		catch (Exception e) { }
		
		// Failed attempt at parsing.
		return null;
	}
	
	/**
	 * Returns the Boolean representation of this cell.
	 * 
	 * @return
	 * 		The Boolean value or null if the cell is empty or null.
	 */
	public Boolean getBoolean(String name) 
	{
		String x = get(name);
		return (isNull(x) ? null : Boolean.valueOf(x));
	}
	
	/**
	 * Returns the Byte representation of this cell.
	 * 
	 * @return
	 * 		The Byte value or null if the cell is empty or null.
	 */
	public Byte getByte(String name) 
	{
		String x = get(name);
		return (isNull(x) ? null : Byte.valueOf(x));
	}
	
	/**
	 * Returns the Short representation of this cell.
	 * 
	 * @return
	 * 		The Short value or null if the cell is empty or null.
	 */
	public Short getShort(String name) 
	{
		String x = get(name);
		return (isNull(x) ? null : Short.valueOf(x));
	}
	
	/**
	 * Returns the Integer representation of this cell.
	 * 
	 * @return
	 * 		The Integer value or null if the cell is empty or null.
	 */
	public Integer getInt(String name) 
	{
		String x = get(name);
		return (isNull(x) ? null : Integer.valueOf(x));
	}
	
	/**
	 * Returns the Long representation of this cell.
	 * 
	 * @return
	 * 		The Long value or null if the cell is empty or null.
	 */
	public Long getLong(String name) 
	{
		String x = get(name);
		return (isNull(x) ? null : Long.valueOf(x));
	}
	
	/**
	 * Returns the Float representation of this cell.
	 * 
	 * @return
	 * 		The Float value or null if the cell is empty or null.
	 */
	public Float getFloat(String name) 
	{
		String x = get(name);
		return (isNull(x) ? null : Float.valueOf(x));
	}
	
	/**
	 * Returns the Double representation of this cell.
	 * 
	 * @return
	 * 		The Double value or null if the cell is empty or null.
	 */
	public Double getDouble(String name) 
	{
		String x = get(name);
		return (isNull(x) ? null : Double.valueOf(x));
	}
	
	/**
	 * Returns the BigDecimal representation of this cell.
	 * 
	 * @return
	 * 		The BigDecimal value or null if the cell is empty or null.
	 */
	public BigDecimal getBigDecimal(String name) 
	{
		String x = get(name);
		return (isNull(name) ? null : new BigDecimal(x));
	}
	
	/**
	 * Returns the BigInteger representation of this cell.
	 * 
	 * @return
	 * 		The BigInteger value or null if the cell is empty or null.
	 */
	public BigInteger getBigInteger(String name) 
	{
		String x = get(name);
		return (isNull(x) ? null : new BigInteger(x));
	}
	
	/**
	 * Determines if this cell's value is null or empty.
	 * 
	 * @return
	 * 		True if this cell's value is null or an empty string.
	 */
	private boolean isNull(String value) 
	{
		return (value == null || value.isEmpty());
	}
	
}
