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
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import org.magnos.asset.base.BaseAssetSource;

import org.magnos.asset.source.TcpSource.SocketInputStream;


/**
 * A source that sends the request to a server over SSL and the server returns
 * the asset requested.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class SslSource extends BaseAssetSource
{

	// The default base of the source. By default this is an empty string
	// meaning all requests given must be a full path.
	public static final String DEFAULT_BASE = "";

	// A queue of available sockets.
	private final ConcurrentLinkedQueue<Socket> socketPool;

	// The factory to use to create sockets.
	private final SocketFactory socketFactory;

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
	public SslSource( String host, int port )
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
	public SslSource( String host, int port, String base )
	{
		this( new InetSocketAddress( host, port ), base );
	}

	/**
	 * Instantiates a new TcpSource with the default base.
	 * 
	 * @param address
	 *        The address to the server.
	 */
	public SslSource( InetSocketAddress address )
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
	public SslSource( InetSocketAddress address, String base )
	{
		super( null, base, DEFAULT_BASE );

		this.address = address;
		this.socketPool = new ConcurrentLinkedQueue<Socket>();
		this.socketFactory = SSLSocketFactory.getDefault();
	}

	@Override
	public InputStream getStream( String request ) throws Exception
	{
		byte[] path = getAbsolute( request ).getBytes();

		Socket s = socketPool.poll();
		if (s == null || s.isClosed() || s.isOutputShutdown() || s.isInputShutdown())
		{
			s = socketFactory.createSocket( address.getAddress(), address.getPort() );
		}

		DataOutputStream o = new DataOutputStream( s.getOutputStream() );
		o.writeInt( path.length );
		o.write( path );
		o.flush();

		DataInputStream i = new DataInputStream( s.getInputStream() );
		int size = i.readInt();

		return new SocketInputStream( s, size, socketPool );
	}

}
