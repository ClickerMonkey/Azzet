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

package org.magnos.asset.json;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;


/**
 * An array of {@link JsonValue}s.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class JsonArray implements JsonValue
{

	private JsonValue[] values;

	/**
	 * Instantiates a new JsonArray.
	 */
	public JsonArray()
	{
	}

	/**
	 * Instantiates a new JsonArray.
	 * 
	 * @param values
	 *        The initial values in the array.
	 */
	public JsonArray( JsonValue... values )
	{
		this.values = values;
	}

	/**
	 * Instantiates a new JsonArray.
	 * 
	 * @param collection
	 *        The initial values in the array.
	 */
	public JsonArray( Collection<JsonValue> collection )
	{
		this.values = collection.toArray( new JsonValue[collection.size()] );
	}

	/**
	 * Gets the JsonValue at the given index if it has the given type and
	 * automatically casts it to the expected type.
	 * 
	 * @param <T>
	 *        The JsonValue type to be returned.
	 * @param index
	 *        The index of the JsonValue in this array.
	 * @param type
	 *        The JsonType to validate against. If the value at the given index
	 *        does not have this type then null is returned.
	 * @return The expected JsonValue or null if the JsonValue at the given index
	 *         has a different type.
	 * @throws ClassCastException
	 *         The expected type and actual type do not match.
	 */
	public <T extends JsonValue> T getValue( int index, JsonType type )
	{
		if (values[index].getType() == type)
		{
			return (T)values[index];
		}

		return null;
	}

	/**
	 * Gets the JsonValue at the given index if it has the given type.
	 * 
	 * @param <T>
	 *        The JsonValue type to be returned.
	 * @param index
	 *        The index of the JsonValue in this array.
	 * @param type
	 *        The expected type.
	 * @return The expected JsonValue or null if the JsonValue at the given index
	 *         has a different type.
	 * @throws ClassCastException
	 *         The expected type and actual type do not match.
	 */
	public <T extends JsonValue> T getValue( int index, Class<T> type )
	{
		if (values[index].getClass() == type)
		{
			return (T)values[index];
		}

		return null;
	}

	/**
	 * Gets the value of the JsonValue at the given index and casts it to any
	 * type.
	 * 
	 * @param <T>
	 *        The type to automatically cast the value to.
	 * @param index
	 *        The index of the JsonValue in this array.
	 * @return The value at the given index.
	 * @throws ClassCastException
	 *         The expected type and actual type do not match.
	 */
	public <T> T get( int index )
	{
		return (T)values[index].getObject();
	}

	/**
	 * Gets the value of the JsonValue at the given index and casts it to the
	 * specified type.
	 * 
	 * @param <T>
	 *        The type to automatically cast the value to.
	 * @param index
	 *        The index of the JsonValue in this array.
	 * @param type
	 *        The expected type.
	 * @return The value at the given index.
	 * @throws ClassCastException
	 *         The expected type and actual type do not match.
	 */
	public <T> T get( int index, Class<T> type )
	{
		return (T)values[index].getObject();
	}

	/**
	 * Tries to get and cast the value at the given index to the expected type
	 * and if that fails the defaultValue is returned.
	 * 
	 * @param <T>
	 *        The type to automatically cast the value to.
	 * @param index
	 *        The index of the JsonValue in this array.
	 * @param defaultValue
	 *        The defaultValue to return if the value in the array does not have
	 *        the expected type.
	 * @return The value at the given index.
	 */
	public <T> T get( int index, T defaultValue )
	{
		try
		{
			return (T)values[index].getObject();
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}

	/**
	 * Tries to get and cast the value at the given index to the expected type
	 * and if that fails the defaultValue is returned.
	 * 
	 * @param <T>
	 *        The type to automatically cast the value to.
	 * @param index
	 *        The index of the JsonValue in this array.
	 * @param defaultValue
	 *        The defaultValue to return if the value in the array does not have
	 *        the expected type.
	 * @param type
	 *        The expected type.
	 * @return The value at the given index.
	 */
	public <T> T get( int index, T defaultValue, Class<T> type )
	{
		try
		{
			return (T)values[index].getObject();
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}

	/**
	 * Adds the JsonValue to the end of this array.
	 * 
	 * @param value
	 *        The JsonValue to add.
	 */
	public void add( JsonValue value )
	{
		final int length = values.length;
		values = Arrays.copyOf( values, length + 1 );
		values[length] = value;
	}

	/**
	 * Adds the array of JsonValues to the end of this array.
	 * 
	 * @param valueArray
	 *        The array of JsonValues to add.
	 */
	public void add( JsonValue... valueArray )
	{
		final int length = values.length;
		values = Arrays.copyOf( values, length + valueArray.length );
		System.arraycopy( valueArray, 0, values, length, valueArray.length );
	}

	/**
	 * Returns the type of the JsonValue at the given index.
	 * 
	 * @param index
	 *        The index of the JsonValue to return the type of.
	 * @return The type of the JsonValue at the given index.
	 */
	public JsonType getType( int index )
	{
		return values[index].getType();
	}

	/**
	 * @return The reference to the internal array of JsonValues.
	 */
	public JsonValue[] values()
	{
		return values;
	}

	/**
	 * @return The number of JsonValues in this array.
	 */
	public int length()
	{
		return values.length;
	}

	@Override
	public JsonValue[] getObject()
	{
		return values;
	}

	@Override
	public JsonType getType()
	{
		return JsonType.Array;
	}

	@Override
	public String toJson()
	{
		return Json.toString( this );
	}

	@Override
	public void write( JsonWriter out ) throws IOException
	{
		out.startArray();

		for (int i = 0; i < values.length; i++)
		{
			if (i > 0)
			{
				out.writeArraySeparator();
			}

			values[i].write( out );
		}

		out.endArray();
	}

}
