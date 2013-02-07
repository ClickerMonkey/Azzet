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

/**
 * A factory to create FutureAssets given an AssetInfo.
 * 
 * @author Philip Diffenderfer
 *
 * @param <T>
 * 		The asset type.
 */
public interface FutureAssetFactory<T>
{
	
	/**
	 * Creates a FutureAsset given an AssetInfo.
	 * 
	 * @param assetInfo
	 * 		The AssetInfo for the asset to load.
	 * @return The FutureAsset to add to the background process.
	 */
	public FutureAsset<T> createFutureAsset( AssetInfo assetInfo );
	
}
