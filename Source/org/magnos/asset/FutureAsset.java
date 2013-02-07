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

import java.util.concurrent.Callable;

import org.magnos.asset.ex.AssetException;

/**
 * A promise that an asset will be loaded in the future at some point. When a
 * future is returned the asset has been queued in a background process to be
 * loaded. If the loading of the asset has not started yet the future can be
 * canceled.
 * 
 * <pre>
 * FutureAsset&lt;BufferedImage&gt; future = Assets.loadFuture("mypic.png");
 * BufferedImage mypic = null;
 * 
 * while (running) {
 *    // do stuff
 *    if ( future.getStatus() == FutureAssetStatus.Loaded ) {
 *       // get the loaded asset
 *       mypic = future.get();
 *       // mark the future as loaded
 *       future.loaded();
 *    }
 *    // do other stuff
 * }
 * </pre>
 * 
 * @author Philip Diffenderfer
 *
 * @param <T>
 * 		The type of asset.
 */
public interface FutureAsset<T> extends Runnable, Callable<T>
{

	/**
	 * The information required to load the asset. This is used to generate 
	 * the asset in the background to be set on this FutureAsset.
	 * 
	 * @return The AssetInfo used to load the asset.
	 */
	public AssetInfo getInfo();
	
	/**
	 * Loads the asset and returns immediately. If the asset is currently
	 * being loaded in the background this will wait for the background process
	 * to complete. The user must check the status of the asset, if it already 
	 * failed then this will do nothing. 
	 * 
	 * @param loadIfCanceled
	 *        Whether the asset should be loaded if the FutureAsset has been
	 *        canceled already. If true the asset will always be loaded, if 
	 *        false the asset will not be loaded if the status is canceled.
	 * @return The asset loaded.
	 */
	public T load( boolean loadIfCanceled );
	
	/**
	 * Returns the asset as-is. If the asset has not been completely loaded 
	 * yet, null will be returned.
	 * 
	 * @return The loaded asset or null if it hasn't been loaded yet.
	 */
	public T get();
	
	/**
	 * Cancels loading the asset if it hasn't been loaded yet. 
	 * 
	 * @return True if the FutureAsset has successfully canceled or it is 
	 *         already in the canceled state.
	 */
	public boolean cancel();

	/**
	 * A method that should be called by the user when they take the non-null
	 * asset through {@link #get()} or forcefully take it through 
	 * {@link #load(boolean)}. Other implementations of FutureAsset may need to be 
	 * notified by the main thread through this method. 
	 * 
	 * @see FutureAsset
	 */
	public void loaded();
	
	/**
	 * The current status of the asset.
	 * 
	 * @return The status of the asset.
	 */
	public FutureAssetStatus getStatus();
	
	/**
	 * If the asset failed loading, this is the exception that it threw.
	 * 
	 * @return The exception that was thrown from the asset failing to load,
	 *         or null if the asset has not failed loading or has even been
	 *         loaded yet.
	 * @see FutureAssetStatus#Failed
	 */
	public AssetException getFailureReason();
	
}
