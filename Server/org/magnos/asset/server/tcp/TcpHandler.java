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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import org.magnos.asset.FormatUtility;
import org.magnos.asset.source.TcpSource;


/**
 * A Thread for handling a single TCP socket requesting assets.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class TcpHandler extends Thread
{

	private TcpServer assetServer;
	private Socket socket;
	private DataInputStream socketInput;
	private DataOutputStream socketOutput;

	/**
	 * Instantiates a new TcpHandler.
	 * 
	 * @param server
	 *        The server that created this handler.
	 * @param socket
	 *        The socket to handle requests from.
	 * @throws IOException
	 *         An error occurred creating the sockets I/O stream.
	 */
	public TcpHandler( TcpServer server, Socket socket ) throws IOException
	{
		this.assetServer = server;
		this.socket = socket;
		this.socketInput = new DataInputStream( new BufferedInputStream( socket.getInputStream() ) );
		this.socketOutput = new DataOutputStream( new BufferedOutputStream( socket.getOutputStream() ) );
	}

	/**
	 * Processes asset requests for the given socket.
	 */
	public void run()
	{
		for (;;)
		{
			try
			{
				String request = TcpSource.getRequest( socketInput );

				assetServer.triggerRequest( request );

				InputStream asset = assetServer.getSource().getStream( request );

				assetServer.triggerResponse( request, asset );

				send( asset, socketOutput );
			}
			catch (IOException e)
			{
				assetServer.triggerError( e, true );

				break;
			}
			catch (Exception e)
			{
				assetServer.triggerError( e, false );
			}
		}
	}

	/**
	 * Closes the handler by closing the associated socket and removing itself
	 * from its AssetServer.
	 */
	public void close()
	{
		try
		{
			socket.close();
		}
		catch (IOException e)
		{
			// ignore
		}
		finally
		{
			assetServer.remove( this );
		}
	}

	/**
	 * Sends the data in the InputStream to the given OutputStream.
	 * 
	 * TODO Make more efficient!
	 * 
	 * @param input
	 *        The InputStream to read from.
	 * @param output
	 *        The OutputStream to write to.
	 * @throws IOException
	 *         An error occurred reading or writing to either stream.
	 */
	public void send( InputStream input, DataOutputStream output ) throws IOException
	{
		byte[] data = FormatUtility.getBytes( input );

		output.writeInt( data.length );
		output.write( data );
		output.flush();
	}

}
