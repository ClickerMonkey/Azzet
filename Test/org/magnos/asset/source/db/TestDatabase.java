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

package org.magnos.asset.source.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.magnos.asset.Assets;
import org.magnos.asset.FormatUtility;
import org.magnos.asset.props.PropertyFormat;
import org.magnos.asset.source.ClasspathSource;
import org.magnos.asset.source.DatabaseSource;

/**
 * A DatabaseSource Test that can't be directly ran.
 * 
 * @author Philip Diffenderfer
 *
 */
@Ignore
public class TestDatabase 
{

	public static final String INSERT_QUERY = "INSERT INTO asset_table (name,asset) VALUES (?,?)";
	
	private Connection connection;
	private String driverName;
	private String driverUrl;
	private String username;
	private String password;
	
	/**
	 * Instantiates a new TestDatabase.
	 * 
	 * @param driverName
	 * 		The class name of the Driver.
	 * @param driverUrl
	 * 		The URL to the database.
	 * @param username
	 * 		The username to the database.
	 * @param password
	 * 		The password to the database.
	 */
	public TestDatabase(String driverName, String driverUrl, String username, String password)
	{
		this.driverName = driverName;
		this.driverUrl = driverUrl;
		this.username = username;
		this.password = password;
	}
	
	/**
	 * Returns the connection, establishing one the first time invoked.
	 * 
	 * @return
	 * 		The reference to the connection to the database.
	 * @throws Exception
	 * 		An error has occurred connecting.
	 */
	public Connection getConnection() throws Exception
	{
		if (connection == null)
		{
			Class.forName( driverName );
			
			connection = DriverManager.getConnection(driverUrl, username, password);	
		}
		return connection;
	}
	
	/**
	 * Runs before each Test method. Establishes a connection and sets auto
	 * commit to false. 
	 * 
	 * @throws Exception
	 * 		An error occurred with the database.
	 */
	@Before
	public void onBefore() throws Exception
	{
		Connection con = getConnection();

		Assets.addFormat(new PropertyFormat());
		Assets.setDefaultSource(new DatabaseSource(con, "SELECT asset FROM asset_table WHERE name=?"));
		
		con.setAutoCommit(false);
	}
	
	/**
	 * Runs the main test by inserting the Asset into the database, then uses
	 * the DatabaseSource to retrieve the asset from the database.
	 * 
	 * @throws Exception
	 * 		An error occurred with the database.
	 */
	@Test
	public void test() throws Exception
	{
		final String request = "app.properties";
		final ClasspathSource source = new ClasspathSource();
		final InputStream input = source.getStream(request);
		
		assertTrue( insert(request, input) );
		
		input.close();
		
		Properties props = Assets.load(request);
		
		assertNotNull( props );

		assertEquals( 2, props.size() );
		assertEquals( "bar", props.get("foo") );
		assertEquals( "world", props.get("hello") );
	}
	
	/**
	 * Runs after each Test method.
	 * 
	 * @throws Exception
	 * 		An error occurred with the database.
	 */
	@After
	public void onAfter() throws Exception
	{
		Connection con = getConnection();
		
		con.rollback();
		
		Assets.reset();
	}
	
	/**
	 * Inserts the request into the database.
	 * 
	 * @param request
	 * 		The request string.
	 * @param input
	 * 		The asset as an InputStream.
	 * @return
	 * 		Returns true if the asset was inserted, otherwise false.
	 * @throws Exception
	 * 		An error occurred with the database.
	 */
	protected boolean insert(String request, InputStream input) throws Exception
	{
		Connection con = getConnection();
		
		PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
		
		boolean inserted = false;
		try 
		{
			stmt.setString(1, request);
			
			// Try Blob, BinaryStream, and finally Bytes if neither work.
			try 
			{
				stmt.setBlob(2, input);
				
				System.out.println(driverName + " inserted as Blob");
			}
			catch (AbstractMethodError e0) 
			{
				try 
				{
					stmt.setBinaryStream(2, input);

					System.out.println(driverName + " inserted as BinaryStream");
				}
				catch (AbstractMethodError e1) 
				{
					stmt.setBytes(2, FormatUtility.getBytes(input));

					System.out.println(driverName + " inserted as Bytes");
				}
			}
			
			inserted = (stmt.executeUpdate() == 1);
		}
		finally 
		{
			stmt.close();
		}

		return inserted;
	}
	
}
