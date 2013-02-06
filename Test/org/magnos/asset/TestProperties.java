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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.magnos.asset.props.Config;
import org.magnos.asset.props.PropertyFormat;
import org.magnos.asset.source.ClasspathSource;

/**
 * Tests the {@link PropertyFormat} class.
 * 
 * @author Philip Diffenderfer
 *
 */
public class TestProperties 
{

	@BeforeClass
	public static void onBefore()
	{
		Assets.addFormat( new PropertyFormat() );
		Assets.setDefaultSource( new ClasspathSource() );
	}
	
	@AfterClass
	public static void onAfter()
	{
		Assets.reset();
	}
	
	@Test
	public void testProperties()
	{
		Properties props = Assets.load("app.properties");

		assertNotNull( props );

		assertEquals( 2, props.size() );
		assertEquals( "bar", props.get("foo") );
		assertEquals( "world", props.get("hello") );
	}
	
	@Test
	public void testConfig()
	{
		Config conf = Assets.load("system.config", Config.class);
		
		assertNotNull( conf );
		
		assertEquals( 9, conf.size() );
		assertEquals( true, conf.getBoolean("boolean") );
		assertEquals( 45, conf.getByte("byte"), 0 );
		assertEquals( 5883, conf.getShort("short"), 0 );
		assertEquals( 345343, conf.getInt("integer"), 0 );
		assertEquals( 7634897374L, conf.getLong("long"), 0 );
		assertEquals( new BigDecimal("96759872498273987427.298798745784"), conf.getDecimal("bigdecimal") );
		assertEquals( new BigInteger("8974826435987234985738475"), conf.getInteger("biginteger") );
		assertEquals( "Hello World", conf.get("string") );
		assertArrayEquals( new String[] {"a", "b", "c"}, conf.getStrings("strings", ",") );
	}
	
	@Test
	public void testConfigWithDefaults()
	{
		Config conf = Assets.load("system.config", Config.class);
		
		assertNotNull( conf );

		assertEquals( true, conf.getBoolean("boolean", false) );
		assertEquals( true, conf.getBoolean("goolean", true) );
		assertEquals( false, conf.getBoolean("toolean", false) );
	}
	
}
