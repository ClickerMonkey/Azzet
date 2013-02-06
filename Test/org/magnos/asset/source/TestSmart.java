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
