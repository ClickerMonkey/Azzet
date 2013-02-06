
package org.magnos.asset.ex;

/**
 * An exception thrown when an AssetSource could not be determined.
 * 
 * @author Philip Diffenderfer
 *
 */
public class UnknownAssetSourceException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	private final String request;
	private final String sourceName;

	/**
	 * Instantiates a new UnknownAssetSourceException.
	 * 
	 * @param request
	 * 	The request for the asset.
	 * @param sourceName
	 * 	The custom name used to determine the source (if any).
	 */
	public UnknownAssetSourceException( String request, String sourceName )
	{
		super( "Could not determine source for request '" + request + "' and source name '" + sourceName + "'" );

		this.request = request;
		this.sourceName = sourceName;
	}

	/**
	 * @return The request for the asset.
	 */
	public String getRequest()
	{
		return request;
	}

	/**
	 * @return The custom name used to determine the source (if any).
	 */
	public String getSourceName()
	{
		return sourceName;
	}

}
