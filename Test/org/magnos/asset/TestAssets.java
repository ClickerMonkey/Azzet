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
import org.magnos.asset.dat.DatFormat;
import org.magnos.asset.source.ClasspathSource;
import org.magnos.asset.text.TextFormat;

/**
 * Tests the {@link Assets} class.
 * 
 * @author Philip Diffenderfer
 *
 */
public class TestAssets 
{

	@Before
	public void onBefore() 
	{
		Assets.addFormat( new TextFormat() );
		Assets.addFormat( new DatFormat() );
		Assets.setDefaultSource( new ClasspathSource() );
	}
	
	@Test
	public void testLoadByExtension() 
	{
		String s = Assets.load("greetings.txt");

		assertNotNull( s );
		assertEquals( "Hello World", s );
	}
	
	@Test
	public void testLoadByType()
	{
		CharBuffer cb = Assets.load("greetings.txt", CharBuffer.class);

		assertNotNull( cb );
		assertEquals( "Hello World", cb.toString() );
	}
	
	@Test
	public void testLoadByDifferentType()
	{
		byte[] dat = Assets.load("greetings.txt", byte[].class);
		
		assertNotNull( dat );
		assertEquals( "Hello World", new String(dat) );
	}
	
	@Test
	public void testLoadByDifferentExtension()
	{
		byte[] dat = Assets.load("greetings.txt", "dat");

		assertNotNull( dat );
		assertEquals( "Hello World", new String(dat) );
	}
	
}
