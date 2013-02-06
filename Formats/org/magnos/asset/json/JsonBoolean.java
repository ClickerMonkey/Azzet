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
 * A true or false value.
 * 
 * @author Philip Diffenderfer
 *
 */
public class JsonBoolean implements JsonValue
{

	private boolean value;

	/**
	 * Instantiates a new JsonBoolean.
	 * 
	 * @param value
	 * 	The initial value.
	 */
	public JsonBoolean( boolean value )
	{
		this.value = value;
	}

	/**
	 * @return The current value.
	 */
	public boolean get()
	{
		return value;
	}

	/**
	 * Sets the new value.
	 * 
	 * @param value
	 * 	The new value.
	 */
	public void set( boolean value )
	{
		this.value = value;
	}

	@Override
	public Boolean getObject()
	{
		return value ? Boolean.TRUE : Boolean.FALSE;
	}
	
	@Override
	public JsonType getType()
	{
		return JsonType.Boolean;
	}

	@Override
	public String toJson()
	{
		return value ? Json.TRUE : Json.FALSE;
	}

	@Override
	public void write( JsonWriter out ) throws IOException
	{
		out.write( value ? Json.TRUE : Json.FALSE );
	}

	public static JsonBoolean fromString( String x )
	{
		if (x.equals( Json.TRUE ))
		{
			return new JsonBoolean( true );
		}
		if (x.equals( Json.FALSE ))
		{
			return new JsonBoolean( false );
		}

		return null;
	}

}
