
package org.magnos.asset.ex;

/**
 * An exception thrown when an AssetFormat could not be determined.
 * 
 * @author Philip Diffenderfer
 *
 */
public class UnknownAssetFormatException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	private final String request;
	private final Class<?> requestType;
	private final String requestExtension;

	/**
	 * Instantiates a new UnknownAssetFormatException.
	 * 
	 * @param request
	 * 	The request for the asset.
	 * @param requestType
	 * 	The expected type of the request (if any).
	 * @param requestExtension
	 * 	The custom extension used to determine the format (if any).
	 */
	public UnknownAssetFormatException( String request, Class<?> requestType, String requestExtension )
	{
		super( "Could not determine format for request '" + request + "' and request type '" + requestType + "' and request extension '" + requestExtension + "'" );

		this.request = request;
		this.requestType = requestType;
		this.requestExtension = requestExtension;
	}

	/**
	 * Returns the request for the asset.
	 * 
	 * @return The request for the asset.
	 */
	public String getRequest()
	{
		return request;
	}

	/**
	 * Returns the expected type of the request (if any).
	 * 
	 * @return The expected type of the request (if any).
	 */
	public Class<?> getRequestType()
	{
		return requestType;
	}

	/**
	 * Returns the custom extension used to determine the format (if any).
	 * 
	 * @return The custom extension used to determine the format (if any).
	 */
	public String getRequestExtension()
	{
		return requestExtension;
	}

}
