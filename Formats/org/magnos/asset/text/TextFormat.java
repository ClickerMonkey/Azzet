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

package org.magnos.asset.text;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.CharBuffer;

import org.magnos.asset.AssetInfo;
import org.magnos.asset.FormatUtility;
import org.magnos.asset.base.BaseAssetFormat;
import org.magnos.asset.info.CharacterInfo;


/**
 * A format for loading {@link String} from TXT files.
 * 
 * <h2>Extensions</h2>
 * <ul>
 * <li>TXT - A Generic Text File</li>
 * </ul>
 * 
 * <h2>Request Types<h2>
 * <ul>
 * <li>{@link java.lang.String}</li>
 * <li>char[]</li>
 * <li>{@link java.nio.CharBuffer}</li>
 * <li>{@link java.lang.StringBuilder}</li>
 * <li>{@link java.lang.StringBuffer}</li>
 * </ul>
 * 
 * @author Philip Diffenderfer
 * 
 */
public class TextFormat extends BaseAssetFormat
{

	/**
	 * Instantiates a new TextFormat.
	 */
	public TextFormat()
	{
		super( new String[] { "txt" }, String.class, char[].class, CharBuffer.class, StringBuilder.class, StringBuffer.class );
	}

	@Override
	public AssetInfo getInfo( Class<?> type )
	{
		return new CharacterInfo( type );
	}

	@Override
	public Object loadAsset( InputStream input, AssetInfo assetInfo ) throws Exception
	{
		CharacterInfo info = (CharacterInfo)assetInfo;
		
		ByteArrayOutputStream output = FormatUtility.getOutput( input );
		String outputString = output.toString( info.getCharsetName() );
		
		Object asset = null;

		if (info.isType( String.class ))
		{
			asset = outputString;
		}
		else if (info.isType( char[].class ))
		{
			asset = outputString.toCharArray();
		}
		else if (info.isType( CharBuffer.class ))
		{
			asset = CharBuffer.wrap( outputString );
		}
		else if (info.isType( StringBuilder.class ))
		{
			asset = new StringBuilder( outputString );
		}
		else if (info.isType( StringBuffer.class ))
		{
			asset = new StringBuffer( outputString );
		}

		return asset;
	}

}
