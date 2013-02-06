
package org.magnos.asset.info;

import java.nio.ByteBuffer;

import org.magnos.asset.AssetInfo;
import org.magnos.asset.base.BaseAssetInfo;


/**
 * Information for loading a ByteBuffer.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class ByteBufferInfo extends BaseAssetInfo
{

	/**
	 * The default value for whether the ByteBuffer is direct.
	 */
	public static final boolean DEFAULT_DIRECT = false;

	/**
	 * The default value for the request type of the AssetInfo.
	 */
	public static final Class<?> DEFAULT_REQUEST_TYPE = ByteBuffer.class;
	
	// properties
	public static final String[] PROPERTIES = {
		"direct"
	};
	
	// If the ByteBuffer is Direct, if not it is Heap.
	private final boolean direct;

	/**
	 * Instantiates a new ByteBufferInfo for the Heap.
	 */
	public ByteBufferInfo()
	{
		this( DEFAULT_REQUEST_TYPE, DEFAULT_DIRECT );
	}

	/**
	 * Instantiates a new ByteBufferInfo.
	 * 
	 * @param direct
	 *        True if the ByteBuffer is allocated directly in memory outside of
	 *        the JVM, or false if the ByteBuffer is allocated to the Heap.
	 */
	public ByteBufferInfo( boolean direct )
	{
		this( DEFAULT_REQUEST_TYPE, direct );
	}

	/**
	 * Instantiates a new ByteBufferInfo.
	 * 
	 * @param requestType
	 * 		  The request type of the info.
	 */
	public ByteBufferInfo( Class<?> requestType )
	{
		this( requestType, DEFAULT_DIRECT );
	}

	/**
	 * Instantiates a new ByteBufferInfo.
	 * 
	 * @param requestType
	 * 		  The request type of the info.
	 * @param direct
	 *        True if the ByteBuffer is allocated directly in memory outside of
	 *        the JVM, or false if the ByteBuffer is allocated to the Heap.
	 */
	public ByteBufferInfo( Class<?> requestType, boolean direct )
	{
		super( requestType );

		this.direct = direct;
	}

	@Override
	protected Object getProperty( String name )
	{
		if (name.equals( "direct" )) return direct;

		return null;
	}

	@Override
	public boolean isMatch( AssetInfo info )
	{
		if ( info instanceof ByteBufferInfo )
		{
			ByteBufferInfo byteBuffer = (ByteBufferInfo)info;
			
			return (
				byteBuffer.direct == direct
			);
		}
		
		return false;
	}
	
	@Override
	public String[] getProperties()
	{
		return PROPERTIES;
	}

	/**
	 * Returns whether the ByteBuffer should be allocated directly.
	 * 
	 * @return True if the ByteBuffer is allocated directly in memory outside of
	 *         the JVM, or false if the ByteBuffer is allocated to the Heap.
	 */
	public boolean isDirect()
	{
		return direct;
	}

}
