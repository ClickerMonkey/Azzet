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

package org.magnos.asset.info;

import java.nio.charset.Charset;

import org.magnos.asset.AssetInfo;
import org.magnos.asset.base.BaseAssetInfo;


/**
 * Information for loading a TXT file as a set of characters.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class CharacterInfo extends BaseAssetInfo
{

	/**
	 * The default character set of the characters.
	 */
	public static final String DEFAULT_CHARSET = Charset.defaultCharset().name();

	// properties
	public static final String[] PROPERTIES = {
		"charsetname"
	};
	
	private final String charsetName;

	/**
	 * Instantiates a new CharacterInfo.
	 */
	public CharacterInfo( Class<?> type )
	{
		this( type, DEFAULT_CHARSET );
	}

	/**
	 * Instantiates a new CharacterInfo.
	 * 
	 * @param charsetName
	 *        The character set of the characters.
	 */
	public CharacterInfo( Class<?> type, String charsetName )
	{
		super( type );

		this.charsetName = charsetName;
	}

	@Override
	protected Object getProperty( String name )
	{
		if (name.equals( "charsetname" )) return charsetName;

		return null;
	}
	
	@Override
	public boolean isMatch( AssetInfo info )
	{
		if ( info instanceof CharacterInfo )
		{
			CharacterInfo character = (CharacterInfo)info;
			
			return (
				character.charsetName.equals( charsetName )
			);
		}
		
		return false;
	}
	
	@Override
	public String[] getProperties()
	{
		return PROPERTIES;
	}

	/**
	 * The character set of the characters.
	 * 
	 * @return The character set of the characters.
	 */
	public String getCharsetName()
	{
		return charsetName;
	}

}
