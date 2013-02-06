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

package org.magnos.asset.java;

import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.magnos.asset.AssetFormat;
import org.magnos.asset.AssetInfo;
import org.magnos.asset.Assets;
import org.magnos.asset.base.BaseAssetFormat;
import org.magnos.asset.zip.ZipFormat.UnclosableStream;


/**
 * A format for loading {@link Jar}s from JAR files.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class JarFormat extends BaseAssetFormat
{

	/**
	 * Instantiates a new JarFormat.
	 */
	public JarFormat()
	{
		super( new String[] { "jar" }, Jar.class );
	}

	@Override
	public Jar loadAsset( InputStream input, AssetInfo assetInfo ) throws Exception
	{
		Jar archive = new Jar();
		JarInputStream jar = new JarInputStream( input );
		JarEntry entry = null;

		DynamicLoader loader = new DynamicLoader();

		while ((entry = jar.getNextJarEntry()) != null)
		{
			String name = entry.getName();
			AssetInfo info = getAssetInfo( name, jar, loader );

			if (info != null)
			{
				archive.put( name, info );
			}
		}

		archive.setClassLoader( loader );

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
	private AssetInfo getAssetInfo( String name, InputStream input, DynamicLoader loader ) throws Exception
	{
		AssetFormat format = Assets.getFormat( name );

		if (format == null)
		{
			return null;
		}

		AssetInfo info = format.getInfo();

		// If the AssetInfo requires the ClassLoader, provide it.
		if (info instanceof ClassLoadable)
		{
			((ClassLoadable)info).setClassLoader( loader );
		}

		info.setSource( null );
		info.setFormat( format );
		info.setRequest( name );
		info.setPath( name );

		// Load it with an InputStream that will not close 'input'.
		Object asset = format.loadAsset( new UnclosableStream( input ), info );

		// If the Asset requires the ClassLoader, provide it.
		if (asset instanceof ClassLoadable)
		{
			((ClassLoadable)asset).setClassLoader( loader );
		}

		info.set( asset );

		return info;
	}

}
