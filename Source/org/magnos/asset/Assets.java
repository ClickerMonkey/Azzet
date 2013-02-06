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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.magnos.asset.ex.UnknownAssetFormatException;
import org.magnos.asset.ex.UnknownAssetSourceException;


/**
 * The main class for accessing assets. By default assets are cached, but that
 * can be changed with {@link Assets#setCaching(boolean)}. To understand how the
 * source or format is determined given a request string look at
 * {@link Assets#getFormat(String)}, {@link Assets#getFormat(Class)}, and
 * {@link Assets#getSource(String)} respectively. The most detailed explanation
 * on how everything is determined is described at
 * {@link #info(String, String, Class, String, AssetInfo)} while caching is
 * described at {@link #get(AssetInfo)}.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class Assets
{

	/**
	 * AssetSources mapped by a user provided key. This key can be used to load
	 * from a specific source opposed to the default source (or a source
	 * determined by brute-force).
	 */
	private static Map<String, AssetSource> sources =
		new ConcurrentHashMap<String, AssetSource>();

	/**
	 * A cache of AssetInfos by the full path of the asset. If the same asset has
	 * been loaded by varying AssetInfos this map will only carry the most recent
	 * one, therefore any new request with different info will not take advantage
	 * of the cache and will reload the asset.
	 */
	private static Map<String, AssetInfo> assets =
		new ConcurrentHashMap<String, AssetInfo>();

	/**
	 * AssetFormats mapped by the extensions they parse.
	 */
	private static Map<String, AssetFormat> formatsByExtension =
		new ConcurrentHashMap<String, AssetFormat>();

	/**
	 * AssetFormats mapped by the output types they produce.
	 */
	private static Map<Class<?>, AssetFormat> formatsByType =
		new ConcurrentHashMap<Class<?>, AssetFormat>();

	/**
	 * The default AssetSource. If this is non-null and a request is valid for
	 * this source then this will be used, otherwise a source will be determined
	 * through brute-force.
	 */
	private static AssetSource defaultSource;

	/**
	 * The default AssetFormat. If a format cannot be determined based on
	 * extension our expected output type this format will be used.
	 */
	private static AssetFormat defaultFormat;

	/**
	 * Whether AssetInfos are cached.
	 */
	private static boolean cache = true;

	/**
	 * The number of cache hits.
	 */
	private static int cacheHits = 0;

	/**
	 * The number of cache misses.
	 */
	private static int cacheMisses = 0;

	/**
	 * Loads an asset based solely on the request. This is equivalent to:
	 * 
	 * <pre>
	 * get( info( request, null, null, null, null ) )
	 * </pre>
	 * 
	 * @param <A>
	 *        The type to automatically cast to.
	 * @param request
	 *        The request used to determine formats, sources, and subsequently
	 *        the full path of the asset.
	 * @return The requested asset.
	 * @see #info(String, String, Class, String, AssetInfo)
	 * @see #get(AssetInfo)
	 */
	public static <A> A load( String request )
	{
		return get( info( request, null, null, null, null ) );
	}

	/**
	 * Loads an asset based on the request and an expected return type. This is
	 * equivalent to:
	 * 
	 * <pre>
	 * get( info( request, null, requestType, null, null ) )
	 * </pre>
	 * 
	 * @param <A>
	 *        The type to automatically cast to.
	 * @param request
	 *        The request used to determine formats, sources, and subsequently
	 *        the full path of the asset.
	 * @param requestType
	 *        If given, the format of the asset will be determined using the
	 *        format that was registered that had this type as one of it's
	 *        possible outputs. If the lookup returns nothing the normal format
	 *        determination will be used.
	 * @return The requested asset.
	 * @see #info(String, String, Class, String, AssetInfo)
	 * @see #get(AssetInfo)
	 */
	public static <A> A load( String request, Class<A> requestType )
	{
		return get( info( request, null, requestType, null, null ) );
	}

	/**
	 * Loads an asset based on the request and extension. This is equivalent to:
	 * 
	 * <pre>
	 * get( info( request, formatExtension, null, null, null ) )
	 * </pre>
	 * 
	 * @param <A>
	 *        The type to automatically cast to.
	 * @param request
	 *        The request used to determine formats, sources, and subsequently
	 *        the full path of the asset.
	 * @param requestExtension
	 *        If given, the format of the asset will be determined using the
	 *        format the was registered that had this extension as one of it's
	 *        possible inputs. If the lookup returns nothing the normal format
	 *        determination will be used. This argument is ignored if requestType
	 *        is provided.
	 * @return The requested asset.
	 * @see #info(String, String, Class, String, AssetInfo)
	 * @see #get(AssetInfo)
	 */
	public static <A> A load( String request, String requestExtension )
	{
		return get( info( request, requestExtension, null, null, null ) );
	}

	/**
	 * Loads an asset based on the request and user specified AssetInfo. This is
	 * equivalent to:
	 * 
	 * <pre>
	 * get( info( request, null, null, null, requestInfo ) )
	 * </pre>
	 * 
	 * @param <A>
	 *        The type to automatically cast to.
	 * @param request
	 *        The request used to determine formats, sources, and subsequently
	 *        the full path of the asset.
	 * @param requestInfo
	 *        If given, this AssetInfo is set and used to load the asset.
	 *        Otherwise The AssetInfo is determined by requestType if given, or
	 *        the default AssetInfo for the determined AssetFormat.
	 * @return The requested asset.
	 * @see #info(String, String, Class, String, AssetInfo)
	 * @see #get(AssetInfo)
	 */
	public static <A> A load( String request, AssetInfo requestInfo )
	{
		return get( info( request, null, null, null, requestInfo ) );
	}

	/**
	 * Loads an asset based on the request, extension, and user specified
	 * AssetInfo. This is equivalent to:
	 * 
	 * <pre>
	 * get( info( request, requestExtension, null, null, requestInfo ) )
	 * </pre>
	 * 
	 * @param <A>
	 *        The type to automatically cast to.
	 * @param request
	 *        The request used to determine formats, sources, and subsequently
	 *        the full path of the asset.
	 * @param requestExtension
	 *        If given, the format of the asset will be determined using the
	 *        format the was registered that had this extension as one of it's
	 *        possible inputs. If the lookup returns nothing the normal format
	 *        determination will be used. This argument is ignored if requestType
	 *        is provided.
	 * @param requestInfo
	 *        If given, this AssetInfo is set and used to load the asset.
	 *        Otherwise The AssetInfo is determined by requestType if given, or
	 *        the default AssetInfo for the determined AssetFormat.
	 * @return The requested asset.
	 * @see #info(String, String, Class, String, AssetInfo)
	 * @see #get(AssetInfo)
	 */
	public static <A> A load( String request, String requestExtension, AssetInfo requestInfo )
	{
		return get( info( request, requestExtension, null, null, requestInfo ) );
	}

	/**
	 * Loads an asset from a specified source based solely on the request. This
	 * is equivalent to:
	 * 
	 * <pre>
	 * get( info( request, null, null, sourceName, null ) )
	 * </pre>
	 * 
	 * @param <A>
	 *        The type to automatically cast to.
	 * @param request
	 *        The request used to determine formats, sources, and subsequently
	 *        the full path of the asset.
	 * @param sourceName
	 *        If given, the source of the asset will be determined using this
	 *        name to lookup an AssetSource. If the lookup returns nothing the
	 *        normal source determination will be used.
	 * @return The requested asset.
	 * @see #info(String, String, Class, String, AssetInfo)
	 * @see #get(AssetInfo)
	 */
	public static <A> A loadFrom( String request, String sourceName )
	{
		return get( info( request, null, null, sourceName, null ) );
	}

	/**
	 * Loads an asset from a specified source based on the request and an
	 * expected return type. This is equivalent to:
	 * 
	 * <pre>
	 * get( info( request, null, requestType, sourceName, null ) )
	 * </pre>
	 * 
	 * @param <A>
	 *        The type to automatically cast to.
	 * @param request
	 *        The request used to determine formats, sources, and subsequently
	 *        the full path of the asset.
	 * @param sourceName
	 *        If given, the source of the asset will be determined using this
	 *        name to lookup an AssetSource. If the lookup returns nothing the
	 *        normal source determination will be used.
	 * @param requestType
	 *        If given, the format of the asset will be determined using the
	 *        format that was registered that had this type as one of it's
	 *        possible outputs. If the lookup returns nothing the normal format
	 *        determination will be used.
	 * @return The requested asset.
	 * @see #info(String, String, Class, String, AssetInfo)
	 * @see #get(AssetInfo)
	 */
	public static <A> A loadFrom( String request, String sourceName, Class<A> requestType )
	{
		return get( info( request, null, requestType, sourceName, null ) );
	}

	/**
	 * Loads an asset from a specified source based on the request and extension.
	 * This is equivalent to:
	 * 
	 * <pre>
	 * get( info( request, requestExtension, null, sourceName, null ) )
	 * </pre>
	 * 
	 * @param <A>
	 *        The type to automatically cast to.
	 * @param request
	 *        The request used to determine formats, sources, and subsequently
	 *        the full path of the asset.
	 * @param sourceName
	 *        If given, the source of the asset will be determined using this
	 *        name to lookup an AssetSource. If the lookup returns nothing the
	 *        normal source determination will be used.
	 * @param requestExtension
	 *        If given, the format of the asset will be determined using the
	 *        format the was registered that had this extension as one of it's
	 *        possible inputs. If the lookup returns nothing the normal format
	 *        determination will be used. This argument is ignored if requestType
	 *        is provided.
	 * @return The requested asset.
	 * @see #info(String, String, Class, String, AssetInfo)
	 * @see #get(AssetInfo)
	 */
	public static <A> A loadFrom( String request, String sourceName, String requestExtension )
	{
		return get( info( request, requestExtension, null, sourceName, null ) );
	}

	/**
	 * Loads an asset from a specified source based on the request and user
	 * specified AssetInfo. This is equivalent to:
	 * 
	 * <pre>
	 * get( info( request, null, null, sourceName, requestInfo ) )
	 * </pre>
	 * 
	 * @param <A>
	 *        The type to automatically cast to.
	 * @param request
	 *        The request used to determine formats, sources, and subsequently
	 *        the full path of the asset.
	 * @param sourceName
	 *        If given, the source of the asset will be determined using this
	 *        name to lookup an AssetSource. If the lookup returns nothing the
	 *        normal source determination will be used.
	 * @param requestInfo
	 *        If given, this AssetInfo is set and used to load the asset.
	 *        Otherwise The AssetInfo is determined by requestType if given, or
	 *        the default AssetInfo for the determined AssetFormat.
	 * @return The requested asset.
	 * @see #info(String, String, Class, String, AssetInfo)
	 * @see #get(AssetInfo)
	 */
	public static <A> A loadFrom( String request, String sourceName, AssetInfo requestInfo )
	{
		return get( info( request, null, null, sourceName, requestInfo ) );
	}

	/**
	 * Loads an asset from a specified source based on the request, extension,
	 * and user specified AssetInfo. This is equivalent to:
	 * 
	 * <pre>
	 * get( info( request, requestExtension, null, sourceName, requestInfo ) )
	 * </pre>
	 * 
	 * @param <A>
	 *        The type to automatically cast to.
	 * @param request
	 *        The request used to determine formats, sources, and subsequently
	 *        the full path of the asset.
	 * @param sourceName
	 *        If given, the source of the asset will be determined using this
	 *        name to lookup an AssetSource. If the lookup returns nothing the
	 *        normal source determination will be used.
	 * @param requestExtension
	 *        If given, the format of the asset will be determined using the
	 *        format the was registered that had this extension as one of it's
	 *        possible inputs. If the lookup returns nothing the normal format
	 *        determination will be used. This argument is ignored if requestType
	 *        is provided.
	 * @param requestInfo
	 *        If given, this AssetInfo is set and used to load the asset.
	 *        Otherwise The AssetInfo is determined by requestType if given, or
	 *        the default AssetInfo for the determined AssetFormat.
	 * @return The requested asset.
	 * @see #info(String, String, Class, String, AssetInfo)
	 * @see #get(AssetInfo)
	 */
	public static <A> A loadFrom( String request, String sourceName, String requestExtension, AssetInfo requestInfo )
	{
		return get( info( request, requestExtension, null, sourceName, requestInfo ) );
	}

	/**
	 * Gets an asset based on the provided AssetInfo. If an asset with the same
	 * path, type, and info is cached it will be returned, otherwise a new asset
	 * will be created and possibly cached for future use.
	 * 
	 * @param <A>
	 *        The type to automatically cast to.
	 * @param info
	 *        The info to use to load an asset.
	 * @return The requested asset.
	 * @throws ClassCastException
	 *         The format doesn't actually produce an asset of the expected type.
	 */
	public static <A> A get( AssetInfo info )
	{
		AssetInfo existingInfo = assets.get( info.getPath() );

		A asset = null;

		if (existingInfo == null || !info.isType( existingInfo.getType() ) || !existingInfo.isMatch( info ))
		{
			asset = info.load();

			if (cache)
			{
				assets.put( info.getPath(), info );
			}

			cacheMisses++;
		}
		else
		{
			asset = (A)existingInfo.get();

			cacheHits++;
		}

		return asset;
	}

	/**
	 * Builds an AssetInfo depending on the values passed in.
	 * 
	 * @param request
	 *        The request used to determine formats, sources, and subsequently
	 *        the full path of the asset. The full path of the asset is saved in
	 *        the AssetInfo returned and may be used to cache the AssetInfo and
	 *        asset.
	 * @param requestExtension
	 *        If given, the format of the asset will be determined using the
	 *        format the was registered that had this extension as one of it's
	 *        possible inputs. If the lookup returns nothing the normal format
	 *        determination will be used. This argument is ignored if requestType
	 *        is provided.
	 * @param requestType
	 *        If given, the format of the asset will be determined using the
	 *        format that was registered that had this type as one of it's
	 *        possible outputs. If the lookup returns nothing the normal format
	 *        determination will be used.
	 * @param sourceName
	 *        If given, the source of the asset will be determined using this
	 *        name to lookup an AssetSource. If the lookup returns nothing the
	 *        normal source determination will be used.
	 * @param requestInfo
	 *        If given, this AssetInfo is set and used to load the asset.
	 *        Otherwise The AssetInfo is determined by requestType if given, or
	 *        the default AssetInfo for the determined AssetFormat.
	 * @return An AssetInfo with the given request, determined format and source,
	 *         and the calculated full path of the asset.
	 * @throws UnknownAssetFormatException
	 *         The format could not be determined based on request,
	 *         requestExtension, requestType, or the default format.
	 * @throws UnknownAssetSourceException
	 *         The source could not be determined based on request, sourceName,
	 *         the default source, or any other sources registered.
	 */
	public static AssetInfo info( String request, String requestExtension, Class<?> requestType, String sourceName, AssetInfo requestInfo )
	{
		AssetFormat format = null;

		// Expected type has priority over extension
		if (requestType != null)
		{
			format = formatsByType.get( requestType );
		}
		else if (requestExtension != null)
		{
			format = formatsByExtension.get( requestExtension );
		}
		// Request type and extension were not given or did not find a format
		if (format == null)
		{
			format = getFormat( request );

			// A format could not be found at all, error!
			if (format == null)
			{
				throw new UnknownAssetFormatException( request, requestType, requestExtension );
			}
		}

		AssetSource source = null;

		// If sourceName is given, use it.
		if (sourceName != null)
		{
			source = sources.get( sourceName );
		}
		// Source name were not given or did not find a source
		if (source == null)
		{
			source = getSource( request );

			// A source could not be found at all, error!
			if (source == null)
			{
				throw new UnknownAssetSourceException( request, sourceName );
			}
		}

		// The default value is existing AssetInfo if any given.
		AssetInfo info = requestInfo;

		// If there was no given info and requestType was given, use that to
		// return the proper AssetInfo.
		if (info == null && requestType != null)
		{
			info = format.getInfo( requestType );
		}
		// Otherwise use the format to create a generic AssetInfo.
		if (info == null)
		{
			info = format.getInfo();
		}

		// Set the source, format, and request for the info. This also calculates
		// the full path of the asset.
		info.setInfo( source, format, request );

		return info;
	}

	/**
	 * Unloads the asset found with the given request if one has been cached. If
	 * the source is successfully determined and a cached asset exists it it
	 * cleared and removed from the cache.
	 * 
	 * @param request
	 *        The request used to determine formats, sources, and subsequently
	 *        the full path of the asset. The full path of the asset is saved in
	 *        the AssetInfo returned and may be used to cache the AssetInfo and
	 *        asset.
	 */
	public static void unload( String request )
	{
		AssetSource source = getSource( request );

		String path = source.getAbsolute( request );

		AssetInfo removedInfo = assets.remove( path );

		if (removedInfo != null)
		{
			removedInfo.clear();
		}
	}

	/**
	 * Unloads the asset with the path in the given AssetInfo if one has been
	 * cached. If an AssetInfo exists with the path it will be cleared and
	 * removed from the cache.
	 * 
	 * @param assetInfo
	 *        The AssetInfo to use to unload an asset based on path.
	 */
	public static void unload( AssetInfo assetInfo )
	{
		AssetInfo removedInfo = assets.remove( assetInfo.getPath() );

		if (removedInfo != null)
		{
			removedInfo.clear();
		}
	}

	/**
	 * Unloads all cached assets.
	 */
	public static void unloadAll()
	{
		for (AssetInfo removedInfo : assets.values())
		{
			removedInfo.clear();
		}

		assets.clear();
	}

	/**
	 * Determine the extension of the given request. If there is no extension on
	 * the request then the entirety of the request is returned as the extension.
	 * 
	 * @param request
	 *        The request to determine the extension of.
	 * @return The extension of the request, or the request if no extension was
	 *         found.
	 */
	public static String getExtension( String request )
	{
		int i = request.lastIndexOf( '.' );

		return (i < 0 || i == request.length() - 1 ? request : request.substring( i + 1 ));
	}

	/**
	 * Returns the cached AssetInfo given the full path of an asset.
	 * 
	 * @param path
	 *        The full path of an asset.
	 * @return The cached AssetInfo or null if none exists.
	 */
	public static AssetInfo getInfo( String path )
	{
		return assets.get( path );
	}

	/**
	 * Gets the default AssetSource.
	 * 
	 * @return The default source if any exists.
	 * @see #getSource(String)
	 */
	public static AssetSource getDefaultSource()
	{
		return defaultSource;
	}

	/**
	 * Sets the default AssetSource.
	 * 
	 * @param source
	 *        The new default AssetSource.
	 * @see #getSource(String)
	 */
	public static void setDefaultSource( AssetSource source )
	{
		defaultSource = source;
	}

	/**
	 * Sets the default AssetSource based on name.
	 * 
	 * @param name
	 *        The name of the AssetSource that should be the default source.
	 * @see #getSource(String)
	 */
	public static void setDefaultSource( String name )
	{
		defaultSource = sources.get( name );
	}

	/**
	 * Adds a new possible source with the given name.
	 * 
	 * @param name
	 *        The name of the source. This can be used on any of the loadFrom
	 *        methods to specify the source directly.
	 * @param source
	 */
	public static void addSource( String name, AssetSource source )
	{
		sources.put( name, source );
	}

	/**
	 * Returns a source based on the given request. If the default AssetSource is
	 * non-null and it finds the request valid, it will be returned. Otherwise
	 * all sources will be iterated and the first valid source will be returned.
	 * If no valid source is returned the default source is returned even if the
	 * request was not valid for it.
	 * 
	 * @param request
	 *        The request to use to determine a source for.
	 * @return The source determined.
	 */
	public static AssetSource getSource( String request )
	{
		if (defaultSource != null && defaultSource.isValid( request ))
		{
			return defaultSource;
		}

		for (AssetSource source : sources.values())
		{
			if (source.isValid( request ))
			{
				return source;
			}
		}

		return defaultSource;
	}

	/**
	 * Gets the default AssetFormat.
	 * 
	 * @return The default format if any exists.
	 * @see #getFormat(Class)
	 * @see #getFormat(String)
	 */
	public static AssetFormat getDefaultFormat()
	{
		return defaultFormat;
	}

	/**
	 * Sets the default AssetFormat.
	 * 
	 * @param format
	 *        The new default AssetFormat.
	 * @see #getFormat(Class)
	 * @see #getFormat(String)
	 */
	public static void setDefaultFormat( AssetFormat format )
	{
		defaultFormat = format;
	}

	/**
	 * Sets the default AssetFormat.
	 * 
	 * @param extension
	 *        The extension of the AssetFormat that should be the default format.
	 * @see #getFormat(Class)
	 * @see #getFormat(String)
	 */
	public static void setDefaultFormat( String extension )
	{
		defaultFormat = formatsByExtension.get( extension );
	}

	/**
	 * Adds a new format based on the format's extensions and available request
	 * types. If there are already AssetFormats registered with any of the
	 * extensions or request types they will be override with this format.
	 * 
	 * @param format
	 *        The AssetFormat to add.
	 */
	public static void addFormat( AssetFormat format )
	{
		for (String ext : format.getExtensions())
		{
			formatsByExtension.put( ext, format );
		}

		for (Class<?> cls : format.getRequestTypes())
		{
			formatsByType.put( cls, format );
		}
	}

	/**
	 * Adds new formats based on the format's extensions and available request
	 * types. If there are already AssetFormats registered with any of the
	 * extensions or request types they will be override with this format.
	 * 
	 * @param formats
	 *        The array of AssetFormat to add.
	 */
	public static void addFormats( AssetFormat... formats )
	{
		for (AssetFormat format : formats)
		{
			addFormat( format );
		}
	}

	/**
	 * Returns a format based on the given request. The extension of the request
	 * is used to lookup a format, if none were found the default format is
	 * returned.
	 * 
	 * @param request
	 *        The request to use to determine a format for.
	 * @return A format of the request or null if none could be determined.
	 */
	public static AssetFormat getFormat( String request )
	{
		String extension = getExtension( request );

		AssetFormat format = formatsByExtension.get( extension );

		return (format == null ? defaultFormat : format);
	}

	/**
	 * Returns a format based on the given request. The request type is used to
	 * lookup a format, if none were found the default format is returned.
	 * 
	 * @param requestType
	 *        The type to use to determine a format for.
	 * @return A format of the request type or null if none could be determined.
	 */
	public static AssetFormat getFormat( Class<?> requestType )
	{
		AssetFormat format = formatsByType.get( requestType );

		return (format == null ? defaultFormat : format);
	}

	/**
	 * Enables or Disables asset caching. By default assets will be cached.
	 * 
	 * @param cache
	 *        True if assets should be cached, otherwise false.
	 */
	public static void setCaching( boolean cache )
	{
		Assets.cache = cache;
	}

	/**
	 * Whether asset caching is enabled or disabled.
	 * 
	 * @return True if caching is enabled, otherwise false.
	 */
	public static boolean isCaching()
	{
		return cache;
	}

	/**
	 * @return The number of assets returned that were cached and didn't have to
	 *         be re-loaded.
	 */
	public static int getCacheHits()
	{
		return cacheHits;
	}

	/**
	 * @return The number of assets that had to be loaded and did not take
	 *         advantage of caching.
	 */
	public static int getCacheMisses()
	{
		return cacheMisses;
	}
	
	/**
	 * Removes all formats, sources, and their defaults.
	 */
	public static void reset()
	{
		formatsByExtension.clear();
		formatsByType.clear();
		sources.clear();
		
		defaultSource = null;
		defaultFormat = null;
	}

}
