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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import org.magnos.asset.io.CharacterSet;

/**
 * The main JSON class which contains constants and simple parsing methods.
 * 
 * @author Philip Diffenderfer
 *
 */
public class Json
{

	public static final char OBJECT_START = '{';
	public static final char OBJECT_END = '}';
	public static final char ARRAY_START = '[';
	public static final char ARRAY_END = ']';
	public static final char STRING_START = '"';
	public static final char STRING_END = '"';
	public static final char ARRAY_SEPARATOR = ',';
	public static final char MEMBER_SEPARATOR = ':';

	public static final String TRUE = "true";
	public static final String FALSE = "false";
	public static final String NULL = "null";

	public static final CharacterSet SET_STRING_STOP = new CharacterSet( STRING_END );
	public static final CharacterSet SET_CONSTANT_STOP = new CharacterSet( new char[][] { { '\0', ' ' }, { MEMBER_SEPARATOR, ARRAY_END, STRING_END, OBJECT_END, ARRAY_SEPARATOR } } );
	public static final CharacterSet SET_MEMBER_STOP = new CharacterSet( new char[][] { { '\0', ' ' }, { MEMBER_SEPARATOR } } );

	private static final JsonFormat format = new JsonFormat();

	public static JsonValue valueOf( String x ) throws IOException
	{
		return format.readValueFromStream( new StringReader( x ) );
	}

	public static <J extends JsonValue> J valueOf( String x, Class<J> expectedType ) throws IOException
	{
		return (J)format.readValueFromStream( new StringReader( x ) );
	}

	public static JsonValue valueOf( InputStream in ) throws IOException
	{
		return format.readValueFromStream( in );
	}

	public static JsonValue valueOf( byte[] x ) throws IOException
	{
		return format.readValueFromStream( new ByteArrayInputStream( x ) );
	}

	public static <J extends JsonValue> J valueOf( byte[] x, Class<J> expectedType ) throws IOException
	{
		return (J)format.readValueFromStream( new ByteArrayInputStream( x ) );
	}

	public static String toString( JsonValue value )
	{
		StringBuilder out = new StringBuilder();
		try
		{
			value.write( JsonWriter.forAppender( out ) );
		}
		catch (Exception e)
		{
			return null;
		}
		return out.toString();
	}

	public static String encode( CharSequence chars )
	{
		StringBuilder out = new StringBuilder();

		for (int i = 0; i < chars.length(); i++)
		{
			switch (chars.charAt( i ))
			{
			case '"':
				out.append( "\\\"" );
				break;
			case '\t':
				out.append( "\\t" );
				break;
			case '\n':
				out.append( "\\n" );
				break;
			case '\r':
				out.append( "\\r" );
				break;
			case '\f':
				out.append( "\\f" );
				break;
			case '\b':
				out.append( "\\b" );
				break;
			case '\\':
				out.append( "\\\\" );
				break;
			default:
				out.append( chars.charAt( i ) );
				break;
			}
		}

		return out.toString();
	}

}
