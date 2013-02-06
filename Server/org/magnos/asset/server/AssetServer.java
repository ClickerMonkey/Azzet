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

import org.magnos.asset.AssetSource;


/**
 * A server which handles requested assets.
 * 
 * @author Philip Diffenderfer
 * 
 */
public interface AssetServer
{

	/**
	 * Starts the server for business by binding, starting the server thread,
	 * waiting for the server to finish binding, and waiting for the server
	 * thread to finish starting.
	 * 
	 * @throws IOException
	 *         Error binding server.
	 */
	public void start() throws IOException, InterruptedException;

	/**
	 * Stops the asset server.
	 * 
	 * @throws IOException
	 *         An error occurred leaving the group.
	 */
	public void stop();

	/**
	 * Returns the thread thats running the AssetServer.
	 * 
	 * @return The reference to the thread.
	 */
	public Thread getThread();

	/**
	 * Returns the AssetSource the server is using to retrieve assets.
	 * 
	 * @return The reference to the AssetSource of the server.
	 */
	public AssetSource getSource();

	/**
	 * Sets the AssetSource the server is using to retrieve assets.
	 * 
	 * @param source
	 *        The new AssetSource.
	 */
	public void setSource( AssetSource source );

	/**
	 * Adds the AssetServerListener to this AssetServer.
	 * 
	 * @param listener
	 *        The AssetServerListener to add.
	 */
	public void add( AssetServerListener listener );

	/**
	 * Removes the given AssetServerListener from this AssetServer.
	 * 
	 * @param listener
	 *        The AssetServerListener to remove.
	 */
	public void remove( AssetServerListener listener );

	/**
	 * Removes all AssetServerListeners from this AssetServer.
	 */
	public void clear();

}
