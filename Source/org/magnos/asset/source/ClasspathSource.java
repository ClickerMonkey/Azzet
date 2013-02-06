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
import java.util.regex.Pattern;

import org.magnos.asset.base.BaseAssetSource;


/**
 * A source that reads assets from the class-path. If this application is
 * bundled in a jar then all resources bundled in the jar with it are accessible
 * through this source.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class ClasspathSource extends BaseAssetSource
{

	// The regular expression used to validate a class-path
	public static final Pattern REGEX_VALID = Pattern.compile( "^[\\w\\d_][\\w\\d\\._\\/]*$" );

	// The default base of the source. By default this is an empty string
	// meaning all requests given must be a full path.
	public static final String DEFAULT_BASE = "";

	// The default loader for loading resources from the class-path.
	public static final ClassLoader DEFAULT_LOADER = ClasspathSource.class.getClassLoader();

	private final ClassLoader loader;

	/**
	 * Instantiates a new ClasspathSource with the default base and the default
	 * class loader.
	 */
	public ClasspathSource()
	{
		this( DEFAULT_BASE, DEFAULT_LOADER );
	}

	/**
	 * Instantiates a new ClasspathSource with the given base and the default
	 * class loader.
	 * 
	 * @param base
	 *        The base to use to load assets. This base is effectively prepended
	 *        to each request string. If null it will be handled as the default
	 *        base.
	 */
	public ClasspathSource( String base )
	{
		this( base, DEFAULT_LOADER );
	}

	/**
	 * Instantiates a new ClasspathSource with the default base and the given
	 * class loader.
	 * 
	 * @param loader
	 *        The loader to use to load assets. The getResourceAsStream method is
	 *        invoked on this loader when a request for an asset is made. If null
	 *        it will be handled as the default class loader.
	 */
	public ClasspathSource( ClassLoader loader )
	{
		this( DEFAULT_BASE, loader );
	}

	/**
	 * Instantiates a new ClasspathSource with the given base and the given class
	 * loader.
	 * 
	 * @param base
	 *        The base to use to load assets. This base is effectively prepended
	 *        to each request string. If null it will be handled as the default
	 *        base.
	 * @param loader
	 *        The loader to use to load assets. The getResourceAsStream method is
	 *        invoked on this loader when a request for an asset is made. If null
	 *        it will be handled as the default class loader.
	 */
	public ClasspathSource( String base, ClassLoader loader )
	{
		super( REGEX_VALID, base, DEFAULT_BASE );

		this.loader = (loader != null ? loader : DEFAULT_LOADER);
	}

	/**
	 * The loader to which the assets are loaded from. The getResourceAsStream
	 * method is invoked on this loader when a request for an asset is made.
	 * 
	 * @return The reference to the class loader.
	 */
	public ClassLoader getLoader()
	{
		return loader;
	}

	@Override
	public InputStream getStream( String request )
	{
		return loader.getResourceAsStream( getAbsolute( request ) );
	}

}
