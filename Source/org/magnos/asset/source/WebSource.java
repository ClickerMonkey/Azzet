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
import java.net.URL;
import java.util.regex.Pattern;

import org.magnos.asset.base.BaseAssetSource;


/**
 * A source that reads assets from a web URL (currently only HTTP tested).
 * 
 * TODO test HTTPS
 * 
 * @author Philip Diffenderfer
 * 
 */
public class WebSource extends BaseAssetSource
{

	// The regular expression used to validate an HTTP URL.
	public static final Pattern REGEX_VALID = Pattern.compile( "^http[s]?\\://[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,3}(:[a-zA-Z0-9]*)?/?([a-zA-Z0-9\\-\\._\\?\\,\\'/\\\\\\+&amp;%\\$#\\=~])*$" );

	// The default base of the source. By default this is an empty string
	// meaning all requests given must be a full path.
	public static final String DEFAULT_BASE = "";

	/**
	 * Instantiates a new WebSource with the default base.
	 */
	public WebSource()
	{
		this( DEFAULT_BASE );
	}

	/**
	 * Instantiates a new WebSource.
	 * 
	 * @param base
	 *        The base to use to load assets. This base is effectively prepended
	 *        to each request string. If null it will be handled as the default
	 *        base.
	 */
	public WebSource( String base )
	{
		super( REGEX_VALID, base, DEFAULT_BASE );
	}

	@Override
	public InputStream getStream( String request ) throws Exception
	{
		return new URL( getAbsolute( request ) ).openStream();
	}

}
