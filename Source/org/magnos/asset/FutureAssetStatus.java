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
 * The status of a FutureAsset.
 * 
 * @author Philip Diffenderfer
 *
 */
public enum FutureAssetStatus
{
	
	/**
	 * The asset is currently queued in the background process to be loaded.
	 */
	Pending ( true ), 
	
	/**
	 * The background process is currently loading this asset.
	 */
	Loading ( true ), 
	
	/**
	 * The asset has successfully been loaded, either by the background process
	 * or by the user manually calling {@link FutureAsset#load(boolean)}.
	 */
	Loaded ( false ), 
	
	/**
	 * The asset has failed loading in the background process. The failure 
	 * exception can be retrieved at {@link FutureAsset#getFailureReason()}.
	 */
	Failed ( false ), 
	
	/**
	 * The asset was queued to be loaded but was canceled. The user can still
	 * call {@link FutureAsset#load(boolean)} to move the FutureAsset to 
	 * Loaded.
	 */
	Canceled ( false );
	
	/**
	 * Whether an asset in this status can be loaded.
	 */
	private final boolean canLoad;
	
	/**
	 * Instantiates a new FutureAssetStatus.
	 * 
	 * @param canLoad
	 * 		Whether an asset in this status can be loaded.
	 */
	private FutureAssetStatus( boolean canLoad )
	{
		this.canLoad = canLoad;
	}
	
	/**
	 * Whether an asset in this status can be loaded.
	 * 
	 * @param loadIfCanceled
	 *        By default an asset in Canceled status cannot be loaded, however
	 *        if this is true the asset can be loaded.
	 * @return True if the asset can be loaded in this status, otherwise false.
	 */
	public boolean isLoadable( final boolean loadIfCanceled )
	{
		return ( canLoad || (loadIfCanceled && this == Canceled) );
	}
	
}
