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

package org.magnos.asset.ex;

import org.magnos.asset.AssetInfo;


/**
 * An exception that occurs when loading an asset.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class AssetException extends RuntimeException
{

	private static final long serialVersionUID = 1L;

	private final AssetInfo info;

	/**
	 * Instantiates a new AssetException.
	 * 
	 * @param info
	 *        The associated asset info.
	 * @param cause
	 *        The cause of the exception.
	 */
	public AssetException( AssetInfo info, Throwable cause )
	{
		super( cause );
		this.info = info;
	}

	/**
	 * Instantiates a new AssetException.
	 * 
	 * @param info
	 *        The associated asset info.
	 */
	public AssetException( AssetInfo info )
	{
		this.info = info;
	}

	/**
	 * Returns the associated asset info.
	 * 
	 * @return The reference to the asset info that threw the error.
	 */
	public AssetInfo getInfo()
	{
		return info;
	}

}
