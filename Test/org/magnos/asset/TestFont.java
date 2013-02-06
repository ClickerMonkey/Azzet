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

import java.awt.Font;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.magnos.asset.font.FontFormat;
import org.magnos.asset.font.FontInfo;
import org.magnos.asset.source.ClasspathSource;

/**
 * Tests the {@link FontFormat} class.
 * 
 * @author Philip Diffenderfer
 *
 */
public class TestFont 
{	

	@BeforeClass
	public static void onBefore() 
	{
		Assets.addFormat( new FontFormat() );
		Assets.setDefaultSource( new ClasspathSource() );
	}
	
	@AfterClass
	public static void onAfter()
	{
		Assets.reset();
	}
	
	@Test
	public void testFont()
	{
		Font fnt = Assets.load("abaddon.ttf");
		
		assertNotNull( fnt );
		
		assertEquals( FontInfo.DEFAULT_SIZE, fnt.getSize2D(), 0.00001 );
	}
	
	@Test
	public void testFontAgain()
	{
		Font fnt = Assets.load("abaddon.ttf", new FontInfo(23));
		
		assertNotNull( fnt );
		
		assertEquals( 23, fnt.getSize2D(), 0.00001 );
	}

}
