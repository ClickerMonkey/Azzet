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

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.magnos.asset.Assets;
import org.magnos.asset.props.PropertyFormat;
import org.magnos.asset.server.AssetServer;
import org.magnos.asset.server.ssl.SslServer;
import org.magnos.asset.source.ClasspathSource;
import org.magnos.asset.text.TextFormat;

/**
 * Tests the {@link SslSource} class.
 * 
 * @author Philip Diffenderfer
 *
 */
public class TestSsl 
{
	/* CREATE KEYSTORE

keytool -genkey -keystore sslstore.ks -keyalg RSA

	 */

	/* RUN WITH THESE ARGUMENTS

-Djavax.net.ssl.keyStore=Test/sslstore.ks -Djavax.net.ssl.keyStorePassword=secret
-Djavax.net.ssl.trustStore=Test/sslstore.ks -Djavax.net.ssl.trustStorePassword=secret

	 */
	
	/* ADDITIONAL DEBUG ARGUMENTS
	 * 
-Djava.protocol.handler.pkgs=com.sun.net.ssl.internal.www.protocol -Djavax.net.debug=ssl

	 */
	
	private static final int SERVER_PORT = 8435;
	private static final int SERVER_BACKLOG = 32;
	private static final String SERVER_HOST = "127.0.0.1";

	@Before
	public void onBefore()
	{
		Assets.addFormat( new PropertyFormat() );
		Assets.addFormat( new TextFormat() );
		Assets.setDefaultSource( new SslSource(SERVER_HOST, SERVER_PORT) );
	}

	@Test
	public void testSsl() throws Exception
	{
		/* SERVER START-UP */
		AssetServer server = new SslServer(SERVER_PORT, SERVER_BACKLOG);
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
