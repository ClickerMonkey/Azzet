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

package org.magnos.asset;

import org.magnos.asset.ex.AssetException;


/**
 * The information associated with an asset. Enough information exists in the
 * information to load the asset at a later time.
 * 
 * @author Philip Diffenderfer
 * 
 */
public interface AssetInfo
{

	/**
	 * Returns true if the given type is compatible with the assets type.
	 * 
	 * @param type
	 *        The asset type.
	 * @return True if the types are compatible.
	 */
	public boolean isType( Class<?> type );

	/**
	 * Returns true if this AssetInfo has the same properties as the given info.
	 * 
	 * @param info
	 *        The info to compare to.
	 * @return True if the given info and this info are equivalent, otherwise
	 *         false.
	 */
	public boolean isMatch( AssetInfo info );

	/**
	 * Returns the asset type.
	 * 
	 * @return The asset type.
	 */
	public Class<?> getType();

	/**
	 * Returns the asset. If the asset has not been loaded by the
	 * {@link AssetInfo#load()} or {@link AssetInfo#set(Object)} methods then
	 * this will return null. Otherwise if either method performed successfully
	 * the expected asset will be returned.
	 * 
	 * @return The reference to the loaded asset or null if not loaded.
	 */
	public <A> A get();

	/**
	 * Returns the asset, loading it if it hasn't been loaded yet. If there has
	 * been an error loading the asset an exception will be thrown.
	 * 
	 * @return The reference to the loaded asset.
	 * @throws AssetException
	 *         An error occurred loading the asset with the given request from
	 *         the given source and in the given format.
	 */
	public <A> A load() throws AssetException;

	/**
	 * Creates the asset with the given request, source, and format but does not
	 * set the asset in this AssetInfo.
	 * 
	 * @return The reference to the loaded asset.
	 * @throws AssetException
	 *         An error occurred loading the asset with the given request from
	 *         the given source and in the given format.
	 */
	public <A> A create() throws AssetException;

	/**
	 * Sets the asset.
	 * 
	 * @param asset
	 *        The new asset.
	 */
	public <A> void set( A asset );

	/**
	 * Clears the asset from this AssetInfo. Any {@link AssetInfo#load()}
	 * invocations made after this will force a reload of the asset.
	 */
	public void clear();

	/**
	 * Returns the property with the given name.
	 * 
	 * @param <T>
	 *        The property type.
	 * @param name
	 *        The name of the property.
	 * @return The property value.
	 */
	public <T> T property( String name );

	/**
	 * Returns the property with the given name.
	 * 
	 * @param <T>
	 *        The property type.
	 * @param name
	 *        The name of the property.
	 * @param type
	 *        The property type.
	 * @return The property value.
	 */
	public <T> T property( String name, Class<T> type );

	/**
	 * Returns the name of all properties available for this AssetInfo.
	 * 
	 * @return The reference to the properties available for this AssetInfo. This
	 *         should not be modified externally, any modifications will be made
	 *         globally and will affect other callers of this method.
	 */
	public String[] getProperties();

	/**
	 * Sets the request, source, and format of the asset.
	 * 
	 * @param source
	 *        The source of the asset.
	 * @param format
	 *        The format of the asset.
	 * @param request
	 *        The request string to the asset.
	 */
	public void setInfo( AssetSource source, AssetFormat format, String request );

	/**
	 * Returns the format of the asset.
	 * 
	 * @return The reference to the format of the asset.
	 */
	public AssetFormat getFormat();

	/**
	 * Sets the format of the asset.
	 * 
	 * @param format
	 *        The new format of the asset.
	 */
	public void setFormat( AssetFormat format );

	/**
	 * Returns the source of the asset.
	 * 
	 * @return The reference to the source of the asset.
	 */
	public AssetSource getSource();

	/**
	 * Sets the source of the asset.
	 * 
	 * @param source
	 *        The new source of the asset.
	 */
	public void setSource( AssetSource source );

	/**
	 * Returns the request of the asset.
	 * 
	 * @return The reference to the request of the asset.
	 */
	public String getRequest();

	/**
	 * Sets the request of the asset.
	 * 
	 * @param request
	 *        The new request of the asset.
	 */
	public void setRequest( String request );

	/**
	 * Returns the path of the asset. The path is typically a unique identifier
	 * to the asset.
	 * 
	 * @return The reference to the path of the asset.
	 */
	public String getPath();

	/**
	 * Sets the path of the asset. This is typically done by invoking
	 * {@link AssetSource#getAbsolute(String)} passing the asset request in the
	 * method to get the path of the request.
	 * 
	 * @param path
	 *        The absolute path of the asset.
	 */
	public void setPath( String path );

}
