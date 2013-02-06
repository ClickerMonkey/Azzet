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
 * The format of an asset. A format will create the asset from an input stream
 * and an optionally supplied asset info.
 * 
 * @author Philip Diffenderfer
 * 
 */
public interface AssetFormat
{

	/**
	 * Instantiates the AssetInfo this format expects for the
	 * {@link AssetFormat#loadAsset(InputStream, AssetInfo)} method.
	 * 
	 * @return The reference to a newly instantiated AssetInfo.
	 */
	public AssetInfo getInfo();

	/**
	 * Instantiates the AssetInfo this format expects for the
	 * {@link AssetFormat#loadAsset(InputStream, AssetInfo)} method.
	 * 
	 * @param type
	 *        One of the available request types.
	 * @return The reference to a newly instantiated AssetInfo.
	 */
	public AssetInfo getInfo( Class<?> type );

	/**
	 * The array of extensions this format can parse.
	 * 
	 * @return The array of extensions of the asset.
	 */
	public String[] getExtensions();

	/**
	 * The array of types this format can return.
	 * 
	 * @return The array of request types.
	 */
	public Class<?>[] getRequestTypes();

	/**
	 * The default request type this format will return.
	 * 
	 * @return The default request type.
	 */
	public Class<?> getDefaultType();

	/**
	 * Determines if this AssetFormat is compatible with the given type.
	 * 
	 * @param type
	 *        The type to check for compatibility.
	 * @return True if this format supports the given type.
	 */
	public boolean hasType( Class<?> type );

	/**
	 * Loads an asset from the given InputStream.
	 * 
	 * @param input
	 *        The stream to read data from.
	 * @param assetInfo
	 *        Optional information on the asset used in the loading process.
	 * @return The reference to the newly instantiated asset.
	 * @throws Exception
	 *         An exception occurred loading the asset from the input stream.
	 */
	public Object loadAsset( InputStream input, AssetInfo assetInfo ) throws Exception;

}
