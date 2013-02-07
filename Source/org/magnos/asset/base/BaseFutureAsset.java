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

import org.magnos.asset.AssetInfo;
import org.magnos.asset.Assets;
import org.magnos.asset.FutureAsset;
import org.magnos.asset.FutureAssetFactory;
import org.magnos.asset.FutureAssetStatus;
import org.magnos.asset.ex.AssetException;

/**
 * A base FutureAsset implementation.
 * 
 * @author Philip Diffenderfer
 *
 * @param <T>
 * 		The asset type.
 */
public class BaseFutureAsset<T> implements FutureAsset<T>
{

	/**
	 * Returns a FutureAssetFactory for BaseFutureAssets.
	 * 
	 * @return
	 * 		A newly instantiated FutureAssetFactory.
	 */
	public static <T> FutureAssetFactory<T> Factory()
	{
		return new FutureAssetFactory<T>()
		{
			@Override
			public FutureAsset<T> createFutureAsset( AssetInfo assetInfo )
			{
				return new BaseFutureAsset<T>( assetInfo );
			}
		};
	}

	// The given AssetInfo, never changes.
	private final AssetInfo info;

	// A lock to use to control the status
	private final Object lock = new Object();

	// The current status of the FutureAsset, controlled by lock.
	private volatile FutureAssetStatus status = FutureAssetStatus.Pending;

	// The last exception thrown for trying to load.
	private AssetException failure;

	// The asset loaded (if any).
	private T asset;

	/**
	 * Instantiates a new BaseFutureAsset.
	 * 
	 * @param info
	 * 		The AssetInfo of the asset.
	 */
	public BaseFutureAsset( AssetInfo info )
	{
		this.info = info;
	}

	@Override
	public void run()
	{
		load( false );
	}

	@Override
	public T call() throws Exception
	{
		return load( false );
	}
	
	@Override
	public AssetInfo getInfo()
	{
		return info;
	}

	@Override
	public T load( final boolean loadIfCanceled )
	{
		if ( status.isLoadable( loadIfCanceled ) )
		{
			synchronized ( lock )
			{
				if ( status.isLoadable( loadIfCanceled ) )
				{
					try
					{
						// Call this to ensure the asset gets cached if successfully loaded.
						asset = Assets.get( info );

						status = FutureAssetStatus.Loaded;
						
						failure = null;
					}
					catch ( AssetException e )
					{
						failure = e;

						status = FutureAssetStatus.Failed;
					}
				}
			}
		}

		return asset;
	}

	@Override
	public T get()
	{
		return asset;
	}

	@Override
	public boolean cancel()
	{
		if ( status == FutureAssetStatus.Pending )
		{
			synchronized ( lock )
			{
				if ( status == FutureAssetStatus.Pending )
				{
					status = FutureAssetStatus.Canceled;
				}
			}
		}

		return ( status == FutureAssetStatus.Canceled );
	}

	@Override
	public void loaded()
	{
		// base implementation does nothing
	}

	@Override
	public FutureAssetStatus getStatus()
	{
		return status;
	}

	@Override
	public AssetException getFailureReason()
	{
		return failure;
	}

}
