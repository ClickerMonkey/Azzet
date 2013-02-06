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

package org.magnos.asset.dat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.magnos.asset.AssetInfo;
import org.magnos.asset.FormatUtility;
import org.magnos.asset.base.BaseAssetFormat;


/**
 * A format for loading byte[]s from DAT files.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class DatFormat extends BaseAssetFormat
{

	/**
	 * Instantiates a new DatFormat.
	 */
	public DatFormat()
	{
		super( new String[] { "dat" }, byte[].class, ByteArrayOutputStream.class, InputStream.class, ByteBuffer.class );
	}

	@Override
	public Object loadAsset( InputStream input, AssetInfo info ) throws Exception
	{
		Object asset = null;

		if (info.isType( byte[].class ))
		{
			// return byte[]
			asset = FormatUtility.getBytes( input );
		}
		else if (info.isType( ByteArrayOutputStream.class ))
		{
			// return ByteArrayOutputStream
			asset = FormatUtility.getOutput( input );
		}
		else if (info.isType( InputStream.class ))
		{
			// return InputStream
			asset = new ByteArrayInputStream( FormatUtility.getBytes( input ) );
		}
		else if (info.isType( ByteBuffer.class ))
		{
			// return ByteBuffer
			asset = wrap( info.property( "direct", Boolean.class ), FormatUtility.getBytes( input ) );
		}

		return asset;
	}

	/**
	 * Wraps the given bytes with a ByteBuffer.
	 * 
	 * @param direct
	 *        True if the ByteBuffer should be allocated via
	 *        {@link ByteBuffer#allocateDirect(int)} or false if it should be
	 *        allocated via {@link ByteBuffer#allocate(int)}.
	 * @param data
	 *        The bytes to wrap.
	 * @return A ByteBuffer containing the given bytes.
	 */
	public static ByteBuffer wrap( boolean direct, byte[] data )
	{
		ByteBuffer b = (direct ? ByteBuffer.allocateDirect( data.length ) : ByteBuffer.allocate( data.length ));
		b.put( data );
		b.flip();
		return b;
	}

}
