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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.magnos.asset.AssetSource;
import org.magnos.asset.base.BaseAssetSource;


/**
 * A source that uses regular expressions to examine the request to determine
 * another source to use.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class SmartSource extends BaseAssetSource
{

	public static final String DEFAULT_BASE = "";
	public static final boolean DEFAULT_LOAD_DEFAULTS = true;

	public static final int PATTERN_GROUP = 1;
	public static final Pattern PATTERN_WEB = Pattern.compile( "^(http:.*|https:.*|ftp:.*)$" );
	public static final Pattern PATTERN_FILES = Pattern.compile( "^(\\./.*|/|[a-zA-Z]:[\\\\/].*)$" );
	public static final Pattern PATTERN_DATABASE = Pattern.compile( "^db:(.*)$" );
	public static final Pattern PATTERN_JAR = Pattern.compile( "^jar:(.*)$" );
	public static final Pattern PATTERN_FTP = Pattern.compile( "^ftp:(.*)$" );
	public static final Pattern PATTERN_MULTICAST = Pattern.compile( "^multicast:(.*)$" );
	public static final Pattern PATTERN_SSL = Pattern.compile( "^ssl:(.*)$" );
	public static final Pattern PATTERN_TCP = Pattern.compile( "^tcp:(.*)$" );
	public static final Pattern PATTERN_UDP = Pattern.compile( "^udp:(.*)$" );

	private ArrayList<PatternedSource> sources = new ArrayList<PatternedSource>();
	private AssetSource defaultSource;

	/**
	 * Instantiates a new SmartSource with the default patterns (file, web) and
	 * default source of class-path.
	 */
	public SmartSource()
	{
		this( DEFAULT_LOAD_DEFAULTS );
	}

	/**
	 * Instantiates a new SmartSource.
	 * 
	 * @param loadDefaults
	 *        If true this will load the default patterns(file, web) and default
	 *        source of class-path.
	 */
	public SmartSource( boolean loadDefaults )
	{
		super( null, DEFAULT_BASE, DEFAULT_BASE );

		if (loadDefaults)
		{
			add( new FileSource(), PATTERN_FILES, PATTERN_GROUP );
			add( new WebSource(), PATTERN_WEB, PATTERN_GROUP );

			defaultSource = new ClasspathSource();
		}
	}

	@Override
	public InputStream getStream( String request ) throws Exception
	{
		final String requestLower = request.toLowerCase();

		for (int i = sources.size() - 1; i >= 0; i--)
		{
			final PatternedSource source = sources.get( i );
			final Matcher matcher = source.pattern.matcher( requestLower );

			if (matcher.matches())
			{
				return source.source.getStream( matcher.group( source.patternGroup ) );
			}
		}

		return defaultSource.getStream( request );
	}

	/**
	 * Returns a matching source based on the request.
	 * 
	 * @param request
	 *        The request to find a matching source for.
	 * @return The matching source, or null if non matched the request.
	 */
	public PatternedSource getMatchingSource( String request )
	{
		String requestLower = request.toLowerCase();

		for (int i = sources.size() - 1; i >= 0; i--)
		{
			PatternedSource patternedSource = sources.get( i );
			Matcher matcher = patternedSource.pattern.matcher( requestLower );

			if (matcher.matches())
			{
				return patternedSource;
			}
		}

		return null;
	}

	/**
	 * Adds a new DatabaseSource with the default pattern.
	 * 
	 * @param source
	 *        The new DatabaseSource.
	 */
	public void addDatabase( DatabaseSource source )
	{
		add( source, PATTERN_DATABASE, PATTERN_GROUP );
	}

	/**
	 * Adds a new FileSource with the default pattern.
	 * 
	 * @param source
	 *        The new FileSource.
	 */
	public void addFile( FileSource source )
	{
		add( source, PATTERN_FILES, PATTERN_GROUP );
	}

	/**
	 * Adds a new FtpSource with the default pattern.
	 * 
	 * @param source
	 *        The new FtpSource.
	 */
	public void addFtp( FtpSource source )
	{
		add( source, PATTERN_FTP, PATTERN_GROUP );
	}

	/**
	 * Adds a new JarSource with the default pattern.
	 * 
	 * @param source
	 *        The new JarSource.
	 */
	public void addJar( JarSource source )
	{
		add( source, PATTERN_JAR, PATTERN_GROUP );
	}

	/**
	 * Adds a new MulticastSource with the default pattern.
	 * 
	 * @param source
	 *        The new MulticastSource.
	 */
	public void addMulticast( MulticastSource source )
	{
		add( source, PATTERN_MULTICAST, PATTERN_GROUP );
	}

	/**
	 * Adds a new SslSource with the default pattern.
	 * 
	 * @param source
	 *        The new SslSource.
	 */
	public void addSsl( SslSource source )
	{
		add( source, PATTERN_SSL, PATTERN_GROUP );
	}

	/**
	 * Adds a new TcpSource with the default pattern.
	 * 
	 * @param source
	 *        The new TcpSource.
	 */
	public void addTcp( TcpSource source )
	{
		add( source, PATTERN_TCP, PATTERN_GROUP );
	}

	/**
	 * Adds a new UdpSource with the default pattern.
	 * 
	 * @param source
	 *        The new UdpSource.
	 */
	public void addUdp( UdpSource source )
	{
		add( source, PATTERN_UDP, PATTERN_GROUP );
	}

	/**
	 * Adds a new WebSource with the default pattern.
	 * 
	 * @param source
	 *        The new WebSource.
	 */
	public void addWeb( WebSource source )
	{
		add( source, PATTERN_WEB, PATTERN_GROUP );
	}

	/**
	 * Adds a new patterned source. The new source added will have highest
	 * priority and will be checked first.
	 * 
	 * @param source
	 *        The AssetSource to add.
	 * @param pattern
	 *        The pattern used to match the request to a source and with the
	 *        patternGroup return the true request to be passed to source.
	 * @param patternGroup
	 *        The group in the regular expression that represents the true
	 *        request to be passed to source.
	 */
	public void add( AssetSource source, Pattern pattern, int patternGroup )
	{
		sources.add( new PatternedSource( source, pattern, patternGroup ) );
	}

	/**
	 * Get the default AssetSource.
	 * 
	 * @return The reference to the default AssetSource or null if there is none.
	 */
	public AssetSource getDefaultSource()
	{
		return defaultSource;
	}

	/**
	 * Set the default AssetSource.
	 * 
	 * @param defaultSource
	 *        The new default AssetSource.
	 */
	public void setDefaultSource( AssetSource defaultSource )
	{
		this.defaultSource = defaultSource;
	}

	/**
	 * Get the list of patterned sources already added to this SmartSource.
	 * 
	 * @return The reference to the internal ArrayList of PatternedSources.
	 */
	public ArrayList<PatternedSource> getSources()
	{
		return sources;
	}

	/**
	 * A simple class holding a regular expression pattern, the accompanying
	 * AssetSource, and the group in the pattern to use to determine the true
	 * request for the source.
	 * 
	 * @author Philip Diffenderfer
	 * 
	 */
	public class PatternedSource
	{

		public final AssetSource source;
		public final Pattern pattern;
		public final int patternGroup;

		public PatternedSource( AssetSource source, Pattern pattern, int patternGroup )
		{
			this.source = source;
			this.pattern = pattern;
			this.patternGroup = patternGroup;
		}
	}

}
