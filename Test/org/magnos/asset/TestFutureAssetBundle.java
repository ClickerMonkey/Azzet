package org.magnos.asset;

import static org.junit.Assert.*;

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
public class TestFutureAssetBundle
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
	public void testBundle() throws InterruptedException
	{
		FutureAssetBundle bundle = new FutureAssetBundle();
		bundle.add( Assets.loadFuture( "dance.gif", BufferedImage.class ) );
		bundle.add( Assets.loadFuture( "big.json" ) );
		bundle.add( Assets.loadFuture( "archive.zip" ) );

		BufferedImage gif = null;
		JsonObject json = null;
		Zip archive = null;

		boolean running = true;
		
		while ( running )
		{
			if ( bundle.hasCompleted() )
			{
				bundle.loaded();
				gif = bundle.getAsset( "dance.gif" );
				json = bundle.getAsset( "big.json" );
				archive = bundle.getAsset( "archive.zip" );
				
				running = false;
			}
			
			System.out.format( "Percent Complete %d\n", (int) ( bundle.percentComplete() * 100 ) );
			
			Thread.sleep( 25 );
		}
		
		assertNotNull( gif );
		assertNotNull( json );
		assertNotNull( archive );
	}

}
