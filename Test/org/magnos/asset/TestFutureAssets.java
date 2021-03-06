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
import org.magnos.asset.font.FontFormat;
import org.magnos.asset.image.GifFormat;
import org.magnos.asset.image.ImageFormat;
import org.magnos.asset.json.JsonFormat;
import org.magnos.asset.json.JsonObject;
import org.magnos.asset.source.ClasspathSource;
import org.magnos.asset.zip.Zip;
import org.magnos.asset.zip.ZipFormat;

/**
 * Tests the {@link FutureAsset} class.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class TestFutureAssets
{

	@BeforeClass
	public static void onBefore()
	{
		Assets.addFormat( new FontFormat() );
		Assets.addFormat( new ImageFormat() );
		Assets.addFormat( new GifFormat() );
		Assets.addFormat( new JsonFormat() );
		Assets.addFormat( new ZipFormat() );
		Assets.setDefaultSource( new ClasspathSource() );
	}

	@AfterClass
	public static void onAfter()
	{
		Assets.reset();
	}

	@Test
	public void testSingle() throws InterruptedException
	{
		FutureAsset<JsonObject> json = Assets.loadFuture( "big.json" );

		long start = System.currentTimeMillis();

		assertEquals( FutureAssetStatus.Pending, json.getStatus() );

		while ( json.getStatus() != FutureAssetStatus.Loaded )
		{
			Thread.sleep( 1 );
		}

		long end = System.currentTimeMillis();

		assertNotNull( json.get() );
		assertEquals( FutureAssetStatus.Loaded, json.getStatus() );

		System.out.format( "Asset loaded in background in %d ms\n", ( end - start ) );
	}

	@Test
	public void testMany() throws InterruptedException
	{
		Assets.unload( "big.json" );

		FutureAsset<Zip> archive = Assets.loadFuture( "archive.zip" );
		FutureAsset<JsonObject> json = Assets.loadFuture( "big.json" );

		while ( archive.getStatus() != FutureAssetStatus.Loaded )
		{
			Thread.sleep( 1 );
		}

		assertNotNull( archive.get() );
		assertEquals( FutureAssetStatus.Loaded, archive.getStatus() );

		long start = System.currentTimeMillis();

		while ( json.getStatus() != FutureAssetStatus.Loaded )
		{
			Thread.sleep( 1 );
		}

		long end = System.currentTimeMillis();

		assertNotNull( json.get() );
		assertEquals( FutureAssetStatus.Loaded, json.getStatus() );

		System.out.format( "JSON loaded in background in %d ms\n", ( end - start ) );
	}

	@Test
	public void testCancel()
	{
		// This might fail if your computer is fast enough to put this on a queue, the thread
		// grabs it, and calls run on it.
		FutureAsset<BufferedImage> gif = Assets.loadFuture( "dance.gif", BufferedImage.class );
		gif.cancel();
		
		assertEquals( FutureAssetStatus.Canceled, gif.getStatus() );
	}
	
	@Test
	public void testLoad()
	{
		Assets.unload( "big.json" );

		FutureAsset<JsonObject> json = Assets.loadFuture( "big.json" );
		json.load( true );

		assertNotNull( json.get() );
		assertEquals( FutureAssetStatus.Loaded, json.getStatus() );
	}

}
