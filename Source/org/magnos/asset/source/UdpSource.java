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

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.magnos.asset.base.BaseAssetSource;


/**
 * A source that reads assets from a UDP server. This source will send a packet
 * containing only the request string to the server and wait for a packet to
 * return.
 * 
 * TODO maxWaitTime resendInterval fragment?
 * 
 * @author Philip Diffenderfer
 * 
 */
public class UdpSource extends BaseAssetSource
{

	// The default packet size to use for reading an asset. Any asset that
	// has more bytes than this may be truncated and may result in an
	// exception being thrown when being parsed by the format.
	public static final int DEFAULT_PACKET = 1380;

	// The default base of the source. By default this is an empty string
	// meaning all requests given must be a full path.
	public static final String DEFAULT_BASE = "";

	private final int packetSize;
	private final SocketAddress address;

	/**
	 * Instantiates a new UdpSource with the default base and packet size.
	 * 
	 * @param host
	 *        The host name or IP address to the server.
	 * @param port
	 *        The port the server is listening on.
	 */
	public UdpSource( String host, int port )
	{
		this( new InetSocketAddress( host, port ), DEFAULT_BASE, DEFAULT_PACKET );
	}

	/**
	 * Instantiates a new UdpSource with the default base.
	 * 
	 * @param host
	 *        The host name or IP address to the server.
	 * @param port
	 *        The port the server is listening on.
	 * @param packetSize
	 *        The maximum packet size to expect for receiving assets, in bytes.
	 */
	public UdpSource( String host, int port, int packetSize )
	{
		this( new InetSocketAddress( host, port ), DEFAULT_BASE, packetSize );
	}

	/**
	 * Instantiates a new UdpSource with the default packet size.
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
	public UdpSource( String host, int port, String base )
	{
		this( new InetSocketAddress( host, port ), base, DEFAULT_PACKET );
	}

	/**
	 * Instantiates a new UdpSource.
	 * 
	 * @param host
	 *        The host name or IP address to the server.
	 * @param port
	 *        The port the server is listening on.
	 * @param base
	 *        The base to use to load assets. This base is effectively prepended
	 *        to each request string. If null it will be handled as the default
	 *        base.
	 * @param packetSize
	 *        The maximum packet size to expect for receiving assets, in bytes.
	 */
	public UdpSource( String host, int port, String base, int packetSize )
	{
		this( new InetSocketAddress( host, port ), base, packetSize );
	}

	/**
	 * Instantiates a new UdpSource with the default base and packet size.
	 * 
	 * @param address
	 *        The address to the server.
	 */
	public UdpSource( SocketAddress address )
	{
		this( address, DEFAULT_BASE, DEFAULT_PACKET );
	}

	/**
	 * Instantiates a new UdpSource with the default base.
	 * 
	 * @param address
	 *        The address to the server.
	 * @param packetSize
	 *        The maximum packet size to expect for receiving assets, in bytes.
	 */
	public UdpSource( SocketAddress address, int packetSize )
	{
		this( address, DEFAULT_BASE, packetSize );
	}

	/**
	 * Instantiates a new UdpSource with the default packet size.
	 * 
	 * @param address
	 *        The address to the server.
	 * @param base
	 *        The base to use to load assets. This base is effectively prepended
	 *        to each request string. If null it will be handled as the default
	 *        base.
	 */
	public UdpSource( SocketAddress address, String base )
	{
		this( address, base, DEFAULT_PACKET );
	}

	/**
	 * Instantiates a new UdpSource.
	 * 
	 * @param address
	 *        The address to the server.
	 * @param base
	 *        The base to use to load assets. This base is effectively prepended
	 *        to each request string. If null it will be handled as the default
	 *        base.
	 * @param packetSize
	 *        The maximum packet size to expect for receiving assets, in bytes.
	 */
	public UdpSource( SocketAddress address, String base, int packetSize )
	{
		super( null, base, DEFAULT_BASE );

		this.address = address;
		this.packetSize = packetSize;
	}

	@Override
	public InputStream getStream( String request ) throws Exception
	{
		byte[] path = getAbsolute( request ).getBytes();

		DatagramPacket outgoing = new DatagramPacket( path, path.length );
		DatagramPacket incoming = new DatagramPacket( new byte[packetSize], packetSize );

		DatagramSocket s = new DatagramSocket();
		s.connect( address );
		s.send( outgoing );
		s.receive( incoming );

		return new DatagramInputStream( incoming.getData(), 0, incoming.getLength(), s );
	}

	/**
	 * An input stream that wraps a DatagramSocket and the packet that was
	 * received.
	 * 
	 * @author Philip Diffenderfer
	 * 
	 */
	protected static class DatagramInputStream extends InputStream
	{

		private final DatagramSocket socket;
		private final byte[] data;
		private final int length;
		private int index = 0;
		private int mark = -1;
		private int max = 0;

		/**
		 * Instantiates a new DatagramInputStream.
		 * 
		 * @param packet
		 *        The packet received by the request.
		 * @param socket
		 *        The socket connected to the magical cloud.
		 */
		public DatagramInputStream( byte[] packet, int offset, int length, DatagramSocket socket )
		{
			this.data = packet;
			this.length = length;
			this.index = offset;
			this.socket = socket;
		}

		public int read() throws IOException
		{
			max--;
			return (index >= length ? -1 : data[index++]);
		}

		public void close() throws IOException
		{
			socket.close();
		}

		public int available() throws IOException
		{
			return (length - index);
		}

		public void mark( int readlimit )
		{
			mark = index;
			max = readlimit;
		}

		public void reset() throws IOException
		{
			if (mark < 0 || max < 0)
			{
				throw new IOException();
			}
			index = mark;
		}

		public boolean markSupported()
		{
			return true;
		}
	}

	/**
	 * Parses the request from the given DatagramPacket.
	 * 
	 * @param packet
	 *        The packet to parse the request from.
	 * @return The request contained in the packet.
	 */
	public static String getRequest( DatagramPacket packet )
	{
		return new String( packet.getData(), 0, packet.getLength() );
	}

}
