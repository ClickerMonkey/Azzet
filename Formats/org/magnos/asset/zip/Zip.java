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

package org.magnos.asset.zip;

import java.util.HashMap;

import org.magnos.asset.AssetInfo;


/**
 * An archive of assets loaded from a ZIP file.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class Zip extends HashMap<String, AssetInfo>
{

	private static final long serialVersionUID = 1L;

	/**
	 * Determines if this Zip has an asset with the given name. If this returns
	 * false but the asset does exist in the Zip the most likely cause is that
	 * the asset requested does not have an associated format loaded.
	 * 
	 * @param name
	 *        The name of the asset.
	 * @return True if this Zip contains the an asset with the given name.
	 */
	public boolean has( String name )
	{
		return containsKey( name );
	}

	/**
	 * Returns an asset with the given name.
	 * 
	 * @param <A>
	 *        The asset type.
	 * @param name
	 *        The name of the asset.
	 * @return The asset with the given name, or null if it doesn't exist.
	 */
	public <A> A getAsset( String name )
	{
		AssetInfo info = get( name );

		return (info == null ? null : (A)info.get());
	}

}
