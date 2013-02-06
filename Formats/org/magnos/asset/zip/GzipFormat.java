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

import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.magnos.asset.AssetFormat;
import org.magnos.asset.AssetInfo;
import org.magnos.asset.Assets;
import org.magnos.asset.base.BaseAssetFormat;


/**
 * A format for loading {@link AssetInfo} from GZ files.
 * 
 * <h2>Extensions</h2>
 * <ul>
 * <li>GZ - GNU Zip File Format</li>
 * </ul>
 * 
 * <h2>Request Types<h2>
 * <ul>
 * <li>{@link org.magnos.asset.AssetInfo}</li>
 * </ul>
 * 
 * @author Philip Diffenderfer
 * 
 */
public class GzipFormat extends BaseAssetFormat
{

	/**
	 * Instantiates a new GzipFormat.
	 */
	public GzipFormat()
	{
		super( new String[] { "gz" }, AssetInfo.class );
	}

	@Override
	public AssetInfo loadAsset( InputStream input, AssetInfo assetInfo ) throws Exception
	{
		String name = getName( assetInfo.getRequest() );

		GZIPInputStream gzip = new GZIPInputStream( input );

		return getAssetInfo( name, gzip );
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
		info.set( format.loadAsset( input, info ) );

		return info;
	}

	/**
	 * Returns the name of the asset given the GZ name.
	 * 
	 * @param gzip
	 *        The name of the GZ.
	 * @return The actual name of the asset.
	 */
	private String getName( String gzip )
	{
		return gzip.substring( 0, gzip.length() - 3 );
	}

}
