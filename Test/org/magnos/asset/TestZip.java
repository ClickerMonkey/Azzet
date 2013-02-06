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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Font;
import java.awt.image.BufferedImage;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.magnos.asset.font.FontFormat;
import org.magnos.asset.image.GifFormat;
import org.magnos.asset.image.ImageFormat;
import org.magnos.asset.source.ClasspathSource;
import org.magnos.asset.zip.Zip;
import org.magnos.asset.zip.ZipFormat;

/**
 * Tests the {@link ZipFormat} class.
 * 
 * @author Philip Diffenderfer
 *
 */
public class TestZip 
{

	@BeforeClass
	public static void onBefore()
	{
		Assets.addFormat( new FontFormat() );
		Assets.addFormat( new ImageFormat() );
		Assets.addFormat( new GifFormat() );
		Assets.addFormat( new ZipFormat() );
		Assets.setDefaultSource( new ClasspathSource() );
	}
	
	@AfterClass
	public static void onAfter()
	{
		Assets.reset();
	}
	
	@Test
	public void testArchive()
	{
		Zip arc = Assets.load("archive.zip");
		
		assertNotNull( arc );
		
		assertTrue( arc.has("abaddon.ttf") );
		assertTrue( arc.getAsset("abaddon.ttf") instanceof Font );
		
		assertTrue( arc.has("troll.jpg") );
		assertTrue( arc.getAsset("troll.jpg") instanceof BufferedImage );
		
		assertTrue( arc.has("dance.gif") );
		assertTrue( arc.getAsset("dance.gif") instanceof BufferedImage[] );
	}
	
}
