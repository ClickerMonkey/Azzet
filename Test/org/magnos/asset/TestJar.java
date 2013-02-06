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
import static org.junit.Assert.assertNotNull;

import java.awt.image.BufferedImage;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.magnos.asset.image.GifFormat;
import org.magnos.asset.java.ClassFormat;
import org.magnos.asset.java.Jar;
import org.magnos.asset.java.JarFormat;
import org.magnos.asset.source.ClasspathSource;

/**
 * Tests the {@link JarFormat} class.
 * 
 * @author Philip Diffenderfer
 *
 */
public class TestJar 
{

	@BeforeClass
	public static void onBefore()
	{
		Assets.addFormat( new GifFormat() );
		Assets.addFormat( new ClassFormat() );
		Assets.addFormat( new JarFormat() );
		Assets.setDefaultSource( new ClasspathSource() );
	}
	
	@AfterClass
	public static void onAfter()
	{
		Assets.reset();
	}
	
	@Test
	public void testArchive() throws InstantiationException, IllegalAccessException
	{
		Jar arc = Assets.load("testing.jar");
		
		assertNotNull( arc );
		
		assertEquals( 5, arc.size() );
		
		BufferedImage[] img = arc.getAsset("dance.gif");
		
		assertNotNull( img );
		assertEquals( 8, img.length );
		
		Class<?> cls = arc.getClass("org.magnos.asset.test.NormalClass");
		
		assertNotNull( cls );
		assertEquals( "org.magnos.asset.test.NormalClass", cls.getName() );
		
		assertNotNull( cls.newInstance() );
	}
	
}
