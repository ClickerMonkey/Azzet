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

package org.magnos.asset.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CountDownLatch;

import org.magnos.asset.AssetSource;
import org.magnos.asset.server.util.HashcodeComparator;


/**
 * An abstract implementation of an AssetServer.
 * 
 * @author Philip Diffenderfer
 * 
 */
public abstract class BaseAssetServer implements AssetServer, Runnable
{

	private final CountDownLatch startLatch = new CountDownLatch( 1 );
	private final ConcurrentSkipListSet<AssetServerListener> serverListeners;
	private AssetSource assetSource;
	private Thread thread;

	/**
	 * Instantiates a new BaseAssetServer.
	 */
	public BaseAssetServer()
	{
		serverListeners = new ConcurrentSkipListSet<AssetServerListener>( new HashcodeComparator() );
	}

	/**
	 * Requires the implementation to initialize and bind to its network
	 * interface.
	 * 
	 * @throws IOException
	 *         An error occurred binding.
	 */
	protected abstract void onBind() throws IOException;

	/**
	 * Requires the implementation to close its resource to the network
	 * interface.
	 * 
	 * @throws IOException
	 *         An error occurred closing.
	 */
	protected abstract void onClose() throws IOException;

	/**
	 * Requires the implementation to perform a single server operation. If this
	 * throws an exception the server is stopped.
	 * 
	 * @throws IOException
	 *         An error occurred executing.
	 */
	protected abstract void onExecute() throws IOException;

	@Override
	public void start() throws IOException, InterruptedException
	{
		try
		{
			onBind();

			thread = new Thread( this );
			thread.start();

			startLatch.await();
		}
		catch (IOException e)
		{
			triggerError( e, false );

			throw e;
		}
		catch (InterruptedException e)
		{
			triggerError( e, false );

			throw e;
		}
	}

	/**
	 * Runs the asset server until {@link #stop()} is invoked or the
	 * implementation throws an Exception during the onExecute invocation.
	 */
	@Override
	public void run()
	{
		// Started!
		startLatch.countDown();

		triggerStart();

		for (;;)
		{
			try
			{
				onExecute();
			}
			catch (IOException e)
			{
				triggerError( e, true );

				break;
			}
		}

		triggerStop();
	}

	@Override
	public void stop()
	{
		try
		{
			onClose();
		}
		catch (IOException e)
		{
			triggerError( e, true );
		}
	}

	@Override
	public void add( AssetServerListener listener )
	{
		serverListeners.add( listener );
	}

	@Override
	public void remove( AssetServerListener listener )
	{
		serverListeners.remove( listener );
	}

	@Override
	public void clear()
	{
		serverListeners.clear();
	}

	@Override
	public Thread getThread()
	{
		return thread;
	}

	@Override
	public AssetSource getSource()
	{
		return assetSource;
	}

	@Override
	public void setSource( AssetSource source )
	{
		this.assetSource = source;
	}

	/**
	 * Triggers the {@link AssetServerListener#onServerStart(AssetServer)} on all
	 * listeners.
	 */
	private void triggerStart()
	{
		for (AssetServerListener listener : serverListeners)
		{
			listener.onServerStart( this );
		}
	}

	/**
	 * Triggers the {@link AssetServerListener#onServerStop(AssetServer)} on all
	 * listeners.
	 */
	private void triggerStop()
	{
		for (AssetServerListener listener : serverListeners)
		{
			listener.onServerStop( this );
		}
	}

	/**
	 * Triggers the
	 * {@link AssetServerListener#onAssetRequest(AssetServer, String)} on all
	 * listeners.
	 * 
	 * @param request
	 *        The request that was made.
	 */
	public void triggerRequest( String request )
	{
		for (AssetServerListener listener : serverListeners)
		{
			listener.onAssetRequest( this, request );
		}
	}

	/**
	 * Triggers the
	 * {@link AssetServerListener#onAssetResponse(AssetServer, String, InputStream)}
	 * on all listeners.
	 * 
	 * @param request
	 *        The request that was made.
	 * @param response
	 *        The response that was created as an InputStream.
	 */
	public void triggerResponse( String request, InputStream response )
	{
		for (AssetServerListener listener : serverListeners)
		{
			listener.onAssetResponse( this, request, response );
		}
	}

	/**
	 * Triggers the
	 * {@link AssetServerListener#onServerError(AssetServer, Exception, boolean)}
	 * on all listeners.
	 * 
	 * @param e
	 *        The exception that was thrown.
	 * @param expected
	 *        Whether the exception was expected.
	 */
	public void triggerError( Exception e, boolean expected )
	{
		for (AssetServerListener listener : serverListeners)
		{
			listener.onServerError( this, e, expected );
		}
	}

}
