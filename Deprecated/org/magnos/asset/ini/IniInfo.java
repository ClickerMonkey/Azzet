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

import java.nio.charset.Charset;

import org.magnos.asset.base.BaseAssetInfo;

/**
 * Information for loading an INI file.
 * 
 * @author Philip Diffenderfer
 *
 */
public class IniInfo extends BaseAssetInfo 
{

	/**
	 * The default character set used for loading a TXT file.
	 */
	public static final String DEFAULT_CHARSET = Charset.defaultCharset().name();
	
	
	private final String charsetName;
	
	/**
	 * Instantiates a new TextInfo.
	 */
	public IniInfo(Class<?> type)
	{
		this(type, DEFAULT_CHARSET);
	}
	
	/**
	 * Instantiates a new TextInfo.
	 * 
	 * @param charsetName
	 * 		The character set to load the TXT file in.
	 */
	public IniInfo(Class<?> type, String charsetName) 
	{
		super( type );
		
		this.charsetName = charsetName;
	}
	
	/**
	 * The character set of the TXT file.
	 * 
	 * @return
	 * 		The character set to load the TXT file in.
	 */
	public String getCharsetName()
	{
		return charsetName;
	}
	
}
