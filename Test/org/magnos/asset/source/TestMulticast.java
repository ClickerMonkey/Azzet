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

package org.magnos.asset.source;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.magnos.asset.Assets;
import org.magnos.asset.props.PropertyFormat;
import org.magnos.asset.server.AssetServer;
import org.magnos.asset.server.multi.MulticastServer;
import org.magnos.asset.text.TextFormat;

/**
 * Tests the {@link MulticastSource} class.
 * 
 * @author Philip Diffenderfer
 *
 */
public class TestMulticast 
{

	private static final String SERVER_ADDRESS = "228.5.6.7";
	private static final int SERVER_PORT = 1027;
	private static final int SERVER_PACKET = 1380;
	private static final InetAddress SERVER_GROUP;
	
	static
	
	{
		InetAddress serverGroupAddress = null;
		
		try
		{
			serverGroupAddress = InetAddress.getByName( SERVER_ADDRESS );
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		
		SERVER_GROUP = serverGroupAddress;
	}
	

	@BeforeClass
	public static void onBefore()
	{
		Assets.addFormat( new PropertyFormat() );
		Assets.addFormat( new TextFormat() );
		Assets.setDefaultSource( new MulticastSource(SERVER_GROUP, SERVER_PORT) );
	}
	
	@AfterClass
	public static void onAfter()
	{
		Assets.reset();
	}
	
	@Test
	public void testMulticast() throws InterruptedException, IOException
	{
		/* SERVER START-UP */
		AssetServer server = new MulticastServer(SERVER_PORT, SERVER_GROUP, SERVER_PACKET);
		server.setSource(new ClasspathSource());
		server.start();
		/* SERVER START-UP */
		
		
		Properties props = Assets.load("app.properties");

		assertNotNull( props );

		assertEquals( 2, props.size() );
		assertEquals( "bar", props.get("foo") );
		assertEquals( "world", props.get("hello") );

		
		String message = Assets.load("greetings.txt");
		
		assertNotNull( message );
		assertEquals( "Hello World", message );
		
		
		/* SERVER SHUT-DOWN */
		server.stop();
		/* SERVER SHUT-DOWN */
	}
	
}
