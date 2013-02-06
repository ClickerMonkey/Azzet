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

package org.magnos.asset.csv;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;


/**
 * A value that is the intersection between a row and a column in a table.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class Cell
{

	// A map of primitive classes to their Object counterparts.
	private static final Map<Class<?>, Class<?>> classMap;

	static
	{
		classMap = new HashMap<Class<?>, Class<?>>();
		classMap.put( boolean.class, Boolean.class );
		classMap.put( char.class, Character.class );
		classMap.put( byte.class, Byte.class );
		classMap.put( short.class, Short.class );
		classMap.put( int.class, Integer.class );
		classMap.put( long.class, Long.class );
		classMap.put( float.class, Float.class );
		classMap.put( double.class, Double.class );
	}

	// The value of the cell.
	private String x;

	/**
	 * Instantiates a new Cell.
	 * 
	 * @param x
	 *        The value of the cell.
	 */
	public Cell( String x )
	{
		this.x = x;
	}

	/**
	 * Sets the value of the cell.
	 * 
	 * @param x
	 *        The value of the cell.
	 */
	public void set( String x )
	{
		this.x = x;
	}

	/**
	 * Gets the value of the cell.
	 * 
	 * @return The reference to the value of the cell.
	 */
	public String get()
	{
		return x;
	}

	/**
	 * Determines if the value in this cell can be represented as the given
	 * class. If true is returned then this cell's value is valid for the given
	 * class. Some classes however will always return true (i.e. Boolean) because
	 * of their implementation of the static valueOf method or the constructor of
	 * the class.
	 * 
	 * @param cls
	 *        The class to to validate against.
	 * @return True if this cells value
	 */
	public boolean is( Class<?> cls )
	{
		return (get( cls ) != null);
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
	 *        The parsed type to return.
	 * @param cls
	 *        The class to validate against.
	 * @return The parsed value or null if this cells value was invalid.
	 */
	public <T> T get( Class<?> cls )
	{
		// If its a primitive, convert it.
		if (cls.isPrimitive())
		{
			cls = classMap.get( cls );
		}

		// Try accessing the static valueOf method if one exists.
		try
		{
			Method m = cls.getMethod( "valueOf", String.class );
			Object result = m.invoke( null, x );

			if (result != null)
			{
				return (T)result;
			}
		}
		// Ignore exceptions
		catch (Exception e)
		{
		}

		// Try a constructor that accepts the cells value.
		try
		{
			Constructor<?> c = cls.getConstructor( String.class );
			Object result = c.newInstance( x );

			if (result != null)
			{
				return (T)result;
			}
		}
		// Ignore exceptions
		catch (Exception e)
		{
		}

		// Failed attempt at parsing.
		return null;
	}

	/**
	 * Returns the Boolean representation of this cell.
	 * 
	 * @return The Boolean value or null if the cell is empty or null.
	 */
	public Boolean getBoolean()
	{
		return (isNull() ? null : Boolean.valueOf( x ));
	}

	/**
	 * Returns the Byte representation of this cell.
	 * 
	 * @return The Byte value or null if the cell is empty or null.
	 */
	public Byte getByte()
	{
		return (isNull() ? null : Byte.valueOf( x ));
	}

	/**
	 * Returns the Short representation of this cell.
	 * 
	 * @return The Short value or null if the cell is empty or null.
	 */
	public Short getShort()
	{
		return (isNull() ? null : Short.valueOf( x ));
	}

	/**
	 * Returns the Integer representation of this cell.
	 * 
	 * @return The Integer value or null if the cell is empty or null.
	 */
	public Integer getInt()
	{
		return (isNull() ? null : Integer.valueOf( x ));
	}

	/**
	 * Returns the Long representation of this cell.
	 * 
	 * @return The Long value or null if the cell is empty or null.
	 */
	public Long getLong()
	{
		return (isNull() ? null : Long.valueOf( x ));
	}

	/**
	 * Returns the Float representation of this cell.
	 * 
	 * @return The Float value or null if the cell is empty or null.
	 */
	public Float getFloat()
	{
		return (isNull() ? null : Float.valueOf( x ));
	}

	/**
	 * Returns the Double representation of this cell.
	 * 
	 * @return The Double value or null if the cell is empty or null.
	 */
	public Double getDouble()
	{
		return (isNull() ? null : Double.valueOf( x ));
	}

	/**
	 * Returns the BigDecimal representation of this cell.
	 * 
	 * @return The BigDecimal value or null if the cell is empty or null.
	 */
	public BigDecimal getBigDecimal()
	{
		return (isNull() ? null : new BigDecimal( x ));
	}

	/**
	 * Returns the BigInteger representation of this cell.
	 * 
	 * @return The BigInteger value or null if the cell is empty or null.
	 */
	public BigInteger getBigInteger()
	{
		return (isNull() ? null : new BigInteger( x ));
	}

	/**
	 * Determines if this cell's value is null or empty.
	 * 
	 * @return True if this cell's value is null or an empty string.
	 */
	public boolean isNull()
	{
		return (x == null || x.isEmpty());
	}

	public String toString()
	{
		return (x == null ? "null" : x.toString());
	}

	public int hashCode()
	{
		return (x == null ? super.hashCode() : x.hashCode());
	}

	public boolean equals( Object o )
	{
		return (x == null ? super.equals( o ) : x.equals( o ));
	}

}
