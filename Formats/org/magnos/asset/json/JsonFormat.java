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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.magnos.asset.AssetInfo;
import org.magnos.asset.base.BaseAssetFormat;
import org.magnos.asset.info.CharacterInfo;
import org.magnos.asset.io.CharacterReader;


/**
 * A format for loading {@link JsonValue} from JS or JSON files.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class JsonFormat extends BaseAssetFormat
{

	/**
	 * Instantiates a new JsonFormat.
	 */
	public JsonFormat()
	{
		super( new String[] { "json", "js" }, JsonObject.class );
	}

	@Override
	public AssetInfo getInfo( Class<?> type )
	{
		return new JsonInfo();
	}

	@Override
	public JsonValue loadAsset( InputStream input, AssetInfo assetInfo ) throws Exception
	{
		JsonInfo info = (JsonInfo)assetInfo;

		return readValueFromStream( input, info.getCharsetName() );
	}

	public JsonValue readValueFromStream( InputStream in ) throws IOException
	{
		return readValueFromStream( in, CharacterInfo.DEFAULT_CHARSET );
	}

	public JsonValue readValueFromStream( InputStream in, String charsetName ) throws IOException
	{
		return readValueFromStream( new BufferedReader( new InputStreamReader( in, charsetName ) ) );
	}

	public JsonValue readValueFromStream( Reader reader ) throws IOException
	{
		return readValue( new CharacterReader( reader ), true );
	}

	private JsonValue readValue( CharacterReader in, boolean readNext ) throws IOException
	{
		if (readNext)
		{
			in.readPastWhitespace();
		}

		switch (in.data)
		{
		case CharacterReader.NO_DATA:
			return null;
		case Json.ARRAY_START:
			return readArray( in );
		case Json.OBJECT_START:
			return readObject( in );
		case Json.STRING_START:
			return new JsonString( in.readUntil( Json.SET_STRING_STOP, false, true, false ) );
		}

		String valueString = in.readUntil( Json.SET_CONSTANT_STOP, true, true, true );

		if (valueString.equals( Json.NULL ))
		{
			return JsonNull.INSTANCE;
		}

		JsonValue value = JsonBoolean.fromString( valueString );

		if (value == null)
		{
			value = JsonNumber.fromString( valueString );
		}

		if (value == null)
		{
			value = new JsonString( valueString );
		}

		return value;
	}

	private JsonArray readArray( CharacterReader in ) throws IOException
	{
		List<JsonValue> valueList = new ArrayList<JsonValue>();

		while (in.readPastWhitespace() != CharacterReader.NO_DATA && in.data != Json.ARRAY_END)
		{
			if (in.data == Json.ARRAY_SEPARATOR)
			{
				continue;
			}

			valueList.add( readValue( in, false ) );
		}

		return new JsonArray( valueList );
	}

	private JsonObject readObject( CharacterReader in ) throws IOException
	{
		JsonObject object = new JsonObject();
		int data;

		while (in.readPastWhitespace() != CharacterReader.NO_DATA && in.data != Json.OBJECT_END)
		{
			if (in.data == Json.ARRAY_SEPARATOR)
			{
				continue;
			}

			String name = null;

			if (in.data == Json.STRING_START)
			{
				name = in.readUntil( Json.SET_STRING_STOP, false, true, false );
			}
			else
			{
				name = in.readUntil( Json.SET_MEMBER_STOP, true, true, true );
			}

			if (name.isEmpty())
			{
				throw new IOException( "Empty member key" + in.getCursor() );
			}

			data = in.readPastWhitespace();

			if (data != Json.MEMBER_SEPARATOR)
			{
				throw new IOException( "Expected member separator ':'" + in.getCursor() );
			}

			JsonValue value = readValue( in, true );

			object.set( name, value );
		}

		return object;
	}

}
