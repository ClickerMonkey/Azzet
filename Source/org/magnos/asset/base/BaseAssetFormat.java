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

import org.magnos.asset.AssetFormat;
import org.magnos.asset.AssetInfo;


/**
 * An abstract AssetFormat that requires the format's extensions and available
 * output (request) types at creation time.
 * 
 * @author Philip Diffenderfer
 * 
 */
public abstract class BaseAssetFormat implements AssetFormat
{

	// The array of extensions this format handles
	private String[] extensions;

	// The default type for the format.
	private Class<?> defaultType;

	// The request types for the format.
	private Class<?>[] requestTypes;

	/**
	 * Instantiates a new BaseAssetFormat.
	 * 
	 * @param extensions
	 *        The extensions this format handles.
	 * @param requestTypes
	 *        The array of compatible request types. At least one type must be
	 *        given. The first type is considered the default type.
	 */
	public BaseAssetFormat( String[] extensions, Class<?>... requestTypes )
	{
		this.extensions = extensions;
		this.defaultType = requestTypes[0];
		this.requestTypes = requestTypes;
	}

	@Override
	public final AssetInfo getInfo()
	{
		return getInfo( defaultType );
	}

	@Override
	public AssetInfo getInfo( Class<?> type )
	{
		return new BaseAssetInfo( type );
	}

	@Override
	public String[] getExtensions()
	{
		return extensions;
	}

	@Override
	public Class<?>[] getRequestTypes()
	{
		return requestTypes;
	}

	@Override
	public Class<?> getDefaultType()
	{
		return defaultType;
	}

	@Override
	public boolean hasType( Class<?> type )
	{
		for (Class<?> t : requestTypes)
		{
			if (t.isAssignableFrom( type ))
			{
				return true;
			}
		}
		return false;
	}

}
