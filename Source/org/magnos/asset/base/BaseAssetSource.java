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

import java.io.InputStream;
import java.util.regex.Pattern;

import org.magnos.asset.AssetSource;


/**
 * A base implementation of AssetSource.
 * 
 * @author pdiffenderfer
 * 
 */
public abstract class BaseAssetSource implements AssetSource
{

	/**
	 * The default base where the assets are loaded from. This is effectively
	 * prepended to each request string.
	 */
	public static final String DEFAULT_BASE = "";

	private final Pattern pattern;
	private final String base;

	/**
	 * Instantiates a new BaseAssetSource.
	 * 
	 * @param pattern
	 *        The regular expression pattern to use to validate the request. If
	 *        null is given the validate method will always return true.
	 * @param base
	 *        The base where the assets are loaded from. This is effectively
	 *        prepended to each request string. If this is null the default base
	 *        is used.
	 * @param defaultBase
	 *        A non-null base which is used when a base is not specified.
	 */
	protected BaseAssetSource( Pattern pattern, String base, String defaultBase )
	{
		this.pattern = pattern;
		this.base = (base != null ? base : defaultBase);
	}

	@Override
	public String getBase()
	{
		return base;
	}

	@Override
	public String getAbsolute( String request )
	{
		return base + request;
	}

	@Override
	public boolean isValid( String request )
	{
		return (pattern == null ? true : pattern.matcher( getAbsolute( request ) ).matches());
	}

	@Override
	public boolean isPresent( String request )
	{
		InputStream input = null;
		// try creating the stream...
		try
		{
			input = getStream( request );
		}
		catch (Exception e)
		{
			// if an error occurred the asset must not be present
			return false;
		}

		// assume the asset was present and now close the stream
		try
		{
			input.close();
		}
		catch (Exception e)
		{
			// swallow *gulp*
		}
		return true;
	}

}
