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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.magnos.asset.csv.Cell;
import org.magnos.asset.csv.CsvFormat;
import org.magnos.asset.csv.Row;
import org.magnos.asset.csv.Table;
import org.magnos.asset.source.ClasspathSource;

/**
 * Tests the {@link CsvFormat} class.
 * 
 * @author Philip Diffenderfer
 *
 */
public class TestCsv 
{

	@BeforeClass
	public static void onBefore()
	{
		Assets.addFormat( new CsvFormat() );
		Assets.setDefaultSource( new ClasspathSource() );
	}
	
	@AfterClass
	public static void onAfter()
	{
		Assets.reset();
	}
	
	@Test
	public void testLoad()
	{
		Table tbl = Assets.load("data.csv");
		
		assertNotNull( tbl );
		assertNotNull( tbl.getHeader() );

		assertEquals( "id", tbl.getHeader().getString(0) );
		assertEquals( "name", tbl.getHeader().getString(1) );
		assertEquals( "age", tbl.getHeader().getString(2) );
		assertEquals( "admin", tbl.getHeader().getString(3) );
		
		assertEquals( 4, tbl.size() );
		
		Row r0 = tbl.get(0);
		assertEquals( new Integer(1), r0.getInt(0) );
		assertEquals( "phil", r0.getString(1) );
		assertEquals( new Float(23), r0.getFloat(2) );
		assertEquals( Boolean.TRUE, r0.getBoolean(3) );
		
		Row r1 = tbl.get(1);
		assertEquals( new Integer(2), r1.getInt(0) );
		assertEquals( "jack", r1.getString(1) );
		assertEquals( new Float(45.6), r1.getFloat(2) );
		assertEquals( Boolean.FALSE, r1.getBoolean(3) );
		
		Row r2 = tbl.get(2);
		assertEquals( new Integer(3), r2.getInt(0) );
		assertEquals( "jesse", r2.getString(1) );
		assertEquals( new Float(2), r2.getFloat(2) );
		assertEquals( Boolean.TRUE, r2.getBoolean(3) );
		
		Row r3 = tbl.get(3);
		assertEquals( new Integer(4), r3.getInt(0) );
		assertEquals( "john", r3.getString(1) );
		assertEquals( new Float(10), r3.getFloat(2) );
		assertEquals( Boolean.FALSE, r3.getBoolean(3) );
	}
	
	@Test
	public void testIs()
	{
		Cell c0 = new Cell("23");

		assertTrue( c0.is(String.class) );
		assertTrue( c0.is(Boolean.class) );
		assertTrue( c0.is(Byte.class) );
		assertTrue( c0.is(Short.class) );
		assertTrue( c0.is(Integer.class) );
		assertTrue( c0.is(Long.class) );
		assertTrue( c0.is(Float.class) );
		assertTrue( c0.is(Double.class) );
		assertTrue( c0.is(BigDecimal.class) );
		assertTrue( c0.is(BigInteger.class) );
		
		Cell c1 = new Cell("23.6");

		assertTrue( c1.is(String.class) );
		assertTrue( c1.is(Boolean.class) );
		assertFalse( c1.is(Byte.class) );
		assertFalse( c1.is(Short.class) );
		assertFalse( c1.is(Integer.class) );
		assertFalse( c1.is(Long.class) );
		assertTrue( c1.is(Float.class) );
		assertTrue( c1.is(Double.class) );
		assertTrue( c1.is(BigDecimal.class) );
		assertFalse( c1.is(BigInteger.class) );
		
		Cell c2 = new Cell("true");

		assertTrue( c2.is(String.class) );
		assertTrue( c1.is(Boolean.class) );
		assertFalse( c2.is(Byte.class) );
		assertFalse( c2.is(Short.class) );
		assertFalse( c2.is(Integer.class) );
		assertFalse( c2.is(Long.class) );
		assertFalse( c2.is(Float.class) );
		assertFalse( c2.is(Double.class) );
		assertFalse( c2.is(BigDecimal.class) );
		assertFalse( c2.is(BigInteger.class) );
	}
	
	@Test
	public void testToString()
	{
		Table tbl = Assets.load("data.csv");
		
		System.out.println( tbl.toFormattedString(true, true) );
		System.out.println();
		System.out.println( tbl.toDelimitedString("\t") );
		System.out.println();
		System.out.println( tbl.toDelimitedString("|") );
	}
	
}
