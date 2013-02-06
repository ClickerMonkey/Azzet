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

import static org.junit.Assert.assertSame;

import java.sql.Connection;

import org.junit.Test;

/**
 * Tests the {@link SmartSource} class.
 * 
 * @author Philip Diffenderfer
 *
 */
public class TestSmart
{
	
	@Test
	public void testSmart()
	{
		ClasspathSource classpath = new ClasspathSource();
		WebSource web = new WebSource();
		FileSource file = new FileSource();
		DatabaseSource database = new DatabaseSource( (Connection)null, (String)null );

		SmartSource smart = new SmartSource( false );
		smart.addFile( file );
		smart.addWeb( web );
		smart.addDatabase( database );
		smart.setDefaultSource( classpath );
		
		assertSame( file, smart.getMatchingSource( "C:\\meow" ).source );
		assertSame( file, smart.getMatchingSource( "D:/meow" ).source );
		assertSame( file, smart.getMatchingSource( "./meow" ).source );
		assertSame( file, smart.getMatchingSource( "/meow" ).source );
		assertSame( web, smart.getMatchingSource( "http://www.google.com" ).source );
		assertSame( web, smart.getMatchingSource( "https://www.google.com" ).source );
		assertSame( web, smart.getMatchingSource( "ftp://www.google.com" ).source );
		assertSame( database, smart.getMatchingSource( "db://meow" ).source );
	}
	
}
