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

package org.magnos.asset;

import static org.junit.Assert.*;

import java.nio.CharBuffer;

import org.junit.Before;
import org.junit.Test;
import org.magnos.asset.source.ClasspathSource;
import org.magnos.asset.text.TextFormat;

/**
 * Tests the {@link TextFormat} class.
 * 
 * @author Philip Diffenderfer
 *
 */
public class TestText 
{

	@Before
	public void onBefore()
	{
		Assets.addFormat( new TextFormat() );
		Assets.setDefaultSource( new ClasspathSource() );
	}
	
	@Test
	public void testString()
	{
		String txt0 = Assets.load("greetings.txt");
		
		assertNotNull( txt0 );
		assertEquals( "Hello World", txt0 );
	}
	
	@Test
	public void testStringExplicit()
	{
		String txt1 = Assets.load("greetings.txt", String.class);
		
		assertNotNull( txt1 );
		assertEquals( "Hello World", txt1 );
	}
	
	@Test
	public void testCharArray()
	{
		char[] txt3 = Assets.load("greetings.txt", char[].class);
		
		assertNotNull( txt3 );
		assertArrayEquals( "Hello World".toCharArray(), txt3 );
	}
	
	@Test
	public void testCharBuffer()
	{
		CharBuffer txt4 = Assets.load("greetings.txt", CharBuffer.class);
		
		assertNotNull( txt4 );
		assertEquals( "Hello World", txt4.toString() );
	}
	
}
