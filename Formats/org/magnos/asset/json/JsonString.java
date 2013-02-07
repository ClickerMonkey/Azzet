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

/**
 * A String value.
 * 
 * @author Philip Diffenderfer
 *
 */
public class JsonString implements JsonValue
{

	private String value;

	/**
	 * Instantiates a new JsonString.
	 */
	public JsonString()
	{
	}
	
	/**
	 * Instantiates a new JsonString.
	 * 
	 * @param value
	 * 	The initial value.
	 */
	public JsonString( String value )
	{
		this.value = value;
	}

	/**
	 * Instantiates a new JsonString.
	 * 
	 * @param value
	 * 	The initial value.
	 */
	public JsonString( Object value )
	{
		this.value = value.toString();
	}

	/**
	 * Returns the current value.
	 * 
	 * @return The current value.
	 */
	public String get()
	{
		return value;
	}

	/**
	 * Sets the new value.
	 * 
	 * @param value
	 * 	The new value.
	 */
	public void set( String value )
	{
		this.value = value;
	}

	@Override
	public String getObject()
	{
		return value;
	}
	
	@Override
	public JsonType getType()
	{
		return JsonType.String;
	}

	@Override
	public String toJson()
	{
		return Json.toString( this );
	}

	@Override
	public void write( JsonWriter out ) throws IOException
	{
		out.writeString( value );
	}

}
