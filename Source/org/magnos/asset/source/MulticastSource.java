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
import java.net.InetAddress;
import java.net.MulticastSocket;

import org.magnos.asset.base.BaseAssetSource;
import org.magnos.asset.source.UdpSource.DatagramInputStream;


/**
 * A source that reads assets from a multicast group. This is a fairly
 * unreliable method since other traffic may already exist on the multicast
 * group.
 * 
 * TODO maxWaitTime resendInterval fragment?
 * 
 * @author Philip Diffenderfer
 * 
 */
public class MulticastSource extends BaseAssetSource
{

	// The default packet size to use for reading an asset. Any asset that
	// has more bytes than this may be truncated and may result in an
	// exception being thrown when being parsed by the format.
	public static final int DEFAULT_PACKET = 1380;

	// The default base of the source. By default this is an empty string
	// meaning all requests given must be a full path.
	public static final String DEFAULT_BASE = "";

	private final int packetSize;
	private final int port;
	private final InetAddress address;

	/**
	 * Instantiates a new MulticastSource given the address and port to join.
	 * 
	 * @param address
	 *        The multicast address to join.
	 * @param port
	 *        The port to listen to on the multicast address.
	 */
	public MulticastSource( InetAddress address, int port )
	{
		this( address, port, DEFAULT_BASE, DEFAULT_PACKET );
	}

	/**
	 * Instantiates a new MulticastSource given the address and port to join and
	 * the base of the requests.
	 * 
	 * @param address
	 *        The multicast address to join.
	 * @param port
	 *        The port to listen to on the multicast address.
	 * @param base
	 *        The base to use to load assets. This base is effectively prepended
	 *        to each request string. If null it will be handled as the default
	 *        base.
	 */
	public MulticastSource( InetAddress address, int port, String base )
	{
		this( address, port, base, DEFAULT_PACKET );
	}

	/**
	 * Instantiates a new MulticastSource given the address and port to join, the
	 * base of the requests, and the maximum packet size in bytes.
	 * 
	 * @param address
	 *        The multicast address to join.
	 * @param port
	 *        The port to listen to on the multicast address.
	 * @param base
	 *        The base to use to load assets. This base is effectively prepended
	 *        to each request string. If null it will be handled as the default
	 *        base.
	 * @param packetSize
	 *        The maximum packet size an asset is expected to be. Any asset
	 *        received thats larger than this will be truncated to this length
	 *        which may result in a parsing error from the asset format.
	 */
	public MulticastSource( InetAddress address, int port, String base, int packetSize )
	{
		super( null, base, DEFAULT_BASE );
		this.address = address;
		this.port = port;
		this.packetSize = packetSize;
	}

	/**
	 * The multicast address to join.
	 * 
	 * @return The reference to the InetAddress joined.
	 */
	public InetAddress getAddress()
	{
		return address;
	}

	/**
	 * The port to listen to on the multicast address.
	 * 
	 * @return The port number to listen to.
	 */
	public int getPort()
	{
		return port;
	}

	/**
	 * The maximum packet size an asset is expected to be. Any asset received
	 * thats larger than this will be truncated to this length which may result
	 * in a parsing error from the asset format.
	 * 
	 * @return The maximum packet size in bytes.
	 */
	public int getPacketSize()
	{
		return packetSize;
	}

	@Override
	public InputStream getStream( String request ) throws Exception
	{
		// | flag | path length | path | response length | response
		// +------+---------------+--------+-----------------+-------------
		// | byte | unsigned byte | byte[] | unsigned short | byte[]
		//
		// When the flag is 0 it is a request
		// When the flag is 1 it is a response

		String absolute = getAbsolute( request );
		byte[] requestPacket = getRequestPacket( absolute );

		if (requestPacket.length > packetSize)
		{
			throw new Exception( "Request packet cannot be larger than " + packetSize + " bytes." );
		}

		DatagramPacket outgoing = new DatagramPacket( requestPacket, requestPacket.length, address, port );
		DatagramPacket incoming = new DatagramPacket( new byte[packetSize], packetSize );

		MulticastSocket s = new MulticastSocket( port );

		// Join the group and send the request.
		s.joinGroup( address );
		s.send( outgoing );

		// Receive the incoming response.
		for (;;)
		{
			s.receive( incoming );

			String response = getResponseString( incoming.getData() );

			// If its non-null and matches break from loop.
			if (response != null && response.equals( absolute ))
			{
				break;
			}
		}

		// Leave the group
		s.leaveGroup( address );

		// Get the response data and validate it.
		byte[] response = incoming.getData();
		int responseLength = getResponseSize( response );

		if (responseLength < 0)
		{
			throw new Exception( "Invalid response. Packet invalid or not large enough to contain asset." );
		}

		int offset = absolute.length() + 4;
		int end = offset + responseLength;

		// Return the asset as a byte array.
		return new DatagramInputStream( response, offset, end, s );
	}

