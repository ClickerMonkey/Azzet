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

package org.magnos.asset.server.udp;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import org.magnos.asset.FormatUtility;
import org.magnos.asset.server.BaseAssetServer;
import org.magnos.asset.source.UdpSource;


/**
 * An AssetServer for transmitting assets over UDP.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class UdpServer extends BaseAssetServer
{

	private final int serverPort;
	private final DatagramPacket serverPacket;
	private DatagramSocket serverSocket;

	/**
	 * Instantiates a new UdpServer.
	 * 
	 * @param port
	 *        The port to listen on.
	 * @param packetSize
	 *        The maximum packet size for assets.
	 */
	public UdpServer( int port, int packetSize )
	{
		this.serverPort = port;
		this.serverPacket = new DatagramPacket( new byte[packetSize], packetSize );
	}

	@Override
	protected void onBind() throws IOException
	{
		serverSocket = new DatagramSocket( serverPort );
	}

	@Override
	protected void onClose() throws IOException
	{
		serverSocket.close();
	}

	@Override
	protected void onExecute() throws IOException
	{
		serverSocket.receive( serverPacket );

		try
		{
			String request = UdpSource.getRequest( serverPacket );

			triggerRequest( request );

			InputStream asset = getSource().getStream( request );

			triggerResponse( request, asset );

			byte[] response = FormatUtility.getBytes( asset );

			serverSocket.send( new DatagramPacket( response, response.length,
					serverPacket.getAddress(), serverPacket.getPort() ) );
		}
		catch (Exception e)
		{
			triggerError( e, false );
			// ignore
		}
	}

}
