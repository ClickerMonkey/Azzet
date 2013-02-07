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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;


/**
 * A map of {@link JsonValue}s by a name.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class JsonObject implements JsonValue
{

	private final Map<String, JsonValue> valueMap;

	/**
	 * Instantiates a new JsonObject.
	 */
	public JsonObject()
	{
		this( new LinkedHashMap<String, JsonValue>() );
	}

	/**
	 * Instantiates a new JsonObject.
	 * 
	 * @param valueMap
	 *        The initial map of JsonValues.
	 */
	public JsonObject( Map<String, JsonValue> valueMap )
	{
		this.valueMap = valueMap;
	}

	/**
	 * Returns the JsonValue with the given name.
	 * 
	 * @param name
	 *        The name of the JsonValue. Case sensitive.
	 * @return The JsonValue with the given name or null if none exists.
	 */
	public JsonValue getValue( String name )
	{
		return valueMap.get( name );
	}

	/**
	 * Returns the JsonValue with the given name casted to the expected type.
	 * 
	 * @param <V>
	 *        The type to cast the JsonValue to.
	 * @param name
	 *        The name of the JsonValue. Case sensitive.
	 * @param expectedType
	 *        The expected JsonValue type.
	 * @return The JsonValue with the given name or null if none exists.
	 * @throws ClassCastException
	 *         The expected type and actual type do not match.
	 */
	public <V extends JsonValue> V getValue( String name, Class<V> expectedType )
	{
		return (V)valueMap.get( name );
	}

	/**
	 * Returns the JsonType for the type with the given name.
	 * 
	 * @param name
	 *        The name of the JsonValue. Case sensitive.
	 * @return The JsonType of the JsonValue with the given name, or null if no
	 *         value exists with the given name.
	 */
	public JsonType getType( String name )
	{
		JsonValue value = valueMap.get( name );

		return (value == null ? null : value.getType());
	}

	/**
	 * Returns the Object of the JsonValue with the given name.
	 * 
	 * @param name
	 *        The name of the JsonValue. Case sensitive.
	 * @return The object of the JsonValue or null if no value exists with the
	 *         given name.
	 */
	public Object getObject( String name )
	{
		JsonValue value = valueMap.get( name );

		return (value == null ? null : value.getObject());
	}

	/**
	 * Returns the value of the JsonValue with the given name.
	 * 
	 * @param <T>
	 *        The type to automatically cast the object to.
	 * @param name
	 *        The name of the JsonValue. Case sensitive.
	 * @return The value of the JsonValue with the given name, or null if it
	 *         doesn't exist.
	 * @throws ClassCastException
	 *         The expected type and actual type do not match.
	 */
	public <T> T get( String name )
	{
		JsonValue value = valueMap.get( name );

		return (value == null ? null : (T)value.getObject());
	}

	/**
	 * Returns the value of the JsonValue with the given name.
	 * 
	 * @param <T>
	 *        The type to automatically cast the object to.
	 * @param name
	 *        The name of the JsonValue. Case sensitive.
	 * @param type
	 *        The qualified type to return.
	 * @return The value of the JsonValue with the given name, or null if it
	 *         doesn't exist.
	 * @throws ClassCastException
	 *         The expected type and actual type do not match.
	 */
	public <T> T get( String name, Class<T> type )
	{
		JsonValue value = valueMap.get( name );

		return (value == null ? null : (T)value.getObject());
	}

	/**
	 * Tries to get and cast the value with the given name to the expected type
	 * and if that fails the defaultValue is returned.
	 * 
	 * @param <T>
	 *        The type to automatically cast the value to.
	 * @param name
	 *        The name of the JsonValue. Case sensitive.
	 * @param defaultValue
	 *        The defaultValue to return if the value in the object does not have
	 *        the expected type.
	 * @return The value with the given name.
	 */
	public <T> T get( String name, T defaultValue )
	{
		try
		{
			return (T)valueMap.get( name ).getObject();
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}

	/**
	 * Tries to get and cast the value with the given name to the expected type
	 * and if that fails the defaultValue is returned.
	 * 
	 * @param <T>
	 *        The type to automatically cast the value to.
	 * @param name
	 *        The name of the JsonValue. Case sensitive.
	 * @param defaultValue
	 *        The defaultValue to return if the value in the object does not have
	 *        the expected type.
	 * @param type
	 *        The qualified type to return.
	 * @return The value with the given name.
	 */
	public <T> T get( String name, T defaultValue, Class<T> type )
	{
		try
		{
			return (T)valueMap.get( name ).getObject();
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}

	/**
	 * Returns whether this object has a value with the given name.
	 * 
	 * @param name
	 *        The name of the value. Case sensitive.
	 * @return True if this object has a value with the given name, otherwise
	 *         false.
	 */
	public boolean has( String name )
	{
		return valueMap.containsKey( name );
	}

	/**
	 * Sets the value in this Object.
	 * 
	 * @param name
	 *        The name of the value.
	 * @param value
	 *        The value.
	 */
	public void set( String name, JsonValue value )
	{
		valueMap.put( name, value );
	}

	/**
	 * Returns the number of values in this object.
	 * 
	 * @return The number of values in this object.
	 */
	public int size()
	{
		return valueMap.size();
	}

	@Override
	public Map<String, JsonValue> getObject()
	{
		return valueMap;
	}

	@Override
	public JsonType getType()
	{
		return JsonType.Object;
	}

	@Override
	public String toJson()
	{
		return Json.toString( this );
	}

	@Override
	public void write( JsonWriter out ) throws IOException
	{
		out.startObject();

		boolean requiresSeparator = false;

		for (Entry<String, JsonValue> entry : valueMap.entrySet())
		{
			final String name = entry.getKey();
			final JsonValue value = entry.getValue();

			if (requiresSeparator)
			{
				out.writeMemberSeparator();
			}

			out.writeName( name );
			out.write( Json.MEMBER_SEPARATOR );

			value.write( out );

			requiresSeparator = true;
		}

		out.endObject();
	}

}