	/**
	 * Creates a packet for making an Asset request.
	 * 
	 * @param request
	 *        The absolute path of the asset requested.
	 * @return The packet data as a byte array.
	 * @throws Exception
	 *         The given request was too large to fit in the packet.
	 */
	public static byte[] getRequestPacket( String request ) throws IOException
	{
		byte[] path = request.getBytes();
		int pathLength = path.length;

		if (pathLength > 255)
		{
			throw new IOException( "Request cannot be more than 255 characters" );
		}

		int packetLength = 2 + pathLength;
		byte[] packet = new byte[packetLength];

		packet[0] = 0;
		packet[1] = (byte)pathLength;
		System.arraycopy( path, 0, packet, 2, pathLength );

		return packet;
	}

	/**
	 * Creates a packet for making an Asset response.
	 * 
	 * @param data
	 *        The asset as a byte array.
	 * @param request
	 *        The original request.
	 * @return The packet data as a byte array.
	 * @throws Exception
	 *         The given request was too large to fit in the packet.
	 */
	public static byte[] getResponsePacket( byte[] data, String request ) throws IOException
	{
		byte[] path = request.getBytes();
		int pathLength = path.length;

		if (pathLength > 255)
		{
			throw new IOException( "Request cannot be more than 255 characters" );
		}

		int packetLength = 2 + pathLength + 2 + data.length;
		byte[] packet = new byte[packetLength];

		packet[0] = 1;
		packet[1] = (byte)pathLength;
		System.arraycopy( path, 0, packet, 2, pathLength );

		packet[pathLength + 2] = (byte)((data.length >> 8) & 0xFF);
		packet[pathLength + 3] = (byte)((data.length >> 0) & 0xFF);

		System.arraycopy( data, 0, packet, pathLength + 4, data.length );

		return packet;
	}

	/**
	 * Returns the request string from the given request packet. If the packet is
	 * not a request packet (i.e. a response packet) or the packet is invalid
	 * null will be returned.
	 * 
	 * @param data
	 *        The packet data.
	 * @return The request in the request packet or null.
	 */
	public static String getRequestString( byte[] data )
	{
		if (data.length < 2)
		{
			return null;
		}

		if (data[0] != 0)
		{
			return null;
		}

		int length = data[1] & 0xFF;
		int actual = data.length - 2;

		if (actual < length)
		{
			return null;
		}

		return new String( data, 2, length );
	}

	/**
	 * Returns the response string from the given response packet. If the packet
	 * is not a response packet (i.e. a request packet) or the packet is invalid
	 * null will be returned.
	 * 
	 * @param data
	 *        The packet data.
	 * @return The response string in the response packet or null.
	 */
	public static String getResponseString( byte[] data )
	{
		if (data.length < 2)
		{
			return null;
		}

		if (data[0] != 1)
		{
			return null;
		}

		int length = data[1] & 0xFF;
		int actual = data.length - 2;

		if (actual < length)
		{
			return null;
		}

		return new String( data, 2, length );
	}

	/**
	 * Returns the size of the asset in bytes in the given response packet. If
	 * the packet is invalid or does not contain the complete asset then -1 is
	 * returned. A valid response packet with all of the asset will return a
	 * positive (possibly zero) integer.
	 * 
	 * @param data
	 *        The packet data from the response.
	 * @return The size of the asset in the response in bytes, or -1 if unknown.
	 */
	public static int getResponseSize( byte[] data )
	{
		// At least 4 bytes are required (flag, path length, asset length).
		if (data.length < 4)
		{
			return -1;
		}

		// Flag must equal 1
		if (data[0] != 1)
		{
			return -1;
		}

		int pathLength = data[1] & 0xFF;

		// Path length must exist in [0,255]
		if (pathLength < 0 || pathLength > 255)
		{
			return -1;
		}

		// Get the size of the response in bytes.
		int responseSize = ((data[pathLength + 2] & 0xFF) << 8) | (data[pathLength + 3] & 0xFF);
		int remaining = data.length - pathLength - 4;

		// If not enough data is in the packet, cannot read it!
		if (responseSize > remaining)
		{
			return -1;
		}

		// Valid response packet.
		return responseSize;
	}

}
