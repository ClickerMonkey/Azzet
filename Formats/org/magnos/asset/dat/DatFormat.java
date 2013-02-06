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
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

import org.magnos.asset.AssetInfo;
import org.magnos.asset.FormatUtility;
import org.magnos.asset.base.BaseAssetFormat;
import org.magnos.asset.info.ByteBufferInfo;


/**
 * A format for loading byte[]s from DAT files.
 * 
 * <h2>Extensions</h2>
 * <ul>
 * <li>DAT - A Generic Binary File</li>
 * </ul>
 * 
 * <h2>Request Types<h2>
 * <ul>
 * <li>byte[]</li>
 * <li>short[]</li>
 * <li>int[]</li>
 * <li>long[]</li>
 * <li>float[]</li>
 * <li>double[]</li>
 * <li>{@link java.io.ByteArrayOutputStream}</li>
 * <li>{@link java.io.InputStream}</li>
 * <li>{@link java.nio.ByteBuffer}</li>
 * <li>{@link java.nio.ShortBuffer}</li>
 * <li>{@link java.nio.IntBuffer}</li>
 * <li>{@link java.nio.LongBuffer}</li>
 * <li>{@link java.nio.FloatBuffer}</li>
 * <li>{@link java.nio.DoubleBuffer}</li>
 * </ul>
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
		super( new String[] { "dat" }, byte[].class, ByteArrayOutputStream.class, InputStream.class, ByteBuffer.class,
			ShortBuffer.class, IntBuffer.class, LongBuffer.class, FloatBuffer.class, DoubleBuffer.class, short[].class,
			int[].class, long[].class, float[].class, double[].class );
	}
	
	public AssetInfo getInfo( Class<?> requestType )
	{
		return new ByteBufferInfo( requestType );
	}

	@Override
	public Object loadAsset( InputStream input, AssetInfo assetInfo ) throws Exception
	{
		ByteBufferInfo info = (ByteBufferInfo)assetInfo;
		
		Object asset = null;

		if (info.isType( byte[].class ))
		{
			asset = FormatUtility.getBytes( input );
		}
		else if (info.isType( ByteArrayOutputStream.class ))
		{
			asset = FormatUtility.getOutput( input );
		}
		else if (info.isType( InputStream.class ))
		{
			asset = new ByteArrayInputStream( FormatUtility.getBytes( input ) );
		}
		else if (info.isType( ByteBuffer.class ))
		{
			asset = wrap( info.isDirect(), FormatUtility.getBytes( input ) );
		}
		else if (info.isType( ShortBuffer.class ))
		{
			asset = wrap( info.isDirect(), FormatUtility.getBytes( input ) ).asShortBuffer();
		}
		else if (info.isType( IntBuffer.class ))
		{
			asset = wrap( info.isDirect(), FormatUtility.getBytes( input ) ).asIntBuffer();
		}
		else if (info.isType( LongBuffer.class ))
		{
			asset = wrap( info.isDirect(), FormatUtility.getBytes( input ) ).asLongBuffer();
		}
		else if (info.isType( FloatBuffer.class ))
		{
			asset = wrap( info.isDirect(), FormatUtility.getBytes( input ) ).asFloatBuffer();
		}
		else if (info.isType( DoubleBuffer.class ))
		{
			asset = wrap( info.isDirect(), FormatUtility.getBytes( input ) ).asDoubleBuffer();
		}
		else if (info.isType( short[].class ))
		{
			ShortBuffer buffer = wrap( info.isDirect(), FormatUtility.getBytes( input ) ).asShortBuffer();
			
			if ( buffer.hasArray() )
			{
				asset = buffer.array();
			}
			else
			{
				short[] data = new short[ buffer.limit() ];
				buffer.get( data );
				asset = data;
			}
		}
		else if (info.isType( int[].class ))
		{
			IntBuffer buffer = wrap( info.isDirect(), FormatUtility.getBytes( input ) ).asIntBuffer();
			
			if ( buffer.hasArray() )
			{
				asset = buffer.array();
			}
			else
			{
				int[] data = new int[ buffer.limit() ];
				buffer.get( data );
				asset = data;
			}
		}
		else if (info.isType( long[].class ))
		{
			LongBuffer buffer = wrap( info.isDirect(), FormatUtility.getBytes( input ) ).asLongBuffer();
			
			if ( buffer.hasArray() )
			{
				asset = buffer.array();
			}
			else
			{
				long[] data = new long[ buffer.limit() ];
				buffer.get( data );
				asset = data;
			}
		}
		else if (info.isType( float[].class ))
		{
			FloatBuffer buffer = wrap( info.isDirect(), FormatUtility.getBytes( input ) ).asFloatBuffer();
			
			if ( buffer.hasArray() )
			{
				asset = buffer.array();
			}
			else
			{
				float[] data = new float[ buffer.limit() ];
				buffer.get( data );
				asset = data;
			}
		}
		else if (info.isType( double[].class ))
		{
			DoubleBuffer buffer = wrap( info.isDirect(), FormatUtility.getBytes( input ) ).asDoubleBuffer();
			
			if ( buffer.hasArray() )
			{
				asset = buffer.array();
			}
			else
			{
				double[] data = new double[ buffer.limit() ];
				buffer.get( data );
				asset = data;
			}
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
