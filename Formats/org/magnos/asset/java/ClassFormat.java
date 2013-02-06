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

import org.magnos.asset.AssetInfo;
import org.magnos.asset.FormatUtility;
import org.magnos.asset.base.BaseAssetFormat;


/**
 * A format for loading {@link Class}s from CLASS and CLAZZ files.
 * 
 * <h2>Extensions</h2>
 * <ul>
 * <li>CLASS/CLAZZ - Java Class File</li>
 * </ul>
 * 
 * <h2>Request Types<h2>
 * <ul>
 * <li>{@link java.lang.Class}</li>
 * </ul>
 * 
 * @author Philip Diffenderfer
 * 
 */
public class ClassFormat extends BaseAssetFormat
{

	/**
	 * Instantiates a new ClassFormat.
	 */
	public ClassFormat()
	{
		super( new String[] { "class", "clazz" }, Class.class );
	}

	@Override
	public AssetInfo getInfo( Class<?> type )
	{
		return new ClassInfo();
	}

	@Override
	public Class<?> loadAsset( InputStream input, AssetInfo assetInfo ) throws Exception
	{
		byte[] data = FormatUtility.getBytes( input );

		ClassInfo info = (ClassInfo)assetInfo;
		DynamicLoader loader = info.getClassLoader();

		Class<?> cls = loader.loadClass( data );
		loader.linkClass( cls );

		return cls;
	}

}
