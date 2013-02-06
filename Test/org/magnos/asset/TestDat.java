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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.magnos.asset.dat.DatFormat;
import org.magnos.asset.source.ClasspathSource;

/**
 * Tests the {@link DatFormat} class.
 * 
 * @author Philip Diffenderfer
 *
 */
public class TestDat 
{

	@Before
	public void onBefore()
	{
		Assets.addFormat( new DatFormat() );
		Assets.setDefaultSource( new ClasspathSource() );
	}
	
	@Test
	public void testByteArray()
	{
		byte[] data = Assets.load("info.dat");

		assertNotNull( data );
		assertEquals( 5, data.length );
		assertEquals( 0, data[0] );
		assertEquals( 1, data[1] );
		assertEquals( 2, data[2] );
		assertEquals( 3, data[3] );
		assertEquals( 4, data[4] );
	}
	
	@Test
	public void testDatByteArrayOutputStream()
	{
		ByteArrayOutputStream out = Assets.load("info.dat", ByteArrayOutputStream.class);
		assertNotNull( out.toByteArray() );
	}
	
	@Test
	public void testInputStream() throws IOException
	{
		InputStream in = Assets.load("info.dat", InputStream.class);
		assertEquals( 5, in.available() );
		assertEquals( 0, in.read() );
		assertEquals( 1, in.read() );
		assertEquals( 2, in.read() );
		assertEquals( 3, in.read() );
		assertEquals( 4, in.read() );
	}
	
}
