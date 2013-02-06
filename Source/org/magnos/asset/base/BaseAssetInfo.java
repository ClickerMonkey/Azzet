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

package org.magnos.asset.base;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.magnos.asset.AssetFormat;
import org.magnos.asset.AssetInfo;
import org.magnos.asset.AssetSource;
import org.magnos.asset.ex.AssetException;


/**
 * A base implementation of AssetInfo.
 * 
 * @author Philip Diffenderfer
 * 
 * @param A
 *        The asset type.
 */
public class BaseAssetInfo implements AssetInfo
{

	public static final String[] PROPERTIES_NONE = {};

	private final Class<?> type;
	private Object asset;
	private String path;
	private String request;
	private AssetFormat format;
	private AssetSource source;

	/**
	 * Instantiates a new BaseAssetInfo.
	 * 
	 * @param type
	 */
	public BaseAssetInfo( Class<?> type )
	{
		this.type = type;
	}

	/**
	 * Returns the property given the lower-case name of the property. This
	 * should be overridden in any implementing classes to return a property
	 * based on the name provided.
	 * 
	 * @param name
	 *        The name of the property.
	 * @return The value of the property, or null if it doesn't exist.
	 */
	protected Object getProperty( String name )
	{
		return null;
	}

	@Override
	public final <T> T property( String name )
	{
		return (T)getProperty( name.toLowerCase() );
	}

	@Override
	public final <T> T property( String name, Class<T> type )
	{
		return (T)getProperty( name.toLowerCase() );
	}

	@Override
	public <A> A get()
	{
		return (A)asset;
	}

	@Override
	public <A> A load() throws AssetException
	{
		if (asset == null)
		{
			asset = create();
		}
		return (A)asset;
	}

	@Override
	public <A> A create() throws AssetException
	{
		AssetSource source = getSource();

		if (source == null)
		{
			throw new NullPointerException( "AssetSource cannot be null" );
		}

		AssetFormat format = getFormat();

		if (format == null)
		{
			throw new NullPointerException( "AssetFormat cannot be null" );
		}

		String request = getRequest();

		if (request == null)
		{
			throw new NullPointerException( "Request cannot be null" );
		}

		Object asset = null;
		InputStream in = null;
		try
		{
			// Get the input stream from the source.
			in = source.getStream( request );

			// Wrap with a buffered input stream if its not one already.
			if (!(in instanceof BufferedInputStream))
			{
				in = new BufferedInputStream( in );
			}

			try
			{
				// Get the asset from the input stream (buffered).
				asset = format.loadAsset( in, this );
			}
			finally
			{
				try
				{
					in.close();
				}
				catch (IOException e)
				{
					// ignore exceptions from closing.
				}
			}
		}
		catch (Exception e)
		{
			throw new AssetException( this, e );
		}

		return (A)asset;
	}

	@Override
	public <A> void set( A asset )
	{
		this.asset = asset;
	}

	@Override
	public void clear()
	{
		this.asset = null;
	}

	@Override
	public void setInfo( AssetSource source, AssetFormat format, String request )
	{
		setSource( source );
		setFormat( format );
		setRequest( request );
		setPath( source.getAbsolute( request ) );
	}

	@Override
	public AssetFormat getFormat()
	{
		return format;
	}

	@Override
	public void setFormat( AssetFormat format )
	{
		this.format = format;
	}

	@Override
	public AssetSource getSource()
	{
		return source;
	}

	@Override
	public void setSource( AssetSource source )
	{
		this.source = source;
	}

	@Override
	public String getRequest()
	{
		return request;
	}

	@Override
	public void setRequest( String request )
	{
		this.request = request;
	}

	@Override
	public String getPath()
	{
		return path;
	}

	@Override
	public void setPath( String path )
	{
		this.path = path;
	}

	@Override
	public boolean isType( Class<?> type )
	{
		return type.isAssignableFrom( this.type );
	}

	@Override
	public boolean isMatch( AssetInfo info )
	{
		return true;
	}

	@Override
	public String[] getProperties()
	{
		return PROPERTIES_NONE;
	}

	@Override
	public Class<?> getType()
	{
		return type;
	}

}
