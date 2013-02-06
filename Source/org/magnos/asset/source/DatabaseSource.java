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

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.magnos.asset.base.BaseAssetSource;


/**
 * A source that reads assets from a database connection.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class DatabaseSource extends BaseAssetSource
{

	// The default base of the source. By default this is an empty string
	// meaning all requests given must be a full path.
	public static final String DEFAULT_BASE = "";

	private final DataSource dataSource;
	private final String query;
	private final Connection connection;

	/**
	 * Instantiates a new DatabaseSource that uses the given connection.
	 * 
	 * @param connection
	 *        The Connection to use to query the database for the assets.
	 * @param query
	 *        The query used to select the asset from the database.
	 */
	public DatabaseSource( Connection connection, String query )
	{
		this( null, connection, query );
	}

	/**
	 * Instantiates a new DatabaseSource that uses the given connection.
	 * 
	 * @param connection
	 *        The Connection to use to query the database for the assets.
	 * @param tableName
	 *        The name of the table containing the assets.
	 * @param assetColumn
	 *        The name of the column containing the asset.
	 * @param requestColumn
	 *        The name of the column that contains the requests.
	 */
	public DatabaseSource( Connection connection, String tableName, String assetColumn, String requestColumn )
	{
		this( null, connection, String.format( "SELECT %s FROM % WHERE %s=?", assetColumn, tableName, requestColumn ) );
	}

	/**
	 * Instantiates a new DatabaseSource that uses the given DataSource to create
	 * database connections.
	 * 
	 * @param dataSource
	 *        The DataSource to create connections with.
	 * @param query
	 *        The query used to select the asset from the database.
	 */
	public DatabaseSource( DataSource dataSource, String query )
	{
		this( dataSource, null, query );
	}

	/**
	 * Instantiates a new DatabaseSource.
	 */
	private DatabaseSource( DataSource dataSource, Connection connection, String query )
	{
		super( null, null, DEFAULT_BASE );

		this.dataSource = dataSource;
		this.connection = connection;
		this.query = query;
	}

	/**
	 * The query used to select the asset from the database. The first column
	 * returned from this query must be a BLOB or CLOB in which case the value
	 * will be read as a binary stream of data.
	 * 
	 * @return The reference to the query String.
	 */
	public String getQuery()
	{
		return query;
	}

	/**
	 * The Connection used to query the database for assets. If this is null then
	 * a DataSource was given to this source.
	 * 
	 * @return The reference to the given Connection or null.
	 */
	public Connection getConnection()
	{
		return connection;
	}

	/**
	 * The DataSource used to create connections to the database. If this is null
	 * then a Connection was given to this source.
	 * 
	 * @return The reference to the given DataSource or null.
	 */
	public DataSource getDataSource()
	{
		return dataSource;
	}

	@Override
	public InputStream getStream( String request ) throws Exception
	{
		Connection conn = connection;
		// If no given connection, create one from the DataSource.
		if (connection == null)
		{
			conn = dataSource.getConnection();
		}

		InputStream input = null;
		try
		{
			// Prepares the query for execution.
			PreparedStatement statement = conn.prepareStatement( query );
			try
			{
				// Places the request string in the query.
				statement.setString( 1, request );

				// Executes the query.
				ResultSet results = statement.executeQuery();
				try
				{
					// If results were returned, read them in as a binary stream.
					if (results.next())
					{
						input = results.getBinaryStream( 1 );
					}
				}
				finally
				{
					results.close();
				}
			}
			finally
			{
				statement.close();
			}
		}
		finally
		{
			// If a connection was created from the DataSource make sure to
			// close it.
			if (connection == null)
			{
				conn.close();
			}
		}

		return input;
	}

}
