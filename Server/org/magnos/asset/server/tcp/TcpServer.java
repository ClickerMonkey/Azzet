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

package org.magnos.asset.server.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.magnos.asset.server.BaseAssetServer;
import org.magnos.asset.server.util.HashcodeComparator;


/**
 * An AssetServer for transmitting assets over TCP.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class TcpServer extends BaseAssetServer
{

	protected final int serverPort;
	protected final int serverBacklog;
	protected ConcurrentSkipListSet<TcpHandler> assetHandlers;
	protected ServerSocket serverSocket;

	/**
	 * Instantiates a new TcpServer.
	 * 
	 * @param serverPort
	 *        The port to bind to.
	 * @param serverBacklog
	 *        The maximum number of waiting connections to queue.
	 */
	public TcpServer( int serverPort, int serverBacklog )
	{
		this.serverPort = serverPort;
		this.serverBacklog = serverBacklog;
		this.assetHandlers = new ConcurrentSkipListSet<TcpHandler>( new HashcodeComparator() );
	}

	@Override
	protected void onBind() throws IOException
	{
		serverSocket = new ServerSocket( serverPort, serverBacklog );
	}

	@Override
	protected void onClose() throws IOException
	{
		for (TcpHandler handler : assetHandlers)
		{
			handler.close();
		}

		serverSocket.close();
	}

	@Override
	protected void onExecute() throws IOException
	{
		Socket socket = serverSocket.accept();

		TcpHandler handler = new TcpHandler( this, socket );
		handler.start();

		assetHandlers.add( handler );
	}

	/**
	 * Removes the given handler from this servers set.
	 * 
	 * @param handler
	 *        The handler to remove.
	 */
	protected void remove( TcpHandler handler )
	{
		assetHandlers.remove( handler );
	}

	/**
	 * Returns the set of handlers currently handling requests.
	 * 
	 * @return The reference to the set of handlers.
	 */
	public Set<TcpHandler> getHandlers()
	{
		return assetHandlers;
	}

}
