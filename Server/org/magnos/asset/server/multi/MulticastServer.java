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

package org.magnos.asset.server.multi;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import org.magnos.asset.FormatUtility;
import org.magnos.asset.server.BaseAssetServer;
import org.magnos.asset.source.MulticastSource;


/**
 * An AssetServer for transmitting assets over Multicast.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class MulticastServer extends BaseAssetServer
{

	private final int serverPort;
	private final InetAddress serverGroup;
	private final DatagramPacket serverPacket;
	private MulticastSocket serverSocket;

	/**
	 * Instantiates a new MulticastServer.
	 * 
	 * @param serverPort
	 *        The port to listen to.
	 * @param serverGroup
	 *        The group to join and listen to.
	 * @param packetSize
	 *        The maximum packet size for assets.
	 */
	public MulticastServer( int serverPort, InetAddress serverGroup, int packetSize )
	{
		this.serverPort = serverPort;
		this.serverGroup = serverGroup;
		this.serverPacket = new DatagramPacket( new byte[packetSize], packetSize );
	}

	@Override
	protected void onBind() throws IOException
	{
		serverSocket = new MulticastSocket( serverPort );
		serverSocket.joinGroup( serverGroup );
	}

	@Override
	protected void onClose() throws IOException
	{
		serverSocket.leaveGroup( serverGroup );
		serverSocket.close();
	}

	@Override
	protected void onExecute() throws IOException
	{
		serverSocket.receive( serverPacket );

		try
		{
			// Get the request from the packet.
			String request = MulticastSource.getRequestString( serverPacket.getData() );

			// If request is null, it was a response packet.
			if (request != null)
			{
				triggerRequest( request );

				// Get the asset as an InputStream.
				InputStream asset = getSource().getStream( request );

				triggerResponse( request, asset );

				// Convert it to an array of bytes.
				byte[] data = FormatUtility.getBytes( asset );

				// Convert the data and request into a response byte array.
				byte[] response = MulticastSource.getResponsePacket( data, request );

				// Send the response packet.
				serverSocket.send( new DatagramPacket( response, response.length, serverGroup, serverPort ) );
			}
		}
		catch (Exception e)
		{
			triggerError( e, false );
		}
	}

}
