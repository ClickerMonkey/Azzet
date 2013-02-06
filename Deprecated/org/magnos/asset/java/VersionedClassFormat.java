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
 * @author Philip Diffenderfer
 * 
 */
@Deprecated
/* WIP */
public class VersionedClassFormat extends BaseAssetFormat
{

	// The internal loader which converts an InputStream into a class.
	private static DynamicLoader classLoader = new DynamicLoader();

	/**
	 * Instantiates a new ClassFormat.
	 */
	public VersionedClassFormat()
	{
		super( new String[] { "class", "clazz" }, Class.class );
	}

	@Override
	public Class<?> loadAsset( InputStream input, AssetInfo assetInfo ) throws Exception
	{
		byte[] data = FormatUtility.getBytes( input );

		Class<?> cls = null;
		try
		{
			cls = classLoader.loadClass( data );
			classLoader.linkClass( cls );
		}
		catch (LinkageError e)
		{
			classLoader = new DynamicLoader();
			cls = classLoader.loadClass( data );
			classLoader.linkClass( cls );
		}

		return cls;
	}

//		InternalLoader newLoader = new InternalLoader();
//		cls = newLoader.loadClass(data);
//		
//		Class<?> old = classLoader.getClass(cls.getName());
//		
//		if (old != null)
//		{
//			Versioned clsVersion = cls.getAnnotation(Versioned.class);
//			Versioned oldVersion = old.getAnnotation(Versioned.class);
//			boolean clsVersioned = (clsVersion != null);
//			boolean oldVersioned = (oldVersion != null);
//			
//			boolean replace = clsVersioned;
//			if (clsVersioned && oldVersioned) 
//			{
//				replace = (clsVersion.value() > oldVersion.value());
//			}
//			
//			if (replace)
//			{
//				classLoader = newLoader;
//				classLoader.linkClass(cls);
//			}
//			else
//			{
//				cls = old;
//			}
//		}
//		else {
//			classLoader = newLoader;
//			classLoader.linkClass(cls);
//		}

}
