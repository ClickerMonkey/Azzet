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
import static org.junit.Assert.fail;

import javax.sound.sampled.Clip;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.magnos.asset.audio.AudioFormat;
import org.magnos.asset.ex.AssetException;
import org.magnos.asset.source.ClasspathSource;

/**
 * Tests the {@link AudioFormat} class.
 * 
 * @author Philip Diffenderfer
 *
 */
public class TestAudio 
{

	@BeforeClass
	public static void onBefore()
	{
		Assets.addFormat( new AudioFormat() );
		Assets.setDefaultSource( new ClasspathSource() );
	}
	
	@AfterClass
	public static void onAfter()
	{
		Assets.reset();
	}
	
	private void doTest() throws AssetException, InterruptedException
	{
		Clip clip = Assets.load("cowbell.wav");

		assertNotNull( clip );
		
		clip.start();

		Thread.sleep(1000);
		
		clip.stop();
		clip.close();
	}
	
	@Test
	public void testWav()
	{
		try 
		{
			doTest();
		}
		catch (AssetException e)
		{
			if (!(e.getCause() instanceof IllegalArgumentException))
			{
				fail( e.getCause().getMessage() );
			}
			else
			{
				System.err.println("Could not play Clip, no audio devices found!");
			}
		}
		catch (InterruptedException e)
		{
			fail( e.getMessage() );
		}
	}
	
}
