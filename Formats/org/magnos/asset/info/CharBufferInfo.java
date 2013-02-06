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

import java.nio.CharBuffer;


/**
 * Information for loading a TXT file as a CharBuffer.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class CharBufferInfo extends CharacterInfo
{

	/**
	 * The default value for whether the CharBuffer is direct.
	 */
	public static final boolean DEFAULT_DIRECT = false;

	// If the CharBuffer is Direct, if not it is Heap.
	private final boolean direct;

	/**
	 * Instantiates a new CharBufferInfo.
	 */
	public CharBufferInfo()
	{
		this( DEFAULT_CHARSET, DEFAULT_DIRECT );
	}

	/**
	 * Instantiates a new CharBufferInfo.
	 * 
	 * @param charsetName
	 *        The character set of the String.
	 */
	public CharBufferInfo( String charsetName )
	{
		this( charsetName, DEFAULT_DIRECT );
	}

	/**
	 * Instantiates a new CharBufferInfo.
	 * 
	 * @param direct
	 *        True if the CharBuffer is allocated directly in memory outside of
	 *        the JVM, or false if the CharBuffer is allocated to the Heap.
	 */
	public CharBufferInfo( boolean direct )
	{
		this( DEFAULT_CHARSET, direct );
	}

	/**
	 * Instantiates a new CharBufferInfo.
	 * 
	 * @param charsetName
	 *        The character set of the String.
	 * @param direct
	 *        True if the CharBuffer is allocated directly in memory outside of
	 *        the JVM, or false if the CharBuffer is allocated to the Heap.
	 */
	public CharBufferInfo( String charsetName, boolean direct )
	{
		super( CharBuffer.class, charsetName );

		this.direct = direct;
	}

	@Override
	protected Object getProperty( String name )
	{
		if (name.equals( "direct" )) return direct;

		return super.getProperty( name );
	}

	/**
	 * Returns whether the CharBuffer should be allocated directly.
	 * 
	 * @return True if the CharBuffer is allocated directly in memory outside of
	 *         the JVM, or false if the CharBuffer is allocated to the Heap.
	 */
	public boolean isDirect()
	{
		return direct;
	}

}
