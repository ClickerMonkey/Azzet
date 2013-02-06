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

package org.magnos.asset.source;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.magnos.asset.base.BaseAssetSource;


/**
 * A source that sends the request to a server over TCP and the server returns
 * the asset requested.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class TcpSource extends BaseAssetSource
{

	// The default base of the source. By default this is an empty string
	// meaning all requests given must be a full path.
	public static final String DEFAULT_BASE = "";

	// A queue of available sockets.
	private final ConcurrentLinkedQueue<Socket> socketPool;

	// The address of the server.
	private final InetSocketAddress address;

	/**
	 * Instantiates a new TcpSource with the default base.
	 * 
	 * @param host
	 *        The host name or IP address to the server.
	 * @param port
	 *        The port the server is listening on.
	 */
	public TcpSource( String host, int port )
	{
		this( new InetSocketAddress( host, port ), DEFAULT_BASE );
	}

	/**
	 * Instantiates a new TcpSource.
	 * 
	 * @param host
	 *        The host name or IP address to the server.
	 * @param port
	 *        The port the server is listening on.
	 * @param base
	 *        The base to use to load assets. This base is effectively prepended
	 *        to each request string. If null it will be handled as the default
	 *        base.
	 */
	public TcpSource( String host, int port, String base )
	{
		this( new InetSocketAddress( host, port ), base );
	}

	/**
	 * Instantiates a new TcpSource with the default base.
	 * 
	 * @param address
	 *        The address to the server.
	 */
	public TcpSource( InetSocketAddress address )
	{
		this( address, DEFAULT_BASE );
	}

	/**
	 * Instantiates a new TcpSource.
	 * 
	 * @param address
	 *        The address to the server.
	 * @param base
	 *        The base to use to load assets. This base is effectively prepended
	 *        to each request string. If null it will be handled as the default
	 *        base.
	 */
	public TcpSource( InetSocketAddress address, String base )
	{
		super( null, base, DEFAULT_BASE );

		this.address = address;
		this.socketPool = new ConcurrentLinkedQueue<Socket>();
	}

	@Override
	public InputStream getStream( String request ) throws Exception
	{
		byte[] path = getAbsolute( request ).getBytes();

		Socket s = socketPool.poll();
		if (s == null || s.isClosed() || s.isOutputShutdown() || s.isInputShutdown())
		{
			s = new Socket();
			s.connect( address );
		}

		DataOutputStream o = new DataOutputStream( s.getOutputStream() );
		o.writeInt( path.length );
		o.write( path );
		o.flush();

		DataInputStream i = new DataInputStream( s.getInputStream() );
		int size = i.readInt();

		return new SocketInputStream( s, size, socketPool );
	}

	/**
	 * An input stream that wraps both a Socket and the sockets input stream.
	 * This is used to ensure that when the input stream is closed the socket is
	 * closed with it.
	 * 
	 * @author Philip Diffenderfer
	 * 
	 */
	protected static class SocketInputStream extends InputStream
	{

		private final Queue<Socket> socketPool;
		private final InputStream socketInput;
		private final Socket socket;
		private int max;

		/**
		 * Instantiates a new SocketInputStream.
		 * 
		 * @param socket
		 *        The socket connected to the magical cloud.
		 * @param socketPool
		 *        The pool to add the socket too once its 'closed'.
		 * @throws IOException
		 *         An error occurred creating the sockets input stream.
		 */
		public SocketInputStream( Socket socket, int max, Queue<Socket> socketPool ) throws IOException
		{
			this.socket = socket;
			this.socketInput = socket.getInputStream();
			this.socketPool = socketPool;
			this.max = max;
		}

		public int read() throws IOException
		{
			return (--max < 0 ? -1 : socketInput.read());
		}

		public int available()
		{
			return max;
		}

		public void close() throws IOException
		{
			socketPool.offer( socket );
		}
	}

	/**
	 * Gets the request string from the given InputStream. The first 4 bytes of
	 * the InputStream are read as the length of the request string.
	 * 
	 * @param input
	 *        The InputStream to read from.
	 * @return The request string from the InputStream.
	 * @throws IOException
	 *         An error occurred reading data from the InputStream.
	 */
	public static String getRequest( DataInputStream input ) throws IOException
	{
		int length = input.readInt();
		int read = 0;
		byte[] data = new byte[length];

		while (read < length)
		{
			read += input.read( data, read, length - read );
		}

		return new String( data );
	}

}
