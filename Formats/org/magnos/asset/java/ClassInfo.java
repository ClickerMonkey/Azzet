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

import org.magnos.asset.AssetInfo;
import org.magnos.asset.base.BaseAssetInfo;

/**
 * Information for loading CLASS and CLAZZ files.
 * 
 * @author Philip Diffenderfer
 *
 */
public class ClassInfo extends BaseAssetInfo implements ClassLoadable 
{

	/**
	 * The default ClassLoader for the class.
	 */
	public static final DynamicLoader DEFAULT_LOADER = new DynamicLoader();
	
	// properties
	public static final String[] PROPERTIES = {
		"loader"
	};
	
	private DynamicLoader loader;
	
	/**
	 * Instantiates a new ClassInfo with the default ClassLoader.
	 */
	public ClassInfo() 
	{
		this(DEFAULT_LOADER);
	}
	
	/**
	 * Instantiates a new ClassInfo.
	 * 
	 * @param loader
	 * 		The ClassLoader for the class.
	 */
	public ClassInfo(DynamicLoader loader) 
	{
		super( Class.class );
		
		this.loader = loader;
	}

	@Override
	protected Object getProperty(String name)
	{
		if (name.equals("loader")) return loader;
		
		return null;
	}
	
	@Override
	public boolean isMatch( AssetInfo info )
	{
		if ( info instanceof ClassInfo )
		{
			ClassInfo clazz = (ClassInfo)info;
			
			return (
				clazz.loader == loader
			);
		}
		
		return false;
	}
	
	@Override
	public String[] getProperties()
	{
		return PROPERTIES;
	}
	
	@Override
	public void setClassLoader(DynamicLoader loader) 
	{
		this.loader = loader;
	}

	@Override
	public DynamicLoader getClassLoader() 
	{
		return loader;
	}
	
}
