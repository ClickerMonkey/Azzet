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

package org.magnos.asset.zip;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.magnos.asset.AssetFormat;
import org.magnos.asset.AssetInfo;
import org.magnos.asset.Assets;
import org.magnos.asset.base.BaseAssetFormat;


/**
 * A format for loading {@link Zip}s from ZIP files.
 * 
 * <h2>Extensions</h2>
 * <ul>
 * <li>ZIP - ZIP File Format</li>
 * </ul>
 * 
 * <h2>Request Types<h2>
 * <ul>
 * <li>{@link org.magnos.asset.zip.Zip}</li>
 * </ul>
 * 
 * @author Philip Diffenderfer
 * 
 */
public class ZipFormat extends BaseAssetFormat
{

	/**
	 * Instantiates a new ZipFormat.
	 */
	public ZipFormat()
	{
		super( new String[] { "zip" }, Zip.class );
	}

	@Override
	public Zip loadAsset( InputStream input, AssetInfo assetInfo ) throws Exception
	{
		Zip archive = new Zip();
		ZipInputStream jar = new ZipInputStream( input );
		ZipEntry entry = null;

		while ((entry = jar.getNextEntry()) != null)
		{
			String name = entry.getName();
			AssetInfo info = getAssetInfo( name, jar );

			if (info != null)
			{
				archive.put( name, info );
			}
		}
		return archive;
	}

	/**
	 * Creates the AssetInfo with the given request and from the given
	 * InputStream.
	 * 
	 * @param <A>
	 *        The asset type.
	 * @param name
	 *        The name of the asset. This is used to determine the asset format.
	 * @param input
	 *        The stream to read the asset from.
	 * @return The AssetInfo loaded with the loaded Asset. If a format could not
	 *         be determined by the given asset name then null will be returned.
	 * @throws Exception
	 *         An error occurred loading the asset from the InputStream with the
	 *         determined format.
	 */
	private AssetInfo getAssetInfo( String name, InputStream input ) throws Exception
	{
		AssetFormat format = Assets.getFormat( name );

		if (format == null)
		{
			return null;
		}

		AssetInfo info = format.getInfo();
		info.setSource( null );
		info.setFormat( format );
		info.setRequest( name );
		info.setPath( name );
		info.set( format.loadAsset( new UnclosableStream( input ), info ) );

		return info;
	}

	/**
	 * An input stream that ignores any calls to close.
	 * 
	 * @author Philip Diffenderfer
	 * 
	 */
	public static class UnclosableStream extends BufferedInputStream
	{

		/**
		 * Instantiates a new UnclosableStream.
		 * 
		 * @param in
		 *        The stream to never close.
		 */
		public UnclosableStream( InputStream in )
		{
			super( in );
		}

		@Override
		public void close() throws IOException
		{
			// ignore
		}

	}

}
