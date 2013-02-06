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

package org.magnos.asset;

import java.io.InputStream;


/**
 * A source to retrieve assets from. A source is given requests and its
 * responsible for validating it, determining the absolute path to the requested
 * object, and finally returning an InputStream to parse the request.
 * 
 * @author Philip Diffenderfer
 * 
 */
public interface AssetSource
{

	/**
	 * Returns true if the request given is in valid format. This will validate
	 * against the absolute path to the request.
	 * 
	 * @param request
	 *        The request to validate.
	 * @return True if the request is valid for this source, otherwise false.
	 */
	public boolean isValid( String request );

	/**
	 * Returns the absolute path to the request within this source. This is
	 * typically a combination between the base of this source and the request.
	 * 
	 * @param request
	 *        The request to compute the absolute path of.
	 * @return The absolute path of the request.
	 */
	public String getAbsolute( String request );

	/**
	 * The base where the assets are loaded from. This is effectively prepended
	 * to each request string.
	 * 
	 * @return The reference to the base String.
	 */
	public String getBase();

	/**
	 * Returns whether the given request exists in this source. This does not
	 * determine if the asset is in the proper format however, it only returns
	 * whether something with the given request exists.
	 * 
	 * The default implementation will try to create the stream and return true
	 * if the stream was created without error. Implementing classes should
	 * override this functionality to something more efficient.
	 * 
	 * @param request
	 *        The request to check for existence.
	 * @return True if the request exists at the source or false if it does not.
	 */
	public boolean isPresent( String request );

	/**
	 * Returns an InputStream to the requested asset. If the asset doesn't exist
	 * in this source then an exception will be thrown or null will be returned
	 * depending on the implementation.
	 * 
	 * @param request
	 *        The request for the asset.
	 * @return The reference to InputStream containing the requested asset, or
	 *         null if the request was invalid.
	 * @throws Exception
	 *         An error occurred retrieving the asset from the source.
	 */
	public InputStream getStream( String request ) throws Exception;

}
